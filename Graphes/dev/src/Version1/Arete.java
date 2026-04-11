package Version1;

import fr.ulille.but.sae_s2_2026.Connexion;
import fr.ulille.but.sae_s2_2026.Lieu;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;

public class Arete implements Connexion {

    private Sommet depart;
    private Sommet arrivee;
    private ModaliteTransport modalite;

    public Arete(Sommet depart, Sommet arrivee, ModaliteTransport modalite) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.modalite = modalite;
    }

    @Override
    public ModaliteTransport getModalite() {
        return modalite;
    }

    @Override
    public Lieu getDepart() {
        return this.depart;
    }

    @Override
    public Lieu getArrivee() {
        return this.arrivee;
    }

    @Override
    public String toString() {
        return "de " + depart + " à " + arrivee;
    }
}
