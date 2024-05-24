package com.helha.java.q2.cinephile.Views;

import com.helha.java.q2.cinephile.Models.Film;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ScheduleViewController {

    @FXML
    private AnchorPane SCREEN1;

    @FXML
    private AnchorPane SCREEN2;

    @FXML
    private AnchorPane SCREEN3;

    @FXML
    private ToggleButton room1Button;

    @FXML
    private ToggleButton room2Button;

    @FXML
    private ToggleButton room3Button;

    @FXML
    private Label moviesynopsis;

    @FXML
    private AnchorPane mainmoviespane;

    @FXML
    private Label movieduration;

    @FXML
    private ImageView movieimage;

    @FXML
    private Label movierating;

    @FXML
    private Label movierepeats;

    @FXML
    private Label movieticketsold;

    @FXML
    private Label movietitle;

    @FXML
    private ToggleButton otherschedulebtn;

    @FXML
    private AnchorPane tablepane;

    @FXML
    private AnchorPane toppane;

    @FXML
    private Film currentFilm; // Attribut pour stocker le film actuellement affiché

    private NavListener listener;

    private String selectedRoom;

    private String selectedHour;

    @FXML
    private Button buyticketbtn;

    @FXML
    private Label ticketsRoom1;

    @FXML
    private Label ticketsRoom2;

    @FXML
    private Label ticketsRoom3;



    /**
     * Initialise le contrôleur.
     */
    @FXML
    private void initialize() {
        buyticketbtn.setOnAction(event -> openCheckoutPage());
        room1Button.setOnAction(this::handleRoomButtonAction);
        room2Button.setOnAction(this::handleRoomButtonAction);
        room3Button.setOnAction(this::handleRoomButtonAction);
    }

    private void handleRoomButtonAction(ActionEvent event) {
        ToggleButton selectedButton = (ToggleButton) event.getSource();

        if (selectedButton.isSelected()) {
            // Mettre à jour la salle sélectionnée
            if (selectedButton == room1Button) {
                selectedRoom = "1";
                selectedHour = "13H";
                room2Button.setSelected(false);
                room3Button.setSelected(false);
            } else if (selectedButton == room2Button) {
                selectedRoom = "2";
                selectedHour = "17H";
                room1Button.setSelected(false);
                room3Button.setSelected(false);
            } else if (selectedButton == room3Button) {
                selectedRoom = "3";
                selectedHour = "19H";
                room1Button.setSelected(false);
                room2Button.setSelected(false);
            }
            // Mettre à jour le style des boutons
            room1Button.setStyle(selectedButton == room1Button ? "-fx-background-color: green; -fx-background-radius: 20; -fx-text-fill: white;" : "-fx-background-color: #aab0ad; -fx-background-radius: 20;");
            room2Button.setStyle(selectedButton == room2Button ? "-fx-background-color: green; -fx-background-radius: 20; -fx-text-fill: white;" : "-fx-background-color: #aab0ad; -fx-background-radius: 20;");
            room3Button.setStyle(selectedButton == room3Button ? "-fx-background-color: green; -fx-background-radius: 20; -fx-text-fill: white;" : "-fx-background-color: #aab0ad; -fx-background-radius: 20;");
        }
    }

    /**
     * Définit le film à afficher sur l'interface utilisateur.
     *
     * @param film Le film à afficher.
     */
    public void setFilm(Film film) {
        moviesynopsis.setText(film.getTexte());
        movietitle.setText(film.getTitre());
        movieduration.setText(film.getDuree());
        afficherTicketsRestants(
                film.getTiquetsRestantsSalle1(),
                film.getTiquetsRestantsSalle2(),
                film.getTiquetsRestantsSalle3()
        );
    }


    /**
     * Gère l'action de retour en arrière vers la vue précédente.
     */

    /**
     * Ouvre la page de paiement pour acheter des billets.
     */
    private void openCheckoutPage() {
        if (listener != null) {
            listener.openCheckoutPage(selectedRoom,selectedHour);
        }

    }

    public void setListener(NavListener listener) {
        this.listener = listener;
    }

    public interface NavListener {


        void openCheckoutPage(String selectedRoom, String selectedHour);
    }


    /**
     * Affiche le synopsis du film dans une boîte de dialogue.
     *
     *  L'événement de clic sur le bouton "View Movie Details".
     */

    @FXML
    // Assurez-vous que ce Label est défini avec @FXML si vous utilisez FXML



    private String formatTextWithLineBreaks(String text, int maxLineLength) {
        StringBuilder formatted = new StringBuilder();
        int lineLength = 0;

        for (String word : text.split(" ")) {
            if (lineLength + word.length() > maxLineLength) {
                formatted.append("\n");
                lineLength = 0;
            }
            formatted.append(word).append(" ");
            lineLength += word.length() + 1; // Adding 1 for the space
        }

        return formatted.toString().trim();
    }

    private void afficherTicketsRestants(int ticketsRoom1Count, int ticketsRoom2Count, int ticketsRoom3Count) {
        ticketsRoom1.setText("Tickets restants : " + ticketsRoom1Count);
        ticketsRoom2.setText("Tickets restants : " + ticketsRoom2Count);
        ticketsRoom3.setText("Tickets restants : " + ticketsRoom3Count);
    }
}


