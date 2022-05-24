package fr.umontpellier.iut.vues;

import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;


/**
 * Cette classe présente les routes et les villes sur le plateau.
 *
 * On y définit le listener à exécuter lorsque qu'un élément du plateau a été choisi par l'utilisateur
 * ainsi que les bindings qui mettront ?à jour le plateau après la prise d'une route ou d'une ville par un joueur
 */
public class VuePlateau extends Pane {

    @FXML Pane panneau;
    public VuePlateau() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/plateau_55.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            Pane p = loader.load();
            panneau = new Pane();
            panneau.getChildren().add(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @FXML
    public void choixRouteOuVille() {
    }
}
