package fr.umontpellier.iut.vues;

import javafx.application.Platform;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


/**
 * Cette classe correspond à une nouvelle fenêtre permettant de choisir le nombre et les noms des joueurs de la partie.
 *
 * Sa présentation graphique peut automatiquement être actualisée chaque fois que le nombre de joueurs change.
 * Lorsque l'utilisateur a fini de saisir les noms de joueurs, il demandera à démarrer la partie.
 */
public class VueChoixJoueurs extends Stage {

    private GridPane grille;
    private ChoiceBox<Integer> choix;
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
        Scene scene = new Scene(grille);
        grille.setStyle("-fx-background-color: darkgrey;");
        this.setTitle("Choix des joueurs");
        this.setScene(scene);
        this.setWidth(500);
        this.setHeight(400);

        this.show();
        Afficher();
    }

    public void Afficher()  {

        grille.setPadding(new Insets(30));
        grille.setHgap(30);
        grille.setVgap(30);

        Label label = new Label("Choisissez le nombre de joueurs");
        
        choix = new ChoiceBox<>();
        
        choix.getItems().add(2);
        choix.getItems().add(3);
        choix.getItems().add(4);
        choix.getItems().add(5);


        grille.add(label, 0, 0);
        grille.add(choix, 1, 0);
        
        VBox noms = new VBox();
        noms.setSpacing(20);
        grille.add(noms, 1, 1);

        Button valider = new Button("Valider");
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

        choix.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {

                if (arg1 == null)
                    {
                        grille.add(new Label("Noms de joueurs"), 0, 1);
                    }

                if (noms.getChildren().size() < arg2)
                    {
                        for (int i = noms.getChildren().size() ; i < arg2; i++)
                            {
                                TextField nom = new TextField();
                                
                                noms.getChildren().add(nom);
                            }

                    }
                else {
                    for (int i = noms.getChildren().size(); i > arg2; i--)
                        {
                            noms.getChildren().remove(i - 1);
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

    /**
     * Définit l'action à exécuter lorsque le nombre de participants change
     */
    protected void setChangementDuNombreDeJoueursListener(ChangeListener<Integer> quandLeNombreDeJoueursChange) {
        System.out.println("caca");
    }

    /**
     * Vérifie que tous les noms des participants sont renseignés
     * et affecte la liste définitive des participants
     */
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
        return choix.getValue();
    }

    /**
     * Retourne le nom que l'utilisateur a renseigné pour le ième participant à la partie
     * @param playerNumber : le numéro du participant
     */
    protected String getJoueurParNumero(int playerNumber) {
        return nomsJoueurs.get(playerNumber - 1);
        
    }

}
