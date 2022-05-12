package fr.umontpellier.iut.vues;

import fr.umontpellier.iut.ICouleurWagon;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Cette classe représente la vue d'une carte Wagon.
 *
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarteWagon extends Pane {

    private ICouleurWagon couleurWagon;

    public VueCarteWagon(ICouleurWagon couleurWagon) {
        this.couleurWagon = couleurWagon;
    }

    public ICouleurWagon getCouleurWagon() {
        return couleurWagon;
    }

    public ImageView AfficherCarte() {

        switch(couleurWagon.toString()){
            case "Noir" : return new ImageView("images/cartesWagons/carte-wagon-NOIR.png");
            case "Blanc" : return new ImageView("images/cartesWagons/carte-wagon-BLANC.png");
            case "Jaune" : return new ImageView("images/cartesWagons/carte-wagon-JAUNE.png");
            case "Rouge": return new ImageView("images/cartesWagons/carte-wagon-ROUGE.png");
            case "Bleu": return new ImageView("images/cartesWagons/carte-wagon-BLEU.png");
            case "Vert":return new ImageView("images/cartesWagons/carte-wagon-VERT.png");
            case "Rose": return new ImageView("images/cartesWagons/carte-wagon-ROSE.png");
            case "Orange": return new ImageView("images/cartesWagons/carte-wagon-ORANGE.png");
            case "Locomotive": return new ImageView("images/cartesWagons/carte-wagon-LOCOMOTIVE.png");
        }
        return null;
    }
}
