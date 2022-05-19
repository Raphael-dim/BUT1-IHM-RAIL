package fr.umontpellier.iut.vues;
import java.util.stream.IntStream;

import fr.umontpellier.iut.IDestination;
import fr.umontpellier.iut.IJeu;
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
    private HBox WagonsVisiblesPiocheDefausse;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        this.getStylesheets().add("css/page.css");
        this.setId("page");
        setHgap(25);
        setVgap(25);
        setPadding(new Insets(15));

        plateau = new VuePlateau();

        autresJoueurs = new VueAutresJoueurs();

        joueurCourant = new VueJoueurCourant();


        cartesWagonVisibles();
        piocheEtDefausse();
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

        Label instruction = new Label();
        instruction.setId("instruction");
        ChangeListener<String> changementInstruction = new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                instruction.setText(arg0.getValue());
            }
        };


        jeu.instructionProperty().addListener(changementInstruction);

        jeu.destinationsInitialesProperty().addListener(changementDestination);
        Button passer = new Button("Passer");
        passer.setId("passer");
        passer.setOnMouseClicked(event -> jeu.passerAEteChoisi());

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(70);
        getColumnConstraints().add(col1);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(30);
        getColumnConstraints().add(col2);
        

        add(instruction, 1, 3);
        add(plateau, 0, 0);
        add(autresJoueurs, 1, 0);
        add(WagonsVisiblesPiocheDefausse, 0, 1);
        
        add(passer, 1, 1);
        add(joueurCourant, 0, 2);
        add(destinations, 1, 2);

        autresJoueurs.creerBindings();
        joueurCourant.creerBindings();

    } 

    public void cartesWagonVisibles() {

        WagonsVisiblesPiocheDefausse = new HBox();
        WagonsVisiblesPiocheDefausse.setSpacing(20);
        HBox CartesWagonsVisibles = new HBox();
        WagonsVisiblesPiocheDefausse.getChildren().add(CartesWagonsVisibles);
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
                                CartesWagonsVisibles.getChildren().add(image);

                            }
                        } else if (arg0.wasRemoved()) {
                            if (arg0.getRemovedSize()>=5)
                                {
                                    CartesWagonsVisibles.getChildren().clear();
                                }
                            else{
                                for (CouleurWagon couleurWagon : arg0.getRemoved()) {
                                    CartesWagonsVisibles.getChildren().remove(trouverImageView(CartesWagonsVisibles, couleurWagon+""));
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
        
        ImageView pioche = new ImageView("images/wagons.png");
        pioche.setOnMouseClicked(e->jeu.uneCarteWagonAEtePiochee());
        VueCarteWagon.texturer(pioche);
        ImageView defausse = new ImageView("images/wagons.png");
        defausse.setPreserveRatio(true);
        defausse.setFitHeight(80);
        WagonsVisiblesPiocheDefausse.getChildren().addAll(pioche, defausse);
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