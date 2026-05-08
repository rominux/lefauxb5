
import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2026.Chemin;
import fr.ulille.but.sae_s2_2026.Connexion;
 
public class Voyage {
 
    private List<Trajet> etapes;
 
    public Voyage(List<Trajet> etapes) {
        this.etapes = etapes;
    }
    public Voyage(Chemin chemin) {
        this.etapes = new ArrayList<Trajet>();
        for (Connexion c : chemin.aretes()) {
            Trajet t = (Trajet) c;
            etapes.add(t);
        }
    }
 
    public List<Trajet> getEtapes() {
        return etapes;
    }
 
    public double getCoutTotal(TypeCout type) {
        double total = 0;
        for (Trajet c : etapes) {
            Trajet t = (Trajet) c;
            total += t.getCout().getValeur(type);
        }
        return total;
    }
 
    @Override
    public String toString() {
        String resultat = "";
        for (int i = 0; i < etapes.size(); i++) {
            Trajet t = (Trajet) etapes.get(i);
            if (i == 0) {
                resultat += t.getDepart().getNom();
            }
            resultat += " -> " + t.getArrivee().getNom() + " (" + t.getModalite() + ")";
        }
        resultat += " | " + getCoutTotal(TypeCout.TEMPS) + " min"
                + " | " + getCoutTotal(TypeCout.CO2) + " kg CO2e"
                + " | " + getCoutTotal(TypeCout.PRIX) + " €";
        return resultat;
    }
}