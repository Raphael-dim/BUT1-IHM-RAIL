package fr.umontpellier.iut.vues;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends Pane {

    public VueAutresJoueurs() {
        this.setPrefSize(200, 200);
    }

    public void creerBindings() {
        getChildren().add(new Label());
        System.out.println(((VueDuJeu) getScene().getRoot()).getJeu().getJoueurs());
    }
}
