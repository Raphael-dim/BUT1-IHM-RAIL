package fr.umontpellier.iut.vues;
import java.security.spec.EllipticCurve;
import java.util.ArrayList;
import java.util.List;

import fr.umontpellier.iut.IJeu;
import fr.umontpellier.iut.IJoueur;
import fr.umontpellier.iut.rails.Joueur;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends GridPane {

    private IJeu jeu;

    public VueAutresJoueurs() {
        this.setHgap(50);
        this.setVgap(50);
    }

    public void creerBindings() {
        
        jeu = ((VueDuJeu) getScene().getRoot()).getJeu();
        ChangeListener<IJoueur> changeListener = new ChangeListener<IJoueur>() {
            @Override
            public void changed(ObservableValue<? extends IJoueur> arg0, IJoueur arg1, IJoueur arg2) {
                Platform.runLater(() -> {
                    if (getChildren().isEmpty())
                        {
                            List<Joueur> autresJoueurs = new ArrayList<>();
                            autresJoueurs.addAll((((VueDuJeu) getScene().getRoot()).getJeu().getJoueurs()));
                            autresJoueurs.remove(arg2);
                            int ligne = 0;
                            int colonne = 0;
                            for (Joueur j : autresJoueurs) 
                                {
                                    add(panneauJoueur(j), colonne, ligne);
                                    colonne++;
                                    if (colonne>=2)
                                        {
                                            colonne=0;
                                            ligne++;
                                        }
                                }
                        }
                    else    
                        {
                            add(panneauJoueur(arg1), getColumnIndex(getPane(arg2.getNom())), getRowIndex(getPane(arg2.getNom())));
                            getChildren().remove(getPane(arg2.getNom()));
                        }
                });
            }
        };
            jeu.joueurCourantProperty().addListener(changeListener);
    }

    public static String traduire(String couleur) {

        switch(couleur)
        {
            case "JAUNE": return "#fcba03";
            case "ROUGE": return "red";
            case "BLEU": return "#05009e";
            case "ROSE": return "#fc0394";
            case "VERT": return "#087a00";
        }
        return couleur;
        
    }

    public Pane panneauJoueur(IJoueur joueur) {

        Pane pane = new Pane();

        ImageView logo= new ImageView("images/avatar-"+joueur.getCouleur()+".png");
        logo.setPreserveRatio(true);
        logo.setFitHeight(100);

        Label nom = new Label(joueur.getNom());
        nom.setStyle("-fx-font-size: 25; -fx-text-fill: "+traduire(joueur.getCouleur().name())+"; -fx-stroke-color: black");
        nom.setLayoutX(80);

        Label score = new Label("Score : "+ joueur.getScore());
        score.setStyle("-fx-font-size: 20; -fx-text-fill: "+traduire(joueur.getCouleur().name())+"; -fx-stroke-color: black");
        score.setLayoutX(80);
        score.setLayoutY(70);

        Rectangle rectangle = new Rectangle(150, 150);
        rectangle.setStyle("-fx-fill: null; -fx-border-style: solid; -fx-border-width: 10; -fx-stroke: black;");

        Label gares = new Label("Gares : ");
        gares.setStyle("-fx-font-size: 20; -fx-text-fill: "+traduire(joueur.getCouleur().name())+"; -fx-stroke-color: black");
        gares.setLayoutX(80);
        gares.setLayoutY(35);

        Ellipse ellipse = new Ellipse();
        ellipse.setOpacity(0.4);
        Stop[] stop = { new Stop(0.1f, VueDuJeu.getCouleur(joueur.getCouleur().name())),
                        new Stop(0.7f, Color.BLACK),
            };

        // create a Linear gradient object
        LinearGradient linear_gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stop);
        ellipse.setFill(linear_gradient);
        ellipse.centerXProperty().bind(gares.layoutXProperty().multiply(1.3));
        ellipse.centerYProperty().bind(gares.layoutYProperty().multiply(1.4));
        ellipse.setRadiusX(130);
        ellipse.setRadiusY(70);

        pane.getChildren().addAll(ellipse, logo, nom, gares, score);

        for (int i = 0; i < joueur.getNbGares(); i++)
            {
                ImageView wagon = new ImageView("images/gares/gare-"+joueur.getCouleur()+".png");
                wagon.setPreserveRatio(true);
                wagon.setFitHeight(40);
                wagon.setX(i*30+130);
                wagon.setY(28);
                pane.getChildren().add(wagon);
            }
        pane.setId(joueur.getNom());
        return pane;
    }

    public Pane getPane(String id) {
        for (Node pane : getChildren())
            {
                Pane l = (Pane) pane;
                if (pane.getId().equals(id))
                    {
                        return l;
                    }
            }
        return null;
    }
}
