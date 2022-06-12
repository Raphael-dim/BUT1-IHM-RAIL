package fr.umontpellier.iut.vues;
import java.util.ArrayList;
import java.util.List;

import fr.umontpellier.iut.IJeu;
import fr.umontpellier.iut.IJoueur;
import fr.umontpellier.iut.rails.CouleurWagon;
import fr.umontpellier.iut.rails.Joueur;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
        setStyle("-fx-text-fill: #ffffff; -fx-background-color: rgba(45, 45, 45, 0.7);-fx-border-radius: 20;  -fx-background-radius: 20;");

        this.setHgap(70);
        getStylesheets().add("css/page.css");

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
                                    setWidth(getWidth() + 30);
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
            case "BLEU": return "#0569f5";
            case "ROSE": return "#f50589";
            case "VERT": return "#12A331";
        }
        return couleur;
        
    }

    public Pane panneauJoueur(IJoueur joueur) {

        Pane pane = new Pane();

        ImageView logo= new ImageView("images/avatar-"+joueur.getCouleur()+".png");
        logo.setPreserveRatio(true);
        logo.setFitHeight(100);

        Label nom = new Label(joueur.getNom());
        nom.setStyle("-fx-underline: true; -fx-font-family: \"IM FELL English SC\"; -fx-font-size: 36; -fx-text-fill: "+traduire(joueur.getCouleur().name())+"; -fx-stroke-color: black");
        nom.setLayoutX(80);

        Label score = new Label("Score : "+ joueur.getScore());
        score.setStyle("-fx-font-family: \"IM FELL English SC\";-fx-font-size: 30; -fx-text-fill: "+traduire(joueur.getCouleur().name())+"; -fx-stroke-color: black");
        score.setLayoutX(80);
        score.setLayoutY(70);

        Rectangle rectangle = new Rectangle(150, 150);
        rectangle.setStyle("-fx-font-family: \"IM FELL English SC\"; -fx-fill: null; -fx-border-style: solid; -fx-border-width: 10; -fx-stroke: black;");

        Label gares = new Label("Gares : ");
        gares.setStyle("-fx-font-family: \"IM FELL English SC\";-fx-font-size: 30; -fx-text-fill: "+traduire(joueur.getCouleur().name())+"; -fx-stroke-color: black");
        gares.setLayoutX(80);
        gares.setLayoutY(35);
        
        pane.getChildren().addAll(logo, nom, gares, score);

        for (int i = 0; i < joueur.getNbGares(); i++)
            {
                ImageView wagon = new ImageView("images/gares/gare-"+joueur.getCouleur()+".png");
                wagon.setPreserveRatio(true);
                wagon.setFitHeight(40);
                wagon.setX(i*30+180);
                wagon.setY(40);
                pane.getChildren().add(wagon);
            }
        pane.setId(joueur.getNom());

        pane.setOnMouseEntered(e->{
            if (getRowIndex(pane) == 1 || getChildren().size() < 3)
                {
                    setHeight(getHeight() + 70);
                    setPadding(new Insets(0, 0, 150, 0));
                }
            else{
                setVgap(150);
            }
            pane.getChildren().add(afficherCarte(joueur));
        });

        pane.setOnMouseExited(e->{
            setHeight(getHeight() - 70);
            setPadding(new Insets(0, 0, 0, 0));
            setVgap(50);
            pane.getChildren().remove(pane.getChildren().size() - 1);
        });



        return pane;
    }

    public Pane afficherCarte(IJoueur joueur) {

        Pane main = new Pane();
        Platform.runLater(() -> {
            main.setTranslateY(120);
            main.setTranslateX(20);
            for (CouleurWagon c : joueur.getCartesWagon())  
            {
                ImageView vueCarteWagon = new VueCarteWagon(c).AfficherCarte();
                vueCarteWagon.setPreserveRatio(true);
                vueCarteWagon.setFitWidth(75);
                if (main.getChildren().size() >= 7) {
                    int i = main.getChildren().size() / 7;
                    vueCarteWagon.setX(main.getChildren().size() % 7 * 40);
                    vueCarteWagon.setY(50 * i);
                } else {
                    vueCarteWagon.setX(main.getChildren().size() * 40);
                }
                        main.getChildren().add(vueCarteWagon);
                    }
                });
        return main;
        
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
