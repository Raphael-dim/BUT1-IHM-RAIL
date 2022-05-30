package fr.umontpellier.iut.vues;

import java.util.List;

import fr.umontpellier.iut.IJeu;
import fr.umontpellier.iut.IJoueur;
import fr.umontpellier.iut.rails.CouleurWagon;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends Pane {

    private IJoueur joueurCourant;
    private IJeu jeu;
    private Pane main;

    public VueJoueurCourant() {
        main = new Pane();
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

        ImageView image = new ImageView("images/avatar-"+joueurCourant.getCouleur()+".png");
        image.setPreserveRatio(true);
        image.setFitHeight(200);
        image.setOpacity(50);
        image.setId("image");
        getChildren().add(image);

        HBox garesEtWagons = new HBox();
        garesEtWagons.setSpacing(20);
        garesEtWagons.setLayoutY(230);
        getChildren().add(garesEtWagons);

        for (int i = 0; i < joueurCourant.getNbGares(); i++) 
            {
                ImageView wagon = new ImageView("images/gares/gare-" + joueurCourant.getCouleur() + ".png");
                wagon.setPreserveRatio(true);
                wagon.setFitHeight(60);
                garesEtWagons.getChildren().add(wagon);
            }

        ImageView image_wagon = new ImageView("images/wagons/image-wagon-" + joueurCourant.getCouleur() + ".png");
        image_wagon.setPreserveRatio(true);
        image_wagon.setFitHeight(110);
        Label wagons = new Label("x " +joueurCourant.getNbWagons());
        wagons.setStyle("-fx-font-size: 30; -fx-text-fill: "+VueAutresJoueurs.traduire(joueurCourant.getCouleur().name())+"; -fx-stroke-color: black");

        garesEtWagons.getChildren().addAll(image_wagon, wagons);

        main.getChildren().clear();
        getChildren().add(main);
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
                                main.getChildren().remove(trouverImageView(couleurWagon+""));
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
            vueCarteWagon.setOnMouseClicked(e->jeu.uneCarteWagonAEteChoisie(couleurWagon));
            VueCarteWagon.texturer(vueCarteWagon);
            if (main.getChildren().size() >= 14) 
            {
                vueCarteWagon.setX(main.getChildren().size() % 14 * 65 + 175);
                vueCarteWagon.setY(100);
            } 
            else 
            {
                vueCarteWagon.setX(main.getChildren().size() * 65 + 175);
            }
                    main.getChildren().add(vueCarteWagon);
                }
    }

    public ImageView trouverImageView(String id) {
        for (Node image : main.getChildren()) {
            ImageView i = (ImageView) image;
            if (i.getId() != null && i.getId().equals(id)) {
                return i;
            }
        }
        return null;
    }
}
