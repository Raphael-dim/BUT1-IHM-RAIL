package fr.umontpellier.iut.vues;

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
                    afficherCartes();
                });
            }
        };

        jeu.joueurCourantProperty().addListener(changeListener);
        
    }

    public void afficherCartes() {

        for (CouleurWagon carte: joueurCourant.getCartesWagon())
            {
                ImageView vueCarteWagon = new VueCarteWagon(carte).AfficherCarte();
                vueCarteWagon.setX(getChildren().size()* 65);
                vueCarteWagon.setId(carte+"");
                VueCarteWagon.texturer(vueCarteWagon);
                getChildren().add(vueCarteWagon);
            }
        ListChangeListener<CouleurWagon> changeListener = new ListChangeListener<CouleurWagon>() {

            @Override
            public void onChanged(Change<? extends CouleurWagon> arg0) {
                Platform.runLater(() -> {
                    while (arg0.next()) {
                        if (arg0.wasAdded()) {
                            for (CouleurWagon couleurWagon : arg0.getAddedSubList()) {
                                ImageView vueCarteWagon = new VueCarteWagon(couleurWagon).AfficherCarte();
                                vueCarteWagon.setId(couleurWagon + "");
                                vueCarteWagon.setX(getChildren().size()*65);
                                VueCarteWagon.texturer(vueCarteWagon);
                                getChildren().add(vueCarteWagon);
                            }
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
}
