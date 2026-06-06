
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.ulille.but.sae_s2_2026.Chemin;
import fr.ulille.but.sae_s2_2026.Connexion;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;
 
public class Voyage implements Serializable {
 
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
        while (!etapes.isEmpty() && etapes.get(0).getModalite() == null) {
            etapes.remove(0);
        }
        while (!etapes.isEmpty() && etapes.get(etapes.size() - 1).getModalite() == null) {
            etapes.remove(etapes.size() - 1);
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
        ModaliteTransport modalite = premierTrajet.getModalite();
        resultat.append(premierTrajet.getDepart().getNom()).append(" (").append(modalite).append(")");

        for (int i = 0; i < etapes.size(); i++) {
            Trajet t = etapes.get(i);
            if (t.getModalite() != modalite && t.getModalite() == null) {
                resultat.append(" -> ").append(t.getArrivee().getNom()).append(" (").append(t.getArrivee().getType()).append(")");
                modalite = t.getArrivee().getType();
            }
            if (i == etapes.size() - 1) {
                resultat.append(" -> ").append(t.getArrivee().getNom());
            }
        }

        resultat.append(" | ").append((int) getCoutTotal(TypeCout.TEMPS)).append(" min")
                .append(" | ").append(String.format("%.2f", getCoutTotal(TypeCout.CO2))).append(" kg CO2e")
                .append(" | ").append(String.format("%.2f", getCoutTotal(TypeCout.PRIX))).append(" €");

        return resultat.toString();
    }

    public String toStringDetaille() {
        StringBuilder resultat = new StringBuilder();
        for (Trajet t : etapes) {
            resultat.append(t.toString()).append("\n");
        }
        return resultat.toString();
    }

    public String toStringHistorique() {
        StringBuilder resultat = new StringBuilder();
        resultat.append(etapes.get(0).getDepart().getNom())
                .append(" -> ")
                .append(etapes.get(etapes.size() - 1).getArrivee().getNom())
                .append(" | ").append((int) getCoutTotal(TypeCout.TEMPS)).append(" min")
                .append(" | ").append(String.format("%.2f", getCoutTotal(TypeCout.CO2))).append(" kg CO2e")
                .append(" | ").append(String.format("%.2f", getCoutTotal(TypeCout.PRIX))).append(" €");
        return resultat.toString();
    }
}