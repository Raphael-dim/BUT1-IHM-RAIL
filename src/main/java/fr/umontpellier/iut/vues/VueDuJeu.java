package fr.umontpellier.iut.vues;

import fr.umontpellier.iut.IDestination;
import fr.umontpellier.iut.IJeu;
import fr.umontpellier.iut.rails.Destination;
import fr.umontpellier.iut.rails.Joueur;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class VueDuJeu extends VBox {

    private IJeu jeu;
    private VuePlateau plateau;
    private VueAutresJoueurs autresJoueurs;
    private VueJoueurCourant joueurCourant;
    private VBox destinations;

    public VueDuJeu(IJeu jeu) {
        this.setPrefSize(900, 700);
        this.jeu = jeu;

        plateau = new VuePlateau();
        autresJoueurs = new VueAutresJoueurs();
        autresJoueurs.setTranslateX(600);
        joueurCourant = new VueJoueurCourant();

        //this.setStyle("-fx-background-color: #0000ff");
        getChildren().addAll(autresJoueurs, joueurCourant);
    }

    public IJeu getJeu() {
        return jeu;
    }

    public void creerBindings() {
        autresJoueurs.creerBindings();
        joueurCourant.creerBindings();

        destinations = new VBox();
        ListChangeListener<Destination> changement = new ListChangeListener<Destination>() {
            @Override
            public void onChanged(Change<? extends Destination> arg0) {
                Platform.runLater(() -> {
                while (arg0.next()) {
                    if (arg0.wasAdded()) {
                        for (Destination destination : arg0.getAddedSubList()) {
                            System.out.println(destination.getNom() + " a été ajouté");
                            destinations.getChildren().add(new Label(destination.getNom()));
                        }
                    } 
                    else if (arg0.wasRemoved()) {
                        for (Destination destination : arg0.getRemoved()) {
                            destinations.getChildren().remove(trouveLabelDestination(destination));
                            System.out.println(destination.getNom() + " a été supprimé");
                        }
                    } 
                }
            });
        };
        };
        jeu.destinationsInitialesProperty().addListener(changement);
        Button passer = new Button("Passer");
        passer.setOnMouseClicked(event -> jeu.passerAEteChoisi());
        this.getChildren().addAll(passer, destinations);
    }

    public Label trouveLabelDestination(IDestination destination) {
        for (Node label : destinations.getChildren())    
            {
                Label l = (Label) label;
                if (l.getText().equals(destination.getNom()))
                    {
                        return l;
                    }
            }
        return null;
    }
}