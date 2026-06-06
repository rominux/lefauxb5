import java.io.Serializable;

import fr.ulille.but.sae_s2_2026.Connexion;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;

public class Trajet implements Connexion, Serializable {

    private Arret depart;
    private Arret arrivee;
    private ModaliteTransport modalite;
    private Cout cout;              
    
    public Trajet(Arret depart, Arret arrivee, ModaliteTransport modalite, Cout cout) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.modalite = modalite;
        this.cout = cout;          
    }

    public Cout getCout() {
        return cout;                
    }

    public ModaliteTransport getModalite() {
        return this.modalite;
    }

    public Arret getDepart() {
        return this.depart;
    }

    public Arret getArrivee() {
        return this.arrivee;
    }

    public boolean estCorrespondance() {
        return this.modalite == null;
    }

    @Override
    public String toString() {
        return "de " + depart + " à " + arrivee;
    }
}