package Version2;

import fr.ulille.but.sae_s2_2026.*;
import java.util.List;

public class GraphesV2 {
    public static void main(String[] args) {
        MultiGrapheOrienteValue graphe = new MultiGrapheOrienteValue();

        // Gares
        Sommet a_gare = new Sommet("A_Gare", ModaliteTransport.TRAIN);
        Sommet c_gare = new Sommet("C_Gare", ModaliteTransport.TRAIN);
        Sommet d_gare = new Sommet("D_Gare", ModaliteTransport.TRAIN);
        Sommet e_gare = new Sommet("E_Gare", ModaliteTransport.TRAIN);

        // Aéroports
        Sommet a_aero = new Sommet("A_Aeroport", ModaliteTransport.AVION);
        Sommet c_aero = new Sommet("C_Aeroport", ModaliteTransport.AVION);

        // Arrêts de bus
        Sommet a_bus = new Sommet("A_ArretBus", ModaliteTransport.BUS);
        Sommet b_bus = new Sommet("B_ArretBus", ModaliteTransport.BUS);
        Sommet c_bus = new Sommet("C_ArretBus", ModaliteTransport.BUS);
        Sommet d_bus = new Sommet("D_ArretBus", ModaliteTransport.BUS);
        Sommet e_bus = new Sommet("E_ArretBus", ModaliteTransport.BUS);

        // Ajout des sommets au graphe
        graphe.ajouterSommet(a_gare); graphe.ajouterSommet(c_gare);
        graphe.ajouterSommet(d_gare); graphe.ajouterSommet(e_gare);
        graphe.ajouterSommet(a_aero); graphe.ajouterSommet(c_aero);
        graphe.ajouterSommet(a_bus);  graphe.ajouterSommet(b_bus);
        graphe.ajouterSommet(c_bus);  graphe.ajouterSommet(d_bus);
        graphe.ajouterSommet(e_bus);

        // AVION
        graphe.ajouterArete(new Arete(a_aero, c_aero, ModaliteTransport.AVION), 110);
        graphe.ajouterArete(new Arete(c_aero, a_aero, ModaliteTransport.AVION), 110);

        // TRAIN
        graphe.ajouterArete(new Arete(a_gare, c_gare, ModaliteTransport.TRAIN), 40);
        graphe.ajouterArete(new Arete(c_gare, a_gare, ModaliteTransport.TRAIN), 40);
        graphe.ajouterArete(new Arete(a_gare, d_gare, ModaliteTransport.TRAIN), 60);
        graphe.ajouterArete(new Arete(d_gare, a_gare, ModaliteTransport.TRAIN), 60);
        graphe.ajouterArete(new Arete(a_gare, e_gare, ModaliteTransport.TRAIN), 30);
        graphe.ajouterArete(new Arete(e_gare, a_gare, ModaliteTransport.TRAIN), 30);
        graphe.ajouterArete(new Arete(c_gare, d_gare, ModaliteTransport.TRAIN), 70);
        graphe.ajouterArete(new Arete(d_gare, c_gare, ModaliteTransport.TRAIN), 70);
        graphe.ajouterArete(new Arete(d_gare, e_gare, ModaliteTransport.TRAIN), 35);
        graphe.ajouterArete(new Arete(e_gare, d_gare, ModaliteTransport.TRAIN), 35);

        // BUS
        graphe.ajouterArete(new Arete(a_bus, b_bus, ModaliteTransport.BUS), 10);
        graphe.ajouterArete(new Arete(b_bus, a_bus, ModaliteTransport.BUS), 10);
        graphe.ajouterArete(new Arete(a_bus, e_bus, ModaliteTransport.BUS), 20);
        graphe.ajouterArete(new Arete(e_bus, a_bus, ModaliteTransport.BUS), 20);
        graphe.ajouterArete(new Arete(b_bus, c_bus, ModaliteTransport.BUS), 25);
        graphe.ajouterArete(new Arete(c_bus, b_bus, ModaliteTransport.BUS), 25);
        graphe.ajouterArete(new Arete(b_bus, e_bus, ModaliteTransport.BUS), 15);
        graphe.ajouterArete(new Arete(e_bus, b_bus, ModaliteTransport.BUS), 15);
        graphe.ajouterArete(new Arete(c_bus, d_bus, ModaliteTransport.BUS), 15);
        graphe.ajouterArete(new Arete(d_bus, c_bus, ModaliteTransport.BUS), 15);
        graphe.ajouterArete(new Arete(d_bus, e_bus, ModaliteTransport.BUS), 12);
        graphe.ajouterArete(new Arete(e_bus, d_bus, ModaliteTransport.BUS), 12);

        // Ville A
        graphe.ajouterArete(new Arete(a_aero, a_bus, ModaliteTransport.BUS), 0);
        graphe.ajouterArete(new Arete(a_bus, a_aero, ModaliteTransport.BUS), 0);
        graphe.ajouterArete(new Arete(a_bus, a_gare, ModaliteTransport.BUS), 0);
        graphe.ajouterArete(new Arete(a_gare, a_bus, ModaliteTransport.BUS), 0);

        // Ville C
        graphe.ajouterArete(new Arete(c_aero, c_bus, ModaliteTransport.BUS), 0);
        graphe.ajouterArete(new Arete(c_bus, c_aero, ModaliteTransport.BUS), 0);
        graphe.ajouterArete(new Arete(c_bus, c_gare, ModaliteTransport.BUS), 0);
        graphe.ajouterArete(new Arete(c_gare, c_bus, ModaliteTransport.BUS), 0);

        // Ville D
        graphe.ajouterArete(new Arete(d_bus, d_gare, ModaliteTransport.BUS), 0);
        graphe.ajouterArete(new Arete(d_gare, d_bus, ModaliteTransport.BUS), 0);

        // Ville E
        graphe.ajouterArete(new Arete(e_bus, e_gare, ModaliteTransport.BUS), 0);
        graphe.ajouterArete(new Arete(e_gare, e_bus, ModaliteTransport.BUS), 0);


        List<Chemin> kpcc = AlgorithmeKPCC.kpcc(graphe, d_bus, a_bus, 4);

        System.out.println("RÉSULTATS VERSION 2 : RECHERCHE MULTIMODALE");
        System.out.println("Critère d'optimisation : PRIX le moins cher");
        System.out.println("Recherche des meilleurs chemins de " + d_bus + " à " + a_bus + " :\n");

        for (int i = 0; i < kpcc.size(); i++) {
            Chemin chemin = kpcc.get(i);
            String affichage = (i + 1) + ") Itinéraire : ";

            List<Connexion> aretesDuChemin = chemin.aretes();
            affichage += aretesDuChemin.get(0).getDepart();

            for (Connexion connexion : aretesDuChemin) {
                affichage += " -> " + connexion.getArrivee();
            }

            // On affiche le prix total au lieu de la durée
            affichage += " | Prix total : " + (int) chemin.poids() + " €";

            System.out.println(affichage);
        }
    }
}
