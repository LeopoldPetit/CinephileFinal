package com.helha.java.q2.cinephile.Controllers;

import com.helha.java.q2.cinephile.Models.Film;
import com.helha.java.q2.cinephile.Models.FilmDb;
import com.helha.java.q2.cinephile.Views.ScheduleViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class ScheduleController {
    private ScheduleViewController scheduleView;
    private FilmDb filmDb;
    private Stage filmStage;

    public ScheduleController() {

    }
    public void start(Stage primaryStage, Film film) throws IOException, URISyntaxException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/helha/java/q2/cinephile/SchedulePage.fxml"));
            Parent root = loader.load();
            // Créez une nouvelle scène avec la racine chargée
            Scene newScene = new Scene(root);

            // Créez un nouveau stage pour la nouvelle scène
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.setWidth(1150);
            newStage.setHeight(800);
            newStage.show();
            scheduleView = loader.getController();
            scheduleView.setListener(new ScheduleViewController.NavListener() {
                @Override
                public void openCheckoutPage(String selectedRoom,String selectedHour) {
                    CheckoutController.openCheckout(film, selectedRoom , selectedHour,newStage);
                    System.out.println(selectedRoom);
                    System.out.println(selectedHour);
                }
            });
            scheduleView.setFilm(film);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

