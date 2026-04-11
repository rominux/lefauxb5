package Version1;

import fr.ulille.but.sae_s2_2026.*;
import java.util.List;

public class GraphesV1 {

    public static void main(String[] args) {
        MultiGrapheOrienteValue graphe = new MultiGrapheOrienteValue();

        Sommet a_gare = new Sommet("A_Gare", ModaliteTransport.TRAIN);
        Sommet c_gare = new Sommet("C_Gare", ModaliteTransport.TRAIN);
        Sommet d_gare = new Sommet("D_Gare", ModaliteTransport.TRAIN);
        Sommet e_gare = new Sommet("E_Gare", ModaliteTransport.TRAIN);

        graphe.ajouterSommet(a_gare);
        graphe.ajouterSommet(c_gare);
        graphe.ajouterSommet(d_gare);
        graphe.ajouterSommet(e_gare);

        // A - C (60 min)
        graphe.ajouterArete(
            new Arete(a_gare, c_gare, ModaliteTransport.TRAIN),
            60
        );
        graphe.ajouterArete(
            new Arete(c_gare, a_gare, ModaliteTransport.TRAIN),
            60
        );

        // A - D (120 min)
        graphe.ajouterArete(
            new Arete(a_gare, d_gare, ModaliteTransport.TRAIN),
            120
        );
        graphe.ajouterArete(
            new Arete(d_gare, a_gare, ModaliteTransport.TRAIN),
            120
        );

        // A - E (60 min)
        graphe.ajouterArete(
            new Arete(a_gare, e_gare, ModaliteTransport.TRAIN),
            60
        );
        graphe.ajouterArete(
            new Arete(e_gare, a_gare, ModaliteTransport.TRAIN),
            60
        );

        // C - D (90 min)
        graphe.ajouterArete(
            new Arete(c_gare, d_gare, ModaliteTransport.TRAIN),
            90
        );
        graphe.ajouterArete(
            new Arete(d_gare, c_gare, ModaliteTransport.TRAIN),
            90
        );

        // D - E (75 min)
        graphe.ajouterArete(
            new Arete(d_gare, e_gare, ModaliteTransport.TRAIN),
            75
        );
        graphe.ajouterArete(
            new Arete(e_gare, d_gare, ModaliteTransport.TRAIN),
            75
        );

        //Calcul des 4 meilleurs itinéraires de A_Gare à D_Gare
        List<Chemin> kpcc = AlgorithmeKPCC.kpcc(graphe, a_gare, d_gare, 4);

        //Affichage lisible pour l'utilisatrice
        System.out.println(
            "RÉSULTATS VERSION 1 : RECHERCHE D'ITINÉRAIRES (TRAIN UNIQUEMENT)"
        );
        System.out.println(
            "Recherche des meilleurs chemins de " +
                a_gare +
                " à " +
                d_gare +
                " :\n"
        );

        for (int i = 0; i < kpcc.size(); i++) {
            Chemin chemin = kpcc.get(i);
            String affichage = (i + 1) + ") Itinéraire : ";

            // On récupère la liste des arêtes pour formater l'itinéraire
            List<Connexion> aretesDuChemin = chemin.aretes();

            // On affiche le point de départ
            affichage += aretesDuChemin.get(0).getDepart();

            // On boucle pour afficher toutes les étapes
            for (Connexion connexion : aretesDuChemin) {
                affichage += " -> " + connexion.getArrivee();
            }

            // On ajoute la durée totale
            affichage +=
                " | Durée totale : " + (int) chemin.poids() + " minutes";

            System.out.println(affichage);
        }
    }
}
