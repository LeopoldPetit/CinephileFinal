package com.helha.java.q2.cinephile.Views;

import com.helha.java.q2.cinephile.Models.Film;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur pour la vue de l'horaire des films.
 */
public class ScheduleViewController {

    @FXML
    private AnchorPane SCREEN1;

    @FXML
    private AnchorPane SCREEN2;

    @FXML
    private AnchorPane SCREEN3;

    @FXML
    private Button buyticketbtn;

    @FXML
    private Button detailsbtn;

    @FXML
    private AnchorPane mainmoviespane;

    @FXML
    private Label movieduration;

    @FXML
    private ImageView movieimage;

    @FXML
    private AnchorPane moviepane;

    @FXML
    private Label movierating;

    @FXML
    private Label movierepeats;

    @FXML
    private Label movieticketsold;

    @FXML
    private Label movietitle;

    @FXML
    private Button otherschedulebtn;

    @FXML
    private AnchorPane tablepane;

    @FXML
    private AnchorPane toppane;

    private NavListener listener;



    /**
     * Initialise le contrôleur.
     */
    @FXML
    private void initialize() {
        buyticketbtn.setOnAction(event -> openCheckoutPage());
    }

    /**
     * Définit le film à afficher sur l'interface utilisateur.
     *
     * @param film Le film à afficher.
     */
    public void setFilm(Film film) {
        movietitle.setText(film.getTitre());
        movieduration.setText(film.getDuree());
    }

    /**
     * Gère l'action de retour en arrière vers la vue précédente.
     */





    /**
     * Ouvre la page de paiement pour acheter des billets.
     */
    private void openCheckoutPage() {
        if (listener != null){
            listener.openCheckoutPage();
        }

    }
    public void setListener(NavListener listener) {
        this.listener = listener;
    }
    public interface NavListener {
        void openCheckoutPage();
    }
}
