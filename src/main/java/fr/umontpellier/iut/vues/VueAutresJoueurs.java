package fr.umontpellier.iut.vues;
import java.util.List;

import fr.umontpellier.iut.IJoueur;
import fr.umontpellier.iut.IJoueur.Couleur;
import fr.umontpellier.iut.rails.CouleurWagon;
import fr.umontpellier.iut.rails.Joueur;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
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
                    if (getChildren().isEmpty())
                        {
                            List<Joueur> Autresjoueurs = ((VueDuJeu) getScene().getRoot()).getJeu().getJoueurs();
                            Autresjoueurs.remove(((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().getValue());
                            for (Joueur j : Autresjoueurs) 
                                {
                                    getChildren().add(panneauJoueur(j));
                                }
                        }
                    else    
                        {
                            System.out.println(arg1.getNom());
                            System.out.println(arg0.getValue().getNom());
                            getChildren().add(panneauJoueur(arg1));
                            getChildren().remove(getPane(arg0.getValue().getNom()));
                        }
                });
            }
        };
        ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().addListener(changeListener);
    }

    public Pane panneauJoueur(IJoueur joueur) {
        Label label = new Label(joueur.getNom());
        Pane pane = new Pane(label);
        ListChangeListener<CouleurWagon> changeListener = new ListChangeListener<CouleurWagon>() {
            @Override
            public void onChanged(Change<? extends CouleurWagon> arg0) {
                Platform.runLater(() -> {
                    while (arg0.next()) {
                        if (arg0.wasAdded()) {
                            for (CouleurWagon couleurWagon : arg0.getAddedSubList()) {
                                pane.getChildren().add(new VueCarteWagon(couleurWagon).AfficherCarte());
                            }
                        } else if (arg0.wasRemoved()) {
                            for (CouleurWagon couleurWagon : arg0.getRemoved()) {
                            }
                        }
                    }
                });
                
            }
            
        };
        joueur.cartesWagonProperty().addListener(changeListener);
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
