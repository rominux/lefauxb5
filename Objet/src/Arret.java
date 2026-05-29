import fr.ulille.but.sae_s2_2026.Lieu;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;

public class Arret implements Lieu {
 
    private String nom;
    private ModaliteTransport type;

    public Arret(String nom, ModaliteTransport type) {
        this.nom = nom;
        this.type = type;
    }

    @Override
    public String toString() {
        return this.nom + " (" + this.type + ")";
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
        if (!(o instanceof Arret)) return false;
        Arret arret = (Arret) o;
        if (nom == null || arret.nom == null || type == null || arret.type == null) return false;
        return nom.equalsIgnoreCase(arret.nom) && type == arret.type;
    }

    @Override
    public int hashCode() {
        int result = nom == null ? 0 : nom.toLowerCase().hashCode();
        result = 31 * result + (type == null ? 0 : type.hashCode());
        return result;
    }

}
