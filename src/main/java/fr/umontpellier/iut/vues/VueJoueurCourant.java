package fr.umontpellier.iut.vues;

import java.util.List;

import fr.umontpellier.iut.IJeu;
import fr.umontpellier.iut.IJoueur;
import fr.umontpellier.iut.rails.CouleurWagon;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends Pane {

    private IJoueur joueurCourant;
    private IJeu jeu;

    public VueJoueurCourant() {

    }
    
    
    public void creerBindings() {
        jeu = ((VueDuJeu) getScene().getRoot()).getJeu();
        ChangeListener<IJoueur> changeListener = new ChangeListener<IJoueur>() {
            @Override
            public void changed(ObservableValue<? extends IJoueur> arg0, IJoueur arg1, IJoueur arg2) {
                Platform.runLater(() -> {   
                    getChildren().clear();
                    joueurCourant = arg2;
                    afficherJoueur();
                });
            }
        };

        jeu.joueurCourantProperty().addListener(changeListener);
        
    }

    public void afficherJoueur() {

        afficherCartes(joueurCourant.getCartesWagon());
        ListChangeListener<CouleurWagon> changeListener = new ListChangeListener<CouleurWagon>() {

            @Override
            public void onChanged(Change<? extends CouleurWagon> arg0) {
                Platform.runLater(() -> {
                    while (arg0.next()) {
                        if (arg0.wasAdded()) {
                            afficherCartes(arg0.getAddedSubList());
                        } else if (arg0.wasRemoved()) {
                            for (CouleurWagon couleurWagon : arg0.getRemoved()) {
                            }
                        }
                    }
                });
            }
        };
        joueurCourant.cartesWagonProperty().addListener(changeListener);
    }

    public void afficherCartes(List<? extends CouleurWagon> cartes) {

        for (CouleurWagon couleurWagon : cartes) 
            {
                ImageView vueCarteWagon = new VueCarteWagon(couleurWagon).AfficherCarte();
                vueCarteWagon.setId(couleurWagon + "");
                VueCarteWagon.texturer(vueCarteWagon);
                if (getChildren().size() >= 14) 
                    {
                        vueCarteWagon.setX((getChildren().size() % 14) * 65);
                        vueCarteWagon.setY(100);
                    } 
                else 
                    {
                        vueCarteWagon.setX(getChildren().size() * 65);
                    }
                getChildren().add(vueCarteWagon);
            }
    }
}
