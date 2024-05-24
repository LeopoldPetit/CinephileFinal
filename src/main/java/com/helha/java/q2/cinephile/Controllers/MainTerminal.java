package com.helha.java.q2.cinephile.Controllers;

import com.helha.java.q2.cinephile.Views.BancontactViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainTerminal extends Application {

    private static BancontactViewController bancontactViewController;
    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static double amountToPay;
    private String code;
    private static Stage stage;


    public static void main(String[] args) {
        // Connect to the central server
        new Thread(MainTerminal::connectToServer).start();

        // Launch the JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/helha/java/q2/cinephile/bancontact.fxml"));
        Parent root = loader.load();
        stage=primaryStage;
        bancontactViewController = loader.getController();
        bancontactViewController.setListener((codeEntered) -> {
            code = codeEntered;
        });
        bancontactViewController.setButtonListener(new BancontactViewController.Blistener() {
            @Override
            public void OnAccepted(Double price) {
                System.out.println("Accepted");
                sendPaymentResponse("PaymentAccepted", price, code);
            }

            @Override
            public void OnRejected(Double price) {
                sendPaymentResponse("PaymentRejected", price, code);
            }
        });

        primaryStage.setTitle("Bancontact Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private static void connectToServer() {
        String serverAddress = "127.0.0.1"; // Server IP address (localhost)
        int serverPort = 12345; // Server port

        try {
            socket = new Socket(serverAddress, serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("Connected to the central server at " + serverAddress + ":" + serverPort);

            // Keep reading responses from the server
            while (true) {
                Object response = in.readObject();
                if (response instanceof String) {
                    String command = (String) response;
                    if (command.startsWith("SEND_PAYMENT")) {
                        double finalAmount = Double.parseDouble(command.split(" ")[1]);
                        System.out.println("Payment accepted. Final amount: " + finalAmount);
                        Platform.runLater(() -> {
                            notifyPrimaryStage();

                            bancontactViewController.setMontant(finalAmount);
                        });
                        amountToPay = finalAmount;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static void notifyPrimaryStage() {
        // Bring primary stage to front and request focus
        stage.setAlwaysOnTop(true);  // Force it to the front
        stage.toFront();
        stage.requestFocus();
        stage.setAlwaysOnTop(false);  // Reset the always-on-top status
    }
    private static void sendPrimaryStageToBack() {
        // Send primary stage to back
        stage.setAlwaysOnTop(false); // Ensure it is not always on top
        stage.toBack(); // Send it to the back
    }


    private static void sendPaymentResponse(String response, Double finalAmount, String code) {
        sendPrimaryStageToBack();
        try {
            System.out.println("Sending payment response: " + response );
            out.writeObject("RESEND_PAYMENTRESPONSE " +response + " " + finalAmount + " "+ code);
            out.flush();
            bancontactViewController.reSetMontant();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
