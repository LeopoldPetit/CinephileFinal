package com.helha.java.q2.cinephile.Controllers;

import com.helha.java.q2.cinephile.Models.Film;
import com.helha.java.q2.cinephile.Models.FilmDb;
import com.helha.java.q2.cinephile.Views.FilmViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public class FilmController {
    private FilmViewController filmView;
    private FilmDb filmDb;
    private Stage filmStage;

    public FilmController() {
        this.filmDb = new FilmDb();
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
        loadFilms();
    }
    private void loadFilms() throws MalformedURLException, URISyntaxException {
        List<Film> films = filmDb.getAllFilms();
        filmView.displayFilms(films);
    }

}
