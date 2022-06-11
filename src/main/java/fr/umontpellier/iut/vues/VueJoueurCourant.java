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
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
    private Label nom;
    private double maxLongueur;
    
    public VueJoueurCourant() {
        main = new Pane();
        getStylesheets().add("css/page.css");
        //setSpacing(3);
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
        logoEtMain.setSpacing(20);
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
        /*
         
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
            //setSpacing(30 / pourcentage);
            */
    }

    public void afficherGaresEtWagons() {

        garesEtWagons = new HBox();
        VBox nomsEtAutre = new VBox();
        nomsEtAutre.setSpacing(4);
        nom = new Label(joueurCourant.getNom());
        nom.setTranslateX(20);
        nom.setStyle("-fx-font-family: \"IM FELL English SC\";-fx-font-size: 45; -fx-text-fill: "
                + VueAutresJoueurs.traduire(joueurCourant.getCouleur().name()) + "; -fx-stroke-color: black");

        nomsEtAutre.getChildren().add(nom);
        nomsEtAutre.getChildren().add(garesEtWagons);
        garesEtWagons.setSpacing(10);
        for (int i = 0; i < joueurCourant.getNbGares(); i++) {
            ImageView gare = new ImageView("images/gares/gare-" + joueurCourant.getCouleur() + ".png");
            gare.setPreserveRatio(true);
            gare.setFitHeight(40);
            garesEtWagons.getChildren().add(gare);
        }

        ImageView image_wagon = new ImageView("images/wagons/image-wagon-" + joueurCourant.getCouleur() + ".png");
        image_wagon.setPreserveRatio(true);
        image_wagon.setFitHeight(45);

        Label wagons = new Label("x " + joueurCourant.getNbWagons());
        wagons.setMinWidth(7);
        wagons.setStyle("-fx-font-family: \"IM FELL English SC\";-fx-font-size: 35; -fx-text-fill: "
        + VueAutresJoueurs.traduire(joueurCourant.getCouleur().name()) + "; -fx-stroke-color: black");
        
        image_wagon.setOnMouseEntered(e -> {
            wagons.setMinWidth(80);
        });

        image_wagon.setOnMouseExited(e -> {
            wagons.setMinWidth(0);
        });

        garesEtWagons.getChildren().addAll(image_wagon, wagons);
        HBox garesEtWagonsEtDestinations = new HBox(nomsEtAutre, destinations);

        destinations.setOnMouseEntered(e -> {
            destinations.setTranslateY(40);
            destinations.setMinWidth(getWidth());
            destinations.setMinHeight(garesEtWagonsEtDestinations.getHeight());
            garesEtWagonsEtDestinations.getChildren().remove(nomsEtAutre);
        });
        destinations.setOnMouseExited(e -> {
            destinations.setMinWidth(0);
            garesEtWagonsEtDestinations.getChildren().clear();
            garesEtWagonsEtDestinations.getChildren().addAll(nomsEtAutre, destinations);
            destinations.setTranslateY(40);

        });

        garesEtWagons.setLayoutY(30);
        garesEtWagonsEtDestinations.setSpacing(15);
        getChildren().add(garesEtWagonsEtDestinations);
    }

    public void afficherDestinations()  {
        

        destinations = new GridPane();
        destinations.setPadding(new Insets(0, 0, 0, 10));
        destinations.setTranslateY(40);
        destinations.setHgap(10);
        int ligne = 0;
        int colonne = 0;
        for (Destination d : joueurCourant.getDestinations())
            {
                Label l = new Label(d.getNom());
                l.setStyle("-fx-font-size: 25; -fx-font-family: \"IM FELL English SC\";-fx-font-size: 29; -fx-text-fill: orange");
                destinations.add(l, colonne, ligne);
                ligne++;
                if (ligne >= 2)
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
            /*
             
            vueCarteWagon.setOnMouseEntered(e->{
                vueCarteWagon.setScaleX(1.25);
                vueCarteWagon.setScaleY(1.25);
                // carte.toFront();
                vueCarteWagon.setEffect(new Glow(0.3));
                vueCarteWagon.setEffect(new DropShadow(20, Color.BLACK));
                vueCarteWagon.toFront();
            });
            */
            if (main.getChildren().size() >= 13) 
            {
                vueCarteWagon.setX(main.getChildren().size() % 13 * 65);
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
