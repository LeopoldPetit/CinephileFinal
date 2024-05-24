package com.helha.java.q2.cinephile.Controllers;

import com.helha.java.q2.cinephile.Models.Film;
import com.helha.java.q2.cinephile.Models.FilmDb;
import com.helha.java.q2.cinephile.Models.Tiquet;
import com.helha.java.q2.cinephile.Models.TiquetDb;
import com.helha.java.q2.cinephile.Views.FilmViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.List;

public class FilmController {
    private static FilmViewController filmView;
    private Stage filmStage;
    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;


    public FilmController() {
        this.filmView = filmView;

    }
    public void start(Stage primaryStage) throws IOException, URISyntaxException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/helha/java/q2/cinephile/FilmView.fxml"));
        Parent root = loader.load();
        filmStage = primaryStage;
        filmView = loader.getController();
        filmView.setListener(new FilmViewController.goToScheduleListener() {
            @Override
            public void openSchedulePage(Film film) throws IOException, URISyntaxException {
                ScheduleController scheduleController = new ScheduleController();
                scheduleController.start(primaryStage, film);
            }
        });
        filmView.setFilmController(this); // Passer une instance de FilmController au FilmViewController

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Cinephile");
        primaryStage.setWidth(1000); // Largeur en pixels
        primaryStage.setHeight(700); // Hauteur en pixels
        primaryStage.show();
        connectToServer();
        loadFilms();
        loadTiquets();
    }
    static void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadFilms() {
        try {
            out.writeObject("GET_FILMS");
            out.flush();
            List<Film> films = (List<Film>) in.readObject();
            filmView.displayFilms(films);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadTiquets() {
        try {
            out.writeObject("GET_TIQUETS");
            out.flush();
            List<Tiquet> tiquets = null;
            tiquets = (List<Tiquet>) in.readObject();
            filmView.displayTiquets(tiquets);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}