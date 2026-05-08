import fr.ulille.but.sae_s2_2026.Connexion;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;

public class Trajet implements Connexion {

    private Ville depart;
    private Ville arrivee;
    private ModaliteTransport modalite;
    private Cout cout;              
    
    public Trajet(Ville depart, Ville arrivee, ModaliteTransport modalite, Cout cout) {
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

    public Ville getDepart() {
        return this.depart;
    }

    public Ville getArrivee() {
        return this.arrivee;
    }

    @Override
    public String toString() {
        return "de " + depart + " à " + arrivee;
    }
}