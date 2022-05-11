package fr.umontpellier.iut.vues;
import java.util.List;

import fr.umontpellier.iut.IJoueur;
import fr.umontpellier.iut.rails.Joueur;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends VBox {

    public VueAutresJoueurs() {
        this.setPrefSize(200, 200);
    }

    public void creerBindings() {
        
        ChangeListener<IJoueur> changeListener = new ChangeListener<IJoueur>() {
            @Override
            public void changed(ObservableValue<? extends IJoueur> arg0, IJoueur arg1, IJoueur arg2) {
                Platform.runLater(() -> {
                    List<Joueur> Autresjoueurs = ((VueDuJeu) getScene().getRoot()).getJeu().getJoueurs();
                    Autresjoueurs.remove(((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().getValue());
                    if (getChildren().isEmpty())
                        {
                            for (Joueur j : Autresjoueurs) 
                                {
                                    getChildren().add(panneauJoueur(j));
                                }
                        }
                    System.out.println(arg0.getValue().getNom()+"test1");
                    System.out.println(arg1.getNom() + "test2");
                    System.out.println(arg2.getNom()+"tes3");
                    getChildren().add(panneauJoueur(arg1));
                    getChildren().remove(getPane(arg0.getValue().getNom()));
                });
            }
        };
        ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().addListener(changeListener);
    }

    public Pane panneauJoueur(IJoueur joueur) {
        Label label = new Label(joueur.getNom());
        Pane pane = new Pane(label);
        pane.setId(joueur.getNom());
        return pane;
    }

    public Pane getPane(String id) {
        for (Node pane : getChildren())
            {
                Pane l = (Pane) pane;
                if (pane.getId().equals(id))
                    {
                        return l;
                    }
            }
        return null;
    }
}
