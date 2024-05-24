package com.helha.java.q2.cinephile.Controllers;

import com.helha.java.q2.cinephile.Models.Film;
import com.helha.java.q2.cinephile.Models.FilmDb;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CentralServer {
    private static final int PORT = 12345;
    private static List<ClientHandler> clients = new CopyOnWriteArrayList<>();

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
                    if (message instanceof String) {
                        String command = (String) message;
                        if (command.equals("GET_FILMS")) {
                            sendFilms();
                        } else if (command.startsWith("SEND_PAYMENT")) {
                            sendPaymentToTerminal(command);
                            System.out.println("reponse envoyer au terminal");
                        } else if (command.startsWith("RESEND_PAYMENTRESPONSE")) {
                            sendPaymentToMain(command);
                            System.out.println("reponse envoyer au main");

                        }
                    }
                }
            } catch (EOFException e) {
                System.out.println("Client disconnected.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
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

        private void sendPaymentToTerminal(String command) {
            String[] parts = command.split(" ");
            double amount = Double.parseDouble(parts[1]);

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

        private void sendPaymentToMain(String command) throws IOException {

            System.out.println("send to main: " + command);
            String[] parts = command.split(" ");
            if (parts.length < 4) {
                System.out.println("Invalid response format: " + command);
                return;
            }
            String response = parts[1];  // "PAYMENTACCEPTED" ou "PAYMENTREJECTED"
            System.out.println(response);
            double finalAmount = Double.parseDouble(parts[2]);  // Le montant final
            String code = parts[3];
            if(code!=null){
               if(comparePromoCode(code)){
                   finalAmount = finalAmount * 0.9;
               }
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
            try (BufferedReader reader = new BufferedReader(new java.io.FileReader(filePath))) {
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
    }
}
