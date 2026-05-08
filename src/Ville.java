import fr.ulille.but.sae_s2_2026.Lieu;

public class Ville implements Lieu {
 
    private String nom;
    private ModaliteTransport type;

    public Ville(String nom, ModaliteTransport type) {
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ville)) return false;
        Ville ville = (Ville) o;
        return nom.toLowerCase().equals(ville.nom.toLowerCase());
    }
 
    
    
}