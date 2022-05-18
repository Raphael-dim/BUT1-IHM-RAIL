package fr.umontpellier.iut.vues;
import java.util.List;

import fr.umontpellier.iut.IJeu;
import fr.umontpellier.iut.IJoueur;
import fr.umontpellier.iut.IJoueur.Couleur;
import fr.umontpellier.iut.rails.CouleurWagon;
import fr.umontpellier.iut.rails.Joueur;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
                            List<Joueur> Autresjoueurs = ((VueDuJeu) getScene().getRoot()).getJeu().getJoueurs();
                            Autresjoueurs.remove(jeu.joueurCourantProperty().getValue());
                            for (Joueur j : Autresjoueurs) 
                                {
                                    getChildren().add(panneauJoueur(j));
                                }
                        }
                    else    
                        {
                            getChildren().add(panneauJoueur(arg1));
                            getChildren().remove(getPane(arg0.getValue().getNom()));
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
        Label nom = new Label(joueur.getNom());
        nom.setStyle("-fx-font-size: 20; -fx-text-fill: "+traduire(joueur.getCouleur().name())+"; -fx-stroke-color: black");
        Label score = new Label("Score : "+ joueur.getScore());
        score.setStyle("-fx-font-size: 15; -fx-text-fill: "+traduire(joueur.getCouleur().name())+"; -fx-stroke-color: black");
        score.setLayoutY(130);
        Rectangle rectangle = new Rectangle(150, 150);
        rectangle.setStyle("-fx-fill: null; -fx-border-style: solid; -fx-border-width: 10; -fx-stroke: black;");
        Pane pane = new Pane(nom);
        pane.getChildren().addAll(rectangle, score);
        for (int i = 0; i < joueur.getNbGares(); i++)
            {
                ImageView wagon = new ImageView("images/wagons/image-wagon-"+joueur.getCouleur()+".png");
                wagon.setPreserveRatio(true);
                wagon.setFitHeight(25);
                wagon.setX(i*20);
                wagon.setY(20);
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
