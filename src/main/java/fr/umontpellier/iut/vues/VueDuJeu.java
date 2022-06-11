package fr.umontpellier.iut.vues;
import java.util.Timer;
import java.util.TimerTask;

import fr.umontpellier.iut.IDestination;
import fr.umontpellier.iut.IJeu;
import fr.umontpellier.iut.IJoueur;
import fr.umontpellier.iut.rails.CouleurWagon;
import fr.umontpellier.iut.rails.Destination;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

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
    private VBox choix;
    private GridPane destinations;
    private HBox wagonsVisibles;
    private HBox piocheDefausse;
    private double maxLongueur;

    public VueDuJeu(IJeu jeu) {

        this.jeu = jeu;
        this.getStylesheets().add("css/page.css");
        this.setId("page");

        Image image = new Image("images/fond.png");

        BackgroundImage img = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
                
        setBackground(new Background(img));

        setHgap(15);
        setVgap(15);
      
        maxLongueur = 1920;
        
        setPadding(new Insets(15));

        plateau = new VuePlateau();

        autresJoueurs = new VueAutresJoueurs();

        joueurCourant = new VueJoueurCourant();
        
        cartesWagonVisibles();
        piocheEtDefausse();
        destinations();
        choixEtInstructions();
        
        /*
        autresJoueurs.setStyle("-fx-background-color: red;");
        joueurCourant.setStyle("-fx-background-color: pink;");
        */
    }

    public IJeu getJeu() {
        return jeu;
    }

    public void creerBindings() {
        
        /*
         * 
         ColumnConstraints col1 = new ColumnConstraints();
         col1.setPercentWidth(64);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(36);
        
        RowConstraints lig1 = new RowConstraints();
        lig1.setPercentHeight(75);
        RowConstraints lig2 = new RowConstraints();
        lig2.setPercentHeight(25);
        
        getRowConstraints().add(lig1);
        getRowConstraints().add(lig2);
        
        getColumnConstraints().add(col1);
        getColumnConstraints().add(col2);
        
        */
        this.getScene().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                Platform.runLater(() -> {
                    for (Node n : wagonsVisibles.getChildren()) {
                        ((ImageView) n).setFitWidth(VueCarteWagon.getLongueur() * (getWidth() / maxLongueur));
                    }
                    for (Node n : piocheDefausse.getChildren()) {
                        ((ImageView) n).setFitWidth(VueCarteWagon.getLongueur() * (getWidth() / maxLongueur));
                    }
                });  
            }    
        });
        
        VBox autresJoueursEtPioches = new VBox(autresJoueurs);
        autresJoueursEtPioches.getChildren().addAll(piocheDefausse, wagonsVisibles);
        autresJoueursEtPioches.setSpacing(70);
        autresJoueursEtPioches.setPadding(new Insets(25));

        plateau.setId("plateau");
        autresJoueursEtPioches.setId("autresJoueursEtPioches");
        joueurCourant.setId("joueurCourant");
        choix.setId("choix");
        add(plateau, 0, 0);
        add(autresJoueursEtPioches, 1, 0);
        add(joueurCourant, 0, 1);
        add(choix, 1, 1);

        FadeTransition fondu = new FadeTransition();
        fondu.setDuration(new Duration(1100));
        fondu.setFromValue(0);
        fondu.setToValue(1);
        fondu.setNode(this);
        fondu.play();

        plateau.creerBindings();
        autresJoueurs.creerBindings();
        joueurCourant.creerBindings();
    } 

    public static Color getCouleur(String couleur)   {
        switch (couleur) {
            case "ROSE":
                return Color.PINK;
            case "BLEU":
                return Color.BLUE;
            case "ROUGE":
                return Color.RED;

            case "VERT":
                return Color.GREEN;

            case "VIOLET":
                return Color.PURPLE;

            case "JAUNE":
                return Color.YELLOW;
        }
        return Color.BLACK;
    }

    public void choixEtInstructions()   {

        Label instruction = new Label();
        instruction.setStyle("-fx-font-size: 35; -fx-font-family: \"IM FELL English SC\";");
        Line line1 = new Line();
        Line line2 = new Line();
        line1.startXProperty().bind(instruction.layoutXProperty());
        line1.endXProperty().bind(instruction.widthProperty());
        line2.startXProperty().bind(instruction.layoutXProperty());
        line2.endXProperty().bind(instruction.widthProperty());
        line1.setStrokeWidth(4);
        line2.setStrokeWidth(4);
        line1.setStrokeLineCap(StrokeLineCap.ROUND);
        line2.setStrokeLineCap(StrokeLineCap.ROUND);
        
        ChangeListener<IJoueur> changementJoueur = new ChangeListener<IJoueur>() {
            @Override
            public void changed(ObservableValue<? extends IJoueur> arg0, IJoueur arg1, IJoueur arg2) {
                Platform.runLater(() -> {
                    instruction.setStyle("-fx-underline: true; -fx-font-family: \"IM FELL English SC\"; -fx-font-weight: bold; -fx-font-size: 35; -fx-text-fill: "+VueAutresJoueurs.traduire(jeu.joueurCourantProperty().getValue().getCouleur()+""));
    
                    line1.setStroke(getCouleur(jeu.joueurCourantProperty().getValue().getCouleur() + ""));
                    line2.setStroke(getCouleur(jeu.joueurCourantProperty().getValue().getCouleur()+""));
                });
            }
        };
        
        jeu.joueurCourantProperty().addListener(changementJoueur);
        
        
        ChangeListener<String> changementInstruction = new ChangeListener<String>() {
            
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                Platform.runLater(() -> {
                    instruction.setStyle("-fx-underline: true; -fx-font-family: \"IM FELL English SC\"; -fx-font-weight: bold; -fx-font-size: 35; -fx-text-fill: "+VueAutresJoueurs.traduire(jeu.joueurCourantProperty().getValue().getCouleur()+""));
                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        
                        @Override
                        public void run() {
                            instruction.setStyle("-fx-font-family: \"IM FELL English SC\"; -fx-font-size: 30; -fx-text-fill: "+VueAutresJoueurs.traduire(jeu.joueurCourantProperty().getValue().getCouleur()+""));
                        }
                        
                    };
                    timer.schedule(timerTask, 2000);
                    instruction.setText(arg2);
                });
            }
        };

        jeu.instructionProperty().addListener(changementInstruction);
        Button passer = new Button("Passer");
        passer.addEventFilter(KeyEvent.KEY_PRESSED, ev->{
            if (ev.getCode() == KeyCode.ENTER)
                {
                    jeu.passerAEteChoisi();
                    ev.consume();
                }
        });
        passer.setStyle("-fx-text-fill: white; -fx-font-family: \"IM FELL English SC\";-fx-font-size: 25");
        passer.setId("passer");
        passer.setOnMouseClicked(event -> jeu.passerAEteChoisi());

        passer.setOnMouseEntered(e -> {
            passer.setStyle(
                    "-fx-font-family: \"IM FELL English SC\";-fx-font-size: 30; -fx-text-fill: white; -fx-border-color: white;");
            passer.setEffect(new Glow(0.3));
            passer.setEffect(new DropShadow(20, Color.BLACK));
        });
        passer.setOnMouseExited(e -> {
            passer.setStyle("-fx-font-family: \"IM FELL English SC\";-fx-font-size: 25");
            passer.setEffect(null);

        });

        choix = new VBox();
        choix.setSpacing(15);
        choix.getChildren().addAll(line1, instruction, line2, passer, destinations);
    }

    public void cartesWagonVisibles() {

        wagonsVisibles = new HBox();
        wagonsVisibles.setSpacing(25);
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
                                image.setFitWidth(VueCarteWagon.getLongueur() * (getWidth() / maxLongueur));
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
        piocheDefausse.setSpacing(40);
        ImageView pioche = new ImageView("images/wagons.png");
        pioche.setOnMouseClicked(e->jeu.uneCarteWagonAEtePiochee());
        VueCarteWagon.texturer(pioche);
        /*
        ImageView defausse = new ImageView("images/wagons.png");
        defausse.setPreserveRatio(true);
        defausse.setFitHeight(80);
        */
        piocheDefausse.getChildren().addAll(pioche);
    }

    public void destinations() {

        ImageView pioche = new ImageView("images/destinations.png");
        VueCarteWagon.texturer(pioche);
        pioche.setOnMouseClicked(e->jeu.uneDestinationAEtePiochee());
        piocheDefausse.getChildren().addAll(pioche);

        destinations = new GridPane();
        destinations.setHgap(20);
        destinations.setVgap(20);
        //destinations.setSpacing(10);
        ListChangeListener<Destination> changementDestination = new ListChangeListener<Destination>() {
            @Override
            public void onChanged(Change<? extends Destination> arg0) {
                Platform.runLater(() -> {
                    while (arg0.next()) {

                        if (arg0.wasAdded()) {
                            for (Destination destination : arg0.getAddedSubList()) {
                                Button boutton = new Button(destination.getNom());
                                boutton.setStyle("-fx-text-fill: white; -fx-font-family: \"IM FELL English SC\";-fx-font-size: 25");
                                boutton.setId(destination.getNom());
                                boutton.setOnMouseClicked(e -> {
                                    jeu.uneDestinationAEteChoisie(boutton.getText());
                                });
                                boutton.setOnMouseEntered(e -> {
                                    boutton.setStyle(
                                            "-fx-font-family: \"IM FELL English SC\";-fx-font-size: 30; -fx-text-fill: white; -fx-border-color: white;");
                                    boutton.setEffect(new DropShadow(20, Color.BLACK));
                                });
                                boutton.setOnMouseExited(e -> {
                                    boutton.setStyle("-fx-text-fill: white; -fx-font-family: \"IM FELL English SC\";-fx-font-size: 25");
                                    boutton.setEffect(null);
                                });
                                int ligne;
                                if (destinations.getChildren().size() >= 2)
                                {
                                    ligne = 1;
                                }
                                else{
                                    ligne = 0;
                                }
                                int colonne = destinations.getChildren().size()%2;
                                destinations.add(boutton, colonne, ligne);
                            }
                        } else if (arg0.wasRemoved()) {
                            for (Destination destination : arg0.getRemoved()) {
                                destinations.getChildren().remove(trouverDestination(destination));
                            }
                        }
                    }
                });
            };
        };
        jeu.destinationsInitialesProperty().addListener(changementDestination);
    }

    public Button getPane(String id) {
        for (Node pane : destinations.getChildren())
        {
            Button l = (Button) pane;
            if (pane.getId().equals(id))
            {
                return l;
            }
        }
        return null;
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