package fr.umontpellier.iut.vues;
import java.util.stream.IntStream;

import fr.umontpellier.iut.IDestination;
import fr.umontpellier.iut.IJeu;
import fr.umontpellier.iut.IJoueur.Couleur;
import fr.umontpellier.iut.rails.CouleurWagon;
import fr.umontpellier.iut.rails.Destination;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 *
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, les 5 cartes Wagons visibles, les destinations lors de l'étape d'initialisation de la partie, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends GridPane {

    private IJeu jeu;
    private VuePlateau plateau;
    private VueAutresJoueurs autresJoueurs;
    private VueJoueurCourant joueurCourant;
    private HBox destinations;
    private HBox wagonsVisibles;
    private HBox piocheDefausse;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        this.getStylesheets().add("css/page.css");
        this.setId("page");
        setHgap(30);
        setVgap(15);
        setPadding(new Insets(15));

        plateau = new VuePlateau();

        autresJoueurs = new VueAutresJoueurs();

        joueurCourant = new VueJoueurCourant();


        cartesWagonVisibles();
        piocheEtDefausse();
        destinations();
        /*
        WagonsVisiblesPiocheDefausse.setStyle("-fx-background-color: lightgreen;");
        autresJoueurs.setStyle("-fx-background-color: red;");
        joueurCourant.setStyle("-fx-background-color: pink;");
        */
    }

    public IJeu getJeu() {
        return jeu;
    }

    public void creerBindings() {

        destinations = new HBox();
        destinations.setSpacing(10);
        ListChangeListener<Destination> changementDestination = new ListChangeListener<Destination>() {
            @Override
            public void onChanged(Change<? extends Destination> arg0) {
                Platform.runLater(() -> {
                while (arg0.next()) {
                    if (arg0.wasAdded()) {
                        for (Destination destination : arg0.getAddedSubList()) {
                            Button boutton = new Button(destination.getNom());
                            boutton.setOnMouseClicked(e->{
                                jeu.uneDestinationAEteChoisie(boutton.getText());
                            });
                            destinations.getChildren().add(boutton);
                        }
                    } 
                    else if (arg0.wasRemoved()) {
                        for (Destination destination : arg0.getRemoved()) {
                            destinations.getChildren().remove(trouverDestination(destination));
                        }
                    } 
                }
                });
            };
        };
        jeu.destinationsInitialesProperty().addListener(changementDestination);

        Label instruction = new Label();
        instruction.setId("instruction");
        ChangeListener<String> changementInstruction = new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                instruction.setText(arg0.getValue());
            }
        };
        jeu.instructionProperty().addListener(changementInstruction);


        
        /*
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(65);
        getColumnConstraints().add(col1);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(3);
        getColumnConstraints().add(col2);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(30);
        getColumnConstraints().add(col3);
        */

        
        Button passer = new Button("Passer");
        passer.setId("passer");
        passer.setOnMouseClicked(event -> jeu.passerAEteChoisi());

        VBox choix = new VBox();
        choix.setSpacing(20);
        choix.getChildren().addAll(instruction, passer, destinations);
        
        VBox autresJoueursEtPioches = new VBox(autresJoueurs);
        autresJoueursEtPioches.getChildren().addAll(piocheDefausse, wagonsVisibles);
        autresJoueursEtPioches.setSpacing(70);

        add(plateau, 0, 0);
        add(autresJoueursEtPioches, 1, 0);
        add(joueurCourant, 0, 1);
        add(choix, 1, 1);

        autresJoueurs.creerBindings();
        joueurCourant.creerBindings();

    } 

    public void cartesWagonVisibles() {

        wagonsVisibles = new HBox();
        wagonsVisibles.setSpacing(5);
        ListChangeListener<CouleurWagon> changement = new ListChangeListener<CouleurWagon>() {

            @Override
            public void onChanged(Change<? extends CouleurWagon> arg0) {
                Platform.runLater(() -> {
                    while (arg0.next()) {
                        if (arg0.wasAdded()) {
                            for (CouleurWagon couleurWagon : arg0.getAddedSubList()) {
                                ImageView image = new VueCarteWagon(couleurWagon).AfficherCarte();
                                VueCarteWagon.texturer(image);
                                image.setId(couleurWagon + "");
                                image.setOnMouseClicked(e->jeu.uneCarteWagonAEteChoisie(couleurWagon));
                                wagonsVisibles.getChildren().add(image);

                            }
                        } else if (arg0.wasRemoved()) {
                            if (arg0.getRemovedSize()>=5)
                                {
                                    wagonsVisibles.getChildren().clear();
                                }
                            else{
                                for (CouleurWagon couleurWagon : arg0.getRemoved()) {
                                    wagonsVisibles.getChildren().remove(trouverImageView(wagonsVisibles, couleurWagon+""));
                                }
                            }
                        }
                    }
                });   
            }
        };
        jeu.cartesWagonVisiblesProperty().addListener(changement);
    }

    public void piocheEtDefausse() {
        
        piocheDefausse = new HBox();
        piocheDefausse.setSpacing(20);
        ImageView pioche = new ImageView("images/wagons.png");
        pioche.setOnMouseClicked(e->jeu.uneCarteWagonAEtePiochee());
        VueCarteWagon.texturer(pioche);
        ImageView defausse = new ImageView("images/wagons.png");
        defausse.setPreserveRatio(true);
        defausse.setFitHeight(80);
        piocheDefausse.getChildren().addAll(pioche, defausse);
    }

    public void destinations() {

        ImageView pioche = new ImageView("images/destinations.png");
        VueCarteWagon.texturer(pioche);
        pioche.setOnMouseClicked(e->jeu.uneDestinationAEtePiochee());
        piocheDefausse.getChildren().addAll(pioche);
    }

    public ImageView trouverImageView(HBox CartesWagonsVisibles, String id) {
        for (Node image : CartesWagonsVisibles.getChildren())
            {
                ImageView i = (ImageView) image;
                if (i.getId().equals(id))
                    {
                        return i;
                    }
            }
        return null;
    }

    public Button trouverDestination(IDestination destination) {
        for (Node boutton : destinations.getChildren())    
            {
                Button b = (Button) boutton;
                if (b.getText().equals(destination.getNom()))
                    {
                        return b;
                    }
            }
        return null;
    }
}