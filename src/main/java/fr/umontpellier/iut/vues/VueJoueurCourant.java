package fr.umontpellier.iut.vues;

import java.util.List;
import fr.umontpellier.iut.IJeu;
import fr.umontpellier.iut.IJoueur;
import fr.umontpellier.iut.rails.CouleurWagon;
import fr.umontpellier.iut.rails.Destination;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {

    private IJoueur joueurCourant;
    private IJeu jeu;
    private Pane main;
    private HBox garesEtWagons;
    private GridPane destinations;
    private double maxLongueur;
    
    public VueJoueurCourant() {
        main = new Pane();

        maxLongueur = 1920;
    }
    
    public void creerBindings() {

        this.getScene().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                Platform.runLater(() -> {
                    calculerTaille();
                });
            }
        });

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
        image.setFitHeight(150);
        image.setId("image");
        
        main.getChildren().clear();

        HBox logoEtMain = new HBox(image, main);
        getChildren().add(logoEtMain);

        afficherDestinations();
        afficherGaresEtWagons();
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

        calculerTaille();
    }

    public void calculerTaille(){
        double pourcentage = maxLongueur / getScene().getWidth(); 
        for (Node n : getChildren())
            {
                HBox hBox = (HBox) n;
                for (Node node : hBox.getChildren())
                    {
                        node.setScaleX(1 / pourcentage);
                        node.setScaleY(1 / pourcentage);
                    }
            }
    }

    public void afficherGaresEtWagons() {

        garesEtWagons = new HBox();
        garesEtWagons.setSpacing(10);
        for (int i = 0; i < joueurCourant.getNbGares(); i++) {
            ImageView gare = new ImageView("images/gares/gare-" + joueurCourant.getCouleur() + ".png");
            gare.setPreserveRatio(true);
            gare.setFitHeight(40);
            garesEtWagons.getChildren().add(gare);
        }

        ImageView image_wagon = new ImageView("images/wagons/image-wagon-" + joueurCourant.getCouleur() + ".png");
        image_wagon.setPreserveRatio(true);
        image_wagon.setFitHeight(75);
        Label wagons = new Label("x " + joueurCourant.getNbWagons());
        wagons.setMinWidth(15);
        wagons.setStyle("-fx-font-size: 30; -fx-text-fill: "
                + VueAutresJoueurs.traduire(joueurCourant.getCouleur().name()) + "; -fx-stroke-color: black");

        garesEtWagons.getChildren().addAll(image_wagon, wagons);
        HBox garesEtWagonsEtDestinations = new HBox(garesEtWagons, destinations);
        getChildren().add(garesEtWagonsEtDestinations);
    }

    public void afficherDestinations()  {
        
        destinations = new GridPane();
        destinations.setHgap(10);
        int ligne = 0;
        int colonne = 0;
        for (Destination d : joueurCourant.getDestinations())
            {
                Label l = new Label(d.getNom());
                l.setStyle("-fx-font-size: 30; -fx-text-fill: orange");
                destinations.add(l, colonne, ligne);
                ligne++;
                if (ligne >= 3)
                    {
                        ligne = 0;
                        colonne++;
                    }
            }
    }

    public void afficherCartes(List<? extends CouleurWagon> cartes) {

        for (CouleurWagon couleurWagon : cartes) 
        {
            ImageView vueCarteWagon = new VueCarteWagon(couleurWagon).AfficherCarte();
            VueCarteWagon.texturer(vueCarteWagon);
            vueCarteWagon.setId(couleurWagon + "");
            vueCarteWagon.setOnMouseClicked(e->jeu.uneCarteWagonAEteChoisie(couleurWagon));
            if (main.getChildren().size() >= 14) 
            {
                vueCarteWagon.setX(main.getChildren().size() % 14 * 65);
                vueCarteWagon.setY(100);
            } 
            else 
            {
                vueCarteWagon.setX(main.getChildren().size() * 65);
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
