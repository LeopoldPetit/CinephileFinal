package com.helha.java.q2.cinephile.Controllers;

import com.helha.java.q2.cinephile.Views.BancontactViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class MainTerminal extends Application {

    private static BancontactViewController bancontactViewController;

    public static void main(String[] args) {
        // Lancez le serveur dans un thread séparé
        new Thread(MainTerminal::startServer).start();

        // Lancez l'application JavaFX
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/helha/java/q2/cinephile/bancontact.fxml"));
        Parent root = loader.load();
        bancontactViewController = loader.getController();
        bancontactViewController.setListener(new BancontactViewController.Listener() {
            @Override
            public void OnCodeEnter(String codeEntrer, Double prix) {
                double nouveauPrix ;
                compareLinesWithPromoCode(codeEntrer);
                if (compareLinesWithPromoCode(codeEntrer)){
                    nouveauPrix = prix * 0.9;
                    bancontactViewController.setMontant(nouveauPrix);
                }


            }
        });

        // Setup the stage
        primaryStage.setTitle("Bancontact Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    private static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Serveur démarré. En attente de connexions...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connecté depuis " + clientSocket.getInetAddress());

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String montantStr = reader.readLine();
                System.out.println(montantStr);
                Double montant = Double.parseDouble(montantStr);

                CountDownLatch latch = new CountDownLatch(1);

                if (bancontactViewController != null) {
                    bancontactViewController.setMontant(montant);
                    bancontactViewController.setClientSocket(clientSocket);
                    bancontactViewController.setLatch(latch);

                    // Attendre que la réponse soit reçue avant de fermer le socket
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("bancontactViewController est null.");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFileCode() throws IOException {
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

    public static boolean compareLinesWithPromoCode( String codeEntrer) {
        try {
            String fileContent = readFileCode();
            String[] lines = fileContent.split("\n");

            for (String line : lines) {
                if (line.trim().equals(codeEntrer)) {
                    System.out.println("Le nom " + codeEntrer + " a été trouvé dans le fichier.");
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
        return false;
    }
}
