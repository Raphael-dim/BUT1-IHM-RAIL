package fr.umontpellier.iut.vues;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 *
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends VBox {

    private IJeu jeu;

    public VueAutresJoueurs() {
        this.setSpacing(50);
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
                            for (Joueur j : autresJoueurs) 
                                {
                                    getChildren().add(panneauJoueur(j));
                                }
                        }
                    else    
                        {
                            getChildren().add(panneauJoueur(arg1));
                            getChildren().remove(getPane(arg2.getNom()));
                        }
                });
            }
        };
            jeu.joueurCourantProperty().addListener(changeListener);
    }

    public String traduire(String couleur) {

        switch(couleur)
        {
            case "JAUNE": return "#fcba03";
            case "ROUGE": return "red";
            case "BLEU": return "blue";
            case "ROSE": return "#fc0394";
            case "VERT": return "green";
        }
        return couleur;
        
    }

    public Pane panneauJoueur(IJoueur joueur) {

        Pane pane = new Pane();

        ImageView logo= new ImageView("images/avatar-"+joueur.getCouleur()+".png");
        logo.setPreserveRatio(true);
        logo.setFitHeight(100);

        Label nom = new Label(joueur.getNom());
        nom.setStyle("-fx-font-size: 20; -fx-text-fill: "+traduire(joueur.getCouleur().name())+"; -fx-stroke-color: black");
        nom.setLayoutX(80);

        Label score = new Label("Score : "+ joueur.getScore());
        score.setStyle("-fx-font-size: 15; -fx-text-fill: "+traduire(joueur.getCouleur().name())+"; -fx-stroke-color: black");
        score.setLayoutX(80);
        score.setLayoutY(70);

        Rectangle rectangle = new Rectangle(150, 150);
        rectangle.setStyle("-fx-fill: null; -fx-border-style: solid; -fx-border-width: 10; -fx-stroke: black;");

        Label gares = new Label("Gares : ");
        gares.setStyle("-fx-font-size: 15; -fx-text-fill: "+traduire(joueur.getCouleur().name())+"; -fx-stroke-color: black");
        gares.setLayoutX(80);
        gares.setLayoutY(35);

        pane.getChildren().addAll(logo, nom, gares, score);

        for (int i = 0; i < joueur.getNbGares(); i++)
            {
                ImageView wagon = new ImageView("images/wagons/image-wagon-"+joueur.getCouleur()+".png");
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
