package fr.umontpellier.iut.vues;

import fr.umontpellier.iut.IDestination;
import fr.umontpellier.iut.IJeu;
import fr.umontpellier.iut.rails.Destination;
import javafx.collections.ListChangeListener;
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
    private VueAutresJoueurs AutresJoueurs;
    private VBox destinations;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        plateau = new VuePlateau();
        AutresJoueurs = new VueAutresJoueurs();
        //getChildren().addAll(plateau, AutresJoueurs);
        this.setPrefSize(500, 500);
    }

    public IJeu getJeu() {
        return jeu;
    }

    public void creerBindings() {
        destinations = new VBox();
        ListChangeListener<Destination> changement = new ListChangeListener<Destination>() {
            @Override
            public void onChanged(Change<? extends Destination> arg0) {
                System.out.println(destinations.getChildren().size());
                System.out.println(destinations.getChildren() + "\n");
                while (arg0.next()) {
                    if (arg0.wasAdded()) {
                        for (Destination destination : arg0.getAddedSubList()) {
                            System.out.println(destination.getNom() + " a été ajouté");
                            destinations.getChildren().add(new Label(destination.getNom()));
                        }
                    } else if (arg0.wasRemoved()) {
                        for (Destination destination : arg0.getRemoved()) {
                            destinations.getChildren().remove(trouveLabelDestination(destination));
                            System.out.println(destination.getNom() + " a été supprimé");
                            System.out.println(destinations.getChildren().size());
                        }
                    } else if (arg0.wasUpdated()) {
                        //System.out.println(lesPersonnes.get(arg0.getFrom()).getNom() + " a désormais "+ lesPersonnes.get(arg0.getFrom()).getAge() + " ans");
                    }
                }
            }
        };
        
        jeu.destinationsInitialesProperty().addListener(changement);
        Button passer = new Button("Passer");
        passer.setOnMouseClicked(event -> jeu.passerAEteChoisi());
        this.getChildren().addAll(passer, destinations);
    }

    public Node trouveLabelDestination(IDestination destination) {
        for (Node label : destinations.getChildren())    
            {
                Label l = (Label) label;
                if (l.getText().equals(destination.getNom()))
                    {
                        System.out.println("SUPPRIME "+ destination.getNom());
                        return label;
                    }
            }
        return null;
    }
}