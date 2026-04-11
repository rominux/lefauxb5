package Version1;

import fr.ulille.but.sae_s2_2026.Lieu;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;

public class Sommet implements Lieu {

    private String nom;
    private ModaliteTransport type;

    public Sommet(String nom, ModaliteTransport type) {
        this.nom = nom;
        this.type = type;
    }

    @Override
    public String toString() {
        return this.nom;
    }

    public ModaliteTransport getType() {
        return this.type;
    }

    public String getNom() {
        return this.nom;
    }
}
