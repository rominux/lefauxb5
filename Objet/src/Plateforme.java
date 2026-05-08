import java.util.List;

import fr.ulille.but.sae_s2_2026.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2026.Chemin;
import fr.ulille.but.sae_s2_2026.Connexion;
import fr.ulille.but.sae_s2_2026.Lieu;
import fr.ulille.but.sae_s2_2026.MultiGrapheOrienteValue;

public class Plateforme {
    private MultiGrapheOrienteValue graphe;

    public Plateforme() {
        graphe = new MultiGrapheOrienteValue();
    }

    public void ajouterVille(Ville ville) {
        graphe.ajouterSommet(ville);
    }
    public void ajouterTrajet(Trajet trajet) {
        graphe.ajouterArete(trajet, 0);
    }

    public List<Chemin> comparer(Lieu depart, Lieu arrivee, Voyageur voyageur) {
        TypeCout typeCout = voyageur.getCritere();
        for (Connexion c : graphe.aretes()) {
            Trajet t = (Trajet) c;
            Cout cout = t.getCout();
            double valeur = cout.getValeur(typeCout);
            graphe.modifierPoidsArete(t, valeur);
        }
        List<Chemin> kpcc = AlgorithmeKPCC.kpcc(graphe, depart, arrivee, 4);
        return kpcc;
    }
}
