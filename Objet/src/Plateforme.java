import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ulille.but.sae_s2_2026.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2026.Chemin;
import fr.ulille.but.sae_s2_2026.Connexion;
import fr.ulille.but.sae_s2_2026.Lieu;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;
import fr.ulille.but.sae_s2_2026.MultiGrapheOrienteValue;

public class Plateforme {
    private MultiGrapheOrienteValue graphe;
    public static final String FICHIER_RESEAU = "exemple-reseau.csv";
    public static final String FICHIER_CORRESPONDANCES = "exemple-correspondances.csv";

    public Plateforme() {
        graphe = new MultiGrapheOrienteValue();
    }
    public Arret getArret(String nom, ModaliteTransport modalite) {
        for (Lieu lieu : graphe.sommets()) {
            if (lieu instanceof Arret) {
                Arret arret = (Arret) lieu;
                if (arret.getNom().equals(nom) && arret.getType().equals(modalite)) return arret;
            }
        }
        return null; 
    }

    public void chargerReseau(File fichierReseau) {
        try (BufferedReader br = new BufferedReader(new FileReader(fichierReseau))) {
            String ligne = br.readLine();
            do {
                String[] morceaux = ligne.split(";");
                String nomDepart = morceaux[0].trim();
                String nomArrivee = morceaux[1].trim();
                ModaliteTransport modaliteLigne = ModaliteTransport.valueOf(morceaux[2].trim().toUpperCase());

                double prix = Double.parseDouble(morceaux[3]);
                double co2 = Double.parseDouble(morceaux[4]);
                double temps = Double.parseDouble(morceaux[5]);

                Arret depart = enregistrerArret(nomDepart, modaliteLigne);
                Arret arrivee = enregistrerArret(nomArrivee, modaliteLigne);
                Cout cout = new Cout(temps, prix, co2);

                Trajet aller = new Trajet(depart, arrivee, modaliteLigne, cout);
                Trajet retour = new Trajet(arrivee, depart, modaliteLigne, cout);
                // on donne un poids de 0 car on définira les poids par rapport au critère choisi plus tard
                graphe.ajouterArete(aller, 0);
                graphe.ajouterArete(retour, 0);
                ligne = br.readLine();
            } while (ligne != null && ligne.trim().length() > 0);
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier réseau");
            e.printStackTrace();
        }
    }
    
    public void chargerCorrespondances(File fichierCorrespondances) {
        try (BufferedReader br = new BufferedReader(new FileReader(fichierCorrespondances))) {
            String ligne = br.readLine();
            do {
                String[] morceaux = ligne.split(";");
                String nom = morceaux[0].trim();
                ModaliteTransport modaliteDepart = ModaliteTransport.valueOf(morceaux[1].trim().toUpperCase());
                ModaliteTransport modaliteArrivee = ModaliteTransport.valueOf(morceaux[2].trim().toUpperCase());

                double prix = Double.parseDouble(morceaux[3]);
                double co2 = Double.parseDouble(morceaux[4]);
                double temps = Double.parseDouble(morceaux[5]);

                Arret depart = enregistrerArret(nom, modaliteDepart);
                Arret arrivee = enregistrerArret(nom, modaliteArrivee);
                Cout cout = new Cout(temps, prix, co2);

                Trajet aller = new Trajet(depart, arrivee, null, cout);
                Trajet retour = new Trajet(arrivee, depart, null, cout);
                
                graphe.ajouterArete(aller, 0);
                graphe.ajouterArete(retour, 0);
                ligne = br.readLine();
            } while (ligne != null && ligne.trim().length() > 0);
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier correspondances");
            e.printStackTrace();
        }
    }

    public List<Chemin> comparer(Lieu depart, Lieu arrivee, Voyageur voyageur) {
        return comparer(depart, arrivee, voyageur, 4);
    }

    public List<Chemin> comparer(Lieu depart, Lieu arrivee, Voyageur voyageur, int nombre) {
        TypeCout typeCout = voyageur.getCritere();
        for (Connexion c : graphe.aretes()) {
            Trajet t = (Trajet) c;
            Cout cout = t.getCout();
            double valeur = cout.getValeur(typeCout);
            graphe.modifierPoidsArete(t, valeur);
        }
        List<Chemin> kpcc = AlgorithmeKPCC.kpcc(graphe, depart, arrivee, nombre);
        return kpcc;
    }

    public List<Voyage> comparerVoyages(Lieu depart, Lieu arrivee, Voyageur voyageur, int maxResultats, Map<TypeCout, Double> limites) {
        int kRecherche = Math.max(maxResultats, 10);
        List<Chemin> chemins = comparer(depart, arrivee, voyageur, kRecherche);
        List<Voyage> voyages = new ArrayList<>();
        for (Chemin chemin : chemins) {
            Voyage voyage = new Voyage(chemin);
            if (respecteLimites(voyage, limites)) {
                voyages.add(voyage);
                if (voyages.size() == maxResultats) {
                    break;
                }
            }
        }
        return voyages;
    }

    private static boolean respecteLimites(Voyage voyage, Map<TypeCout, Double> limites) {
        if (limites == null || limites.isEmpty()) {
            return true;
        }
        for (Map.Entry<TypeCout, Double> entree : limites.entrySet()) {
            Double max = entree.getValue();
            if (max != null && voyage.getCoutTotal(entree.getKey()) > max) {
                return false;
            }
        }
        return true;
    }

    private Arret enregistrerArret(String nom, ModaliteTransport modalite) {
        Arret arret = new Arret(nom.trim(), modalite);
        graphe.ajouterSommet(arret);
        return arret;
    }

    public static void main(String[] args) {
        Plateforme plateforme = new Plateforme();
        TypeCout critere = TypeCout.CO2;
        int maxResultats = 4;

        File fichierReseau = new File(Plateforme.FICHIER_RESEAU);
        if (fichierReseau.exists() && fichierReseau.isFile()) {
            plateforme.chargerReseau(fichierReseau);
        } else {
            fichierReseau = new File("Objet" + File.separator + Plateforme.FICHIER_RESEAU);
            if (fichierReseau.exists() && fichierReseau.isFile()) {
                plateforme.chargerReseau(fichierReseau);
            } else {
                System.out.println("Fichier Réseau introuvable");
                throw new RuntimeException("Fichier Réseau introuvable");
            }
        }
        File fichierCorrespondances = new File(Plateforme.FICHIER_CORRESPONDANCES);
        if (fichierCorrespondances.exists() && fichierCorrespondances.isFile()) {
            plateforme.chargerCorrespondances(fichierCorrespondances);
        } else {
            fichierCorrespondances = new File("Objet" + File.separator + Plateforme.FICHIER_CORRESPONDANCES);
            if (fichierCorrespondances.exists() && fichierCorrespondances.isFile()) {
                plateforme.chargerCorrespondances(fichierCorrespondances);
            } else {
                System.out.println("Fichier Correspondances introuvable");
                throw new RuntimeException("Fichier Correspondances introuvable");
            }
        }
        
        Arret depart = plateforme.getArret("A", ModaliteTransport.TRAIN);
        Arret arrivee = plateforme.getArret("M", ModaliteTransport.TRAIN);
        if (depart == null || arrivee == null) {
            System.out.println("Ville de depart ou d'arrivee introuvable pour la modalite choisie.");
            return;
        }

        Voyageur voyageur = new Voyageur("Utilisateur", critere);
        Map<TypeCout, Double> limites = new HashMap<>();
        limites.put(TypeCout.TEMPS, 180.0);

        List<Voyage> meilleurs = plateforme.comparerVoyages(depart, arrivee, voyageur, maxResultats, limites);

        System.out.println("RESULTATS VERSION 2 :");
        System.out.println("Critere : " + critere);
        System.out.println("Contraintes : TEMPS <= 180");
        System.out.println("Recherche des meilleurs voyages de " + depart + " a " + arrivee + " :\n");

        if (meilleurs.isEmpty()) {
            System.out.println("Aucun voyage possible avec ces criteres.");
            return;
        }

        for (int i = 0; i < meilleurs.size(); i++) {
            Voyage voyage = meilleurs.get(i);
            System.out.println((i + 1) + ") " + voyage);
        }
    }
}
