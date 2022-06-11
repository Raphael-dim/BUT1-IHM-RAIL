package fr.umontpellier.iut.vues;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import fr.umontpellier.iut.IJoueur.Couleur;
import fr.umontpellier.iut.rails.Joueur;


/**
 * Cette classe correspond à une nouvelle fenêtre permettant de choisir le nombre et les noms des joueurs de la partie.
 *
 * Sa présentation graphique peut automatiquement être actualisée chaque fois que le nombre de joueurs change.
 * Lorsque l'utilisateur a fini de saisir les noms de joueurs, il demandera à démarrer la partie.
 */
public class VueChoixJoueurs extends Stage {

    private GridPane grille;
    private ChoiceBox<Integer> liste;
    private VBox vbox;
    private ImageView titre;
    private Pane pane;
    private ImageView image;
    private ObservableList<String> nomsJoueurs;
    private boolean demarrerPartie = false;
    public ObservableList<String> nomsJoueursProperty() {
        return nomsJoueurs;
    }

    public List<String> getNomsJoueurs() {
        return nomsJoueurs;
    }

    public VueChoixJoueurs() {
        nomsJoueurs = FXCollections.observableArrayList();
        grille = new GridPane();
        vbox = new VBox();
        titre = new ImageView("images/titre.png");
        titre.setPreserveRatio(true);
        titre.setFitWidth(680);
        vbox.getChildren().addAll(titre, grille);
        Scene scene = new Scene(vbox);
        this.setTitle("Choix des joueurs");
        this.setScene(scene);
        setWidth(700);
        setHeight(700);
        scene.getStylesheets().add("css/page.css");
        vbox.setId("page_debut");
        Afficher();

    }

    public void Afficher()  {
        
        grille.setPadding(new Insets(30));
        grille.setHgap(30);
        grille.setVgap(30);

        pane = new Pane();
        image = new ImageView("images/rail.png");
        image.setTranslateX(-40);
        pane.getChildren().add(image);
        vbox.getChildren().add(pane);
        
        Label label = new Label("Choisissez le nombre de joueurs : ");
        label.setId("choix");
        
        liste = new ChoiceBox<>(); 
        liste.setId("liste");
        liste.setOnMouseEntered(e->{
            liste.setStyle("-fx-border-color: white;-fx-border-width: 2; -fx-background-color: #000000;");
        }); 
        
        liste.setOnMouseExited(e -> {
            liste.setStyle(null);
        });
        liste.getItems().add(2);
        liste.getItems().add(3);
        liste.getItems().add(4);
        liste.getItems().add(5);


        grille.add(label, 0, 0);
        grille.add(liste, 1, 0);
        
        VBox noms = new VBox();
        noms.setSpacing(20);
        grille.add(noms, 1, 1);

        Button valider = new Button("Valider");
        valider.setOnMouseEntered(e->{
            valider.setStyle("-fx-font-family: \"IM FELL English SC\"; -fx-font-size: 25");
        });
        valider.setOnMouseExited(e -> {
            valider.setStyle("-fx-font-size: 20");
        });
        valider.setId("valider");
        grille.add(valider, 1, 2);
        valider.setOnMouseClicked(e->{
            Boolean bool = false;
            if (! noms.getChildren().isEmpty())
                {
                    bool = true;
                }
            for (Node node : noms.getChildren())
                {
                    TextField nom = (TextField) node;
                    if (nom.getText() == null || nom.getText().equals(""))
                        {
                            bool = false;
                            Label la = new Label("Chaque joueur doit avoir un nom.");
                            la.setStyle("-fx-font-size: 22; -fx-text-fill: #E81717; ");
                            grille.add(la, 0, 2);

                        }
                }
            if (bool)
                {
                    for (int i = 0; i < noms.getChildren().size(); i++)
                        {
                            TextField nom = (TextField) noms.getChildren().get(i);
                            String name = nom.getText();
                            nomsJoueurs.add(name);
                        }
                    setListeDesNomsDeJoueurs();
                }
        });

        Pane wagons = new Pane();
        wagons.translateYProperty().bind(image.translateYProperty());
        pane.getChildren().add(wagons);

        ArrayList<Joueur.Couleur> couleurs = new ArrayList<>(Arrays.asList(Joueur.Couleur.values()));
        Collections.shuffle(couleurs);
        liste.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {

                if (arg1 == null)
                    {
                        Label label = new Label("Noms des joueurs : ");
                        label.setId("choix");
                        grille.add(label, 0, 1);
                    }

                if (noms.getChildren().size() < arg2)
                    {
                        for (int i = noms.getChildren().size() ; i < arg2; i++)
                            {
                                TextField nom = new TextField();
                                Couleur c = couleurs.remove(0);
                                nom.setId(c.name());
                                ImageView wagon = new ImageView("images/wagons/image-wagon-"+c+".png");
                                wagon.setId(c.name());
                                wagon.setFitHeight(40);
                                wagon.setTranslateY(15);
                                wagon.setPreserveRatio(true);
                                wagons.getChildren().add(wagon);
                                TranslateTransition animation = new TranslateTransition();
                                animation.setDuration(Duration.millis(2500));
                                animation.setNode(wagon);
                                animation.setFromX(-100);
                                animation.setToX(getWidth() - 150 -  ( i * 110));
                                animation.play();

                                nom.setStyle("-fx-background-color: #1d1e1f;");
                                noms.getChildren().add(nom);
                            }

                    }
                else {
                    for (int i = noms.getChildren().size(); i > arg2; i--)
                    {
                            couleurs.add(Couleur.valueOf(wagons.getChildren().get(wagons.getChildren().size() - 1).getId()));
                            noms.getChildren().remove(i - 1);
                            TranslateTransition animation = new TranslateTransition();
                            animation.setDuration(Duration.millis(1500));
                            animation.setNode(wagons.getChildren().get(wagons.getChildren().size() - 1));
                            animation.setFromX(wagons.getChildren().get(wagons.getChildren().size() - 1).getTranslateX());
                            animation.setToX(-100);
                            animation.play();
                            animation.onFinishedProperty().set(e->{
                                wagons.getChildren().remove(wagons.getChildren().size() - 1);
                            });
                    }
                }
            }
        });
    }

    /**
     * Définit l'action à exécuter lorsque la liste des participants est correctement initialisée
     */
    public void setNomsDesJoueursDefinisListener(ListChangeListener<String> quandLesNomsDesJoueursSontDefinis) {
        nomsJoueurs.addListener(quandLesNomsDesJoueursSontDefinis);
    }

    protected void setListeDesNomsDeJoueurs() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        for (int i = 1; i <= getNombreDeJoueurs() ; i++) {
            String name = getJoueurParNumero(i);
            if (name == null || name.equals("")) {
                tempNamesList.clear();
                break;
            }
            else
                tempNamesList.add(name);
        }


        if (!tempNamesList.isEmpty()) {
            hide();

            nomsJoueurs.clear();
            nomsJoueurs.addAll(tempNamesList);
            demarrerPartie = true;


        }

    }

    public boolean getDemarrerPartie() {
        return demarrerPartie;
    }

    /**
     * Retourne le nombre de participants à la partie que l'utilisateur a renseigné
     */
    protected int getNombreDeJoueurs() {
        return liste.getValue();
    }

    /**
     * Retourne le nom que l'utilisateur a renseigné pour le ième participant à la partie
     * @param playerNumber : le numéro du participant
     */
    protected String getJoueurParNumero(int playerNumber) {
        return nomsJoueurs.get(playerNumber - 1);
        
    }

}
