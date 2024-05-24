package com.helha.java.q2.cinephile.Controllers;

import com.helha.java.q2.cinephile.Models.Film;
import com.helha.java.q2.cinephile.Models.FilmDb;
import com.helha.java.q2.cinephile.Models.Tiquet;
import com.helha.java.q2.cinephile.Models.TiquetDb;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CentralServer {
    private static final int PORT = 12345;
    private static List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    static int NombreTiquets;
    static int NombreDeTiquetEnfant;
    static int NombreDeTiquetAdulte;
    static int NombreDeTiquetSenior;
    static int FilmId;
    static int room;
    static String hour;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Central server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ClientHandler(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Object message = in.readObject();
                    if (message instanceof String command) {
                        if (command.equals("GET_FILMS")) {
                            sendFilms();
                            System.out.println("Films sent to the client");
                        } else if (command.startsWith("SEND_PAYMENT")) {
                            sendPaymentToTerminal(command);
                            System.out.println("reponse envoyer au terminal");
                        } else if (command.startsWith("RESEND_PAYMENTRESPONSE")) {
                            sendPaymentToMain(command);
                            System.out.println("reponse envoyer au main");

                        } else if (command.equals("GET_TIQUETS")) {
                            sendTiquets();
                            System.out.println("Films sent to the client");
                        }
                    }
                }
            } catch (EOFException e) {
                System.out.println("Client disconnected.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.remove(this);
            }
        }

        private void sendFilms() {
            FilmDb filmDb = new FilmDb();
            List<Film> films = filmDb.getAllFilms();
            try {
                out.writeObject(films);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private void sendTiquets() {
            TiquetDb tiquetDb = new TiquetDb();
            List<Tiquet> tiquets = tiquetDb.getAllTiquets();
            try {
                out.writeObject(tiquets);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendPaymentToTerminal(String command) {
            String[] parts = command.split(" ");
            double amount = Double.parseDouble(parts[1]);
            NombreTiquets = Integer.parseInt(parts[2]);
            FilmId = Integer.parseInt(parts[3]);
            NombreDeTiquetEnfant= Integer.parseInt(parts[4]);
            NombreDeTiquetAdulte = Integer.parseInt(parts[5]);
            NombreDeTiquetSenior = Integer.parseInt(parts[6]);
            room = Integer.parseInt(parts[7]);
            hour = parts[8];


            try {

                // Send to MainTerminal
                for (ClientHandler client : clients) {
                    if (client != this) {
                        client.out.writeObject("SEND_PAYMENT " + amount);
                        client.out.flush();
                        System.out.println("Payment sent to MainTerminal: " + amount);
                        return; // Assuming only one MainTerminal client
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendPaymentToMain(String command) throws IOException, SQLException {

            System.out.println("send to main: " + command);
            String[] parts = command.split(" ");
            if (parts.length < 4) {
                System.out.println("Invalid response format: " + command);
                return;
            }
            String response = parts[1];  //
            System.out.println(response);
            double finalAmount = Double.parseDouble(parts[2]);  // Le montant final
            String code = parts[3];
            if(code!=null){
               if(comparePromoCode(code)){
                   finalAmount = finalAmount * 0.9;
               }
            }
            System.out.println("Nombre tiquet: " + NombreTiquets);
            System.out.println("FilmId: " + FilmId);
            if(response.equals("PaymentAccepted")){
                FilmDb filmDb = new FilmDb();
                Film film=filmDb.getFilmById(FilmId);
                createNewTiquet(FilmId, NombreTiquets,room,hour, finalAmount,NombreDeTiquetEnfant, NombreDeTiquetSenior, NombreDeTiquetAdulte, film.getTitre());
                updateTiquetsRestants(FilmId,room,NombreTiquets);
            }



            // Envoyer la réponse à tous les clients sauf à celui-ci
            for (ClientHandler client : clients) {
                if (client != this) {
                    client.out.writeObject(response + " " + finalAmount);
                    client.out.flush();
                    System.out.println("Payment response sent to a client: " + response + " " + finalAmount);
                }
            }
        }
        public static String readPromoCode() throws IOException {
            String filePath = "src/main/resources/com/helha/java/q2/cinephile/promotionCode.txt";
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            return content.toString();
        }

        public static boolean comparePromoCode(String codeEntered) {
            try {
                String fileContent = readPromoCode();
                String[] lines = fileContent.split("\n");

                for (String line : lines) {
                    if (line.trim().equals(codeEntered)) {
                        System.out.println("Promo code " + codeEntered + " found.");
                        return true;
                    }
                }
                return false;
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
            return false;
        }

        private static void createNewTiquet(int filmId, int nombreDeTiquet, int salle, String heure, double prix, int nombreDeTiquetEnfant, int nombreDeTiquetSenior, int nombreDeTiquetAdulte, String nomFilm) {
            TiquetDb tiquetDb = new TiquetDb();
            Tiquet newTiquet = new Tiquet();
            newTiquet.setFilmId(filmId);
            newTiquet.setNombreDeTiquet(nombreDeTiquet);
            newTiquet.setSalle(salle);
            newTiquet.setHeure(heure);
            newTiquet.setPrix(String.valueOf(prix));
            newTiquet.setNombreDeTiquetEnfant(nombreDeTiquetEnfant);
            newTiquet.setNombreDeTiquetSenior(nombreDeTiquetSenior);
            newTiquet.setNombreDeTiquetAdulte(nombreDeTiquetAdulte);
            newTiquet.setNomFilm(nomFilm);

            tiquetDb.insertTiquet(newTiquet);
            System.out.println("Nouveau tiquet créé avec succès.");
        }

        private static void updateTiquetsRestants(int filmId, int salle, int nombreDeTiquetAchetes) {
            FilmDb filmDb = new FilmDb();
            try {
                // Récupérer les informations actuelles sur les tiquets restants pour la salle choisie
                Film film = filmDb.getFilmById(filmId);

                // Mettre à jour les tiquets restants dans la salle choisie
                switch (salle) {
                    case 1:
                        System.out.println("salle: 1"+film.getTitre()+nombreDeTiquetAchetes);
                        film.setTiquetsRestantsSalle1(film.getTiquetsRestantsSalle1() - nombreDeTiquetAchetes);
                        break;
                    case 2:
                        System.out.println("salle: 2"+film.getTitre()+nombreDeTiquetAchetes);
                        film.setTiquetsRestantsSalle2(film.getTiquetsRestantsSalle2() - nombreDeTiquetAchetes);
                        break;
                    case 3:
                        System.out.println("salle: 3"+film.getTitre()+nombreDeTiquetAchetes);
                        film.setTiquetsRestantsSalle3(film.getTiquetsRestantsSalle3() - nombreDeTiquetAchetes);
                        break;
                    default:
                        System.out.println("Salle inconnue.");
                        return;
                }

                // Mettre à jour les informations dans la base de données
                filmDb.updateFilm(film);
                System.out.println("Mise à jour des tiquets restants pour la salle " + salle + " effectuée avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
