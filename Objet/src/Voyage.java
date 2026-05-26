
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
        if (etapes == null || etapes.isEmpty()) {
            return "Voyage vide";
        }

        StringBuilder resultat = new StringBuilder();
        
        Trajet premierTrajet = etapes.get(0);
        resultat.append(premierTrajet.getDepart().getNom());
        if (premierTrajet.getModalite() != null) {
            resultat.append(" (").append(premierTrajet.getModalite()).append(")");
        }

        for (int i = 0; i < etapes.size(); i++) {
            Trajet t = etapes.get(i);

            if (t.getModalite() == null) {
                resultat.append(" -> ").append(t.getDepart().getNom()).append(" [Changement]");
                
                for (int j = i + 1; j < etapes.size(); j++) {
                    if (etapes.get(j).getModalite() != null) {
                        resultat.append(" (").append(etapes.get(j).getModalite()).append(")");
                        break;
                    }
                }
            }

            if (i == etapes.size() - 1) {
                resultat.append(" -> ").append(t.getArrivee().getNom());
            }
        }

        resultat.append(" | ").append(getCoutTotal(TypeCout.TEMPS)).append(" min")
                .append(" | ").append(getCoutTotal(TypeCout.CO2)).append(" kg CO2e")
                .append(" | ").append(getCoutTotal(TypeCout.PRIX)).append(" €");

        return resultat.toString();
    }
}