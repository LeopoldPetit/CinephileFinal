package com.helha.java.q2.cinephile.Controllers;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Classe principale de l'application Cinephile.
 */
public class Main extends Application {

    /**
     * Méthode principale appelée au démarrage de l'application.
     *
     * @param primaryStage La scène principale de l'application.
     */


    /**
     * Méthode principale du programme.
     *
     * @param args Les arguments passés lors du lancement du programme.
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, URISyntaxException {
        FilmController filmController = new FilmController();
        filmController.start(primaryStage);
    }
}



