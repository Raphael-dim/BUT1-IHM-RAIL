package fr.umontpellier.iut.vues;
import fr.umontpellier.iut.IDestination;
import fr.umontpellier.iut.IJeu;
import fr.umontpellier.iut.rails.CouleurWagon;
import fr.umontpellier.iut.rails.Destination;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
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
public class VueDuJeu extends HBox {

    private IJeu jeu;
    private VuePlateau plateau;
    private VueAutresJoueurs autresJoueurs;
    private VueJoueurCourant joueurCourant;
    private VBox destinations;
    private HBox cartesWagonVisibles;
    private HBox piocheEtDefausse;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        this.setStyle("-fx-background-color: grey;");

        plateau = new VuePlateau();

        autresJoueurs = new VueAutresJoueurs();

        joueurCourant = new VueJoueurCourant();

        //this.setStyle("-fx-background-color: #0000ff");
        //getChildren().add(plateau);
        cartesWagonVisibles();
        piocheEtDefausse();
        
        piocheEtDefausse.setStyle("-fx-background-color: blue;");
        cartesWagonVisibles.setStyle("-fx-background-color: lightgreen;");
        autresJoueurs.setStyle("-fx-background-color: red;");
        joueurCourant.setStyle("-fx-background-color: pink;");

    }

    public IJeu getJeu() {
        return jeu;
    }

    public void creerBindings() {

        destinations = new VBox();
        destinations.setStyle("-fx-background-color: black;");
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
        passer.setTranslateX(this.getTranslateX());
        passer.setTranslateY(20);
        passer.setOnMouseClicked(event -> jeu.passerAEteChoisi());
        this.getChildren().addAll(passer, destinations);

        getChildren().addAll(autresJoueurs, cartesWagonVisibles, piocheEtDefausse);
        getChildren().add(joueurCourant);
        autresJoueurs.creerBindings();
        joueurCourant.setTranslateY(joueurCourant.getTranslateY()+20);
        joueurCourant.setTranslateX(0);
        joueurCourant.creerBindings();
    }

    public void cartesWagonVisibles() {

        HBox cartesWagonVisibles = new HBox();
        this.cartesWagonVisibles=cartesWagonVisibles;
        cartesWagonVisibles.setTranslateY(600);
        cartesWagonVisibles.setMaxHeight(50);
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
                                cartesWagonVisibles.getChildren().add(image);
                            }
                        } else if (arg0.wasRemoved()) {
                            for (CouleurWagon couleurWagon : arg0.getRemoved()) {
                                cartesWagonVisibles.getChildren().remove(trouverImageView(couleurWagon+""));
                            }
                        }
                    }
                });   
            }
        };
        jeu.cartesWagonVisiblesProperty().addListener(changement);
        this.cartesWagonVisibles=cartesWagonVisibles;
    }

    public void piocheEtDefausse() {
        
        HBox piocheEtDefausse = new HBox(10);
        piocheEtDefausse.setMaxHeight(80);

        piocheEtDefausse.setTranslateX(piocheEtDefausse.getTranslateX()+10);
        piocheEtDefausse.setTranslateY(600);
        ImageView pioche = new ImageView("images/wagons.png");
        pioche.setOnMouseClicked(e->jeu.uneCarteWagonAEtePiochee());
        VueCarteWagon.texturer(pioche);
        ImageView defausse = new ImageView("images/wagons.png");
        defausse.setPreserveRatio(true);
        defausse.setFitHeight(80);

        piocheEtDefausse.getChildren().addAll(pioche, defausse);
        this.piocheEtDefausse=piocheEtDefausse;
    }

    public ImageView trouverImageView(String id) {
        for (Node image : cartesWagonVisibles.getChildren())
            {
                ImageView i = (ImageView) image;
                if (i.getId().equals(id))
                    {
                        return i;
                    }
            }
        return null;
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