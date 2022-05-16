package fr.umontpellier.iut.vues;

import fr.umontpellier.iut.IJoueur;
import fr.umontpellier.iut.rails.CouleurWagon;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.DepthTest;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends Pane {

    private IJoueur joueurCourant;

    public VueJoueurCourant() {
        this.setPrefSize(200, 200);

    }
    
    
    public void creerBindings() {
        ChangeListener<IJoueur> changeListener = new ChangeListener<IJoueur>() {
            @Override
            public void changed(ObservableValue<? extends IJoueur> arg0, IJoueur arg1, IJoueur arg2) {
                Platform.runLater(() -> {   
                    joueurCourant= ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().getValue();
                    joueurCourant = arg0.getValue();
                    AfficherCartes();
                });
            }
        };

        ((VueDuJeu) getScene().getRoot()).getJeu().joueurCourantProperty().addListener(changeListener);
        
    }

    public void AfficherCartes() {

        int x =0;
        for (CouleurWagon carte: joueurCourant.getCartesWagon())
            {
                ImageView vueCarteWagon = new VueCarteWagon(carte).AfficherCarte();
                vueCarteWagon.setId(carte+"");
                vueCarteWagon.setPreserveRatio(true);
                vueCarteWagon.setFitHeight(100);
                //vueCarteWagon.setFitWidth(150);
                vueCarteWagon.setX(x);

                vueCarteWagon.setOnMouseEntered(e-> {
                    vueCarteWagon.setFitHeight(120);
                    //vueCarteWagon.toFront();
                    vueCarteWagon.setEffect(new Glow(0.3)); 
                    vueCarteWagon.setEffect(new DropShadow(20, Color.BLACK));
                });
                vueCarteWagon.setOnMouseExited(e-> {

                    vueCarteWagon.setEffect(null);
                    vueCarteWagon.setFitHeight(100);
                    //vueCarteWagon.toBack();
                    
                });
                getChildren().add(vueCarteWagon);
                x+=40;
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
