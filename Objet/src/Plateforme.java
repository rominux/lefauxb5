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
    private Map<String, Ville> villes;

    public Plateforme() {
        graphe = new MultiGrapheOrienteValue();
        villes = new HashMap<>();
    }

    public void ajouterVille(Ville ville) {
        graphe.ajouterSommet(ville);
        if (ville != null) {
            villes.put(normaliserNom(ville.getNom()), ville);
        }
    }
    public void ajouterTrajet(Trajet trajet) {
        graphe.ajouterArete(trajet, 0);
    }

    public Ville getVille(String nom) {
        if (nom == null) {
            return null;
        }
        return villes.get(normaliserNom(nom));
    }

    public void chargerDonnees(String[] data) {
        validerDonnees(data);
        for (String ligne : data) {
            String[] morceaux = ligne.split(";");
            String nomDepart = morceaux[0].trim();
            String nomArrivee = morceaux[1].trim();
            ModaliteTransport modaliteLigne = parseModalite(morceaux[2]);

            double prix = parseValeur(morceaux[3], "prix");
            double co2 = parseValeur(morceaux[4], "co2");
            double temps = parseValeur(morceaux[5], "temps");

            Ville depart = enregistrerVille(nomDepart, modaliteLigne);
            Ville arrivee = enregistrerVille(nomArrivee, modaliteLigne);
            Cout cout = new Cout(temps, prix, co2);

            Trajet aller = new Trajet(depart, arrivee, modaliteLigne, cout);
            Trajet retour = new Trajet(arrivee, depart, modaliteLigne, cout);
            ajouterTrajet(aller);
            ajouterTrajet(retour);
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

    private Ville enregistrerVille(String nom, ModaliteTransport modalite) {
        String cle = normaliserNom(nom);
        Ville ville = villes.get(cle);
        if (ville == null) {
            ville = new Ville(nom.trim(), modalite);
            villes.put(cle, ville);
            graphe.ajouterSommet(ville);
        }
        return ville;
    }

    private static String normaliserNom(String nom) {
        return nom.trim().toLowerCase();
    }

    private static void validerDonnees(String[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Donnees manquantes");
        }
        for (int i = 0; i < data.length; i++) {
            String ligne = data[i];
            if (ligne == null) {
                throw new IllegalArgumentException("Ligne vide a l'index " + i);
            }
            String[] morceaux = ligne.split(";");
            if (morceaux.length != 6) {
                throw new IllegalArgumentException("Donnees incompletes a la ligne " + (i + 1));
            }
            parseModalite(morceaux[2]);
            parseValeur(morceaux[3], "prix");
            parseValeur(morceaux[4], "co2");
            parseValeur(morceaux[5], "temps");
        }
    }

    private static ModaliteTransport parseModalite(String valeur) {
        String normalisee = valeur.trim().toUpperCase();
        return ModaliteTransport.valueOf(normalisee);
    }

    private static double parseValeur(String valeur, String champ) {
        double resultat = Double.parseDouble(valeur.trim());
        if (resultat < 0) {
            throw new IllegalArgumentException("Valeur negative pour " + champ);
        }
        return resultat;
    }

    public static void main(String[] args) {
        Plateforme plateforme = new Plateforme();
        String[] data = new String[] {
            "villeA;villeB;Train;60;1.7;80",
            "villeB;villeD;Train;22;2.4;40",
            "villeA;villeC;Train;42;1.4;50",
            "villeB;villeC;Train;14;1.4;60",
            "villeC;villeD;Avion;110;150;22",
            "villeC;villeD;Train;65;1.2;90"
        };

        TypeCout critere = TypeCout.CO2;
        int maxResultats = 4;

        plateforme.chargerDonnees(data);

        Ville depart = plateforme.getVille("villeA");
        Ville arrivee = plateforme.getVille("villeD");
        if (depart == null || arrivee == null) {
            System.out.println("Ville de depart ou d'arrivee introuvable pour la modalite choisie.");
            return;
        }

        Voyageur voyageur = new Voyageur("Utilisateur", critere);
        Map<TypeCout, Double> limites = new HashMap<>();
        limites.put(TypeCout.TEMPS, 180.0);

        List<Voyage> meilleurs = plateforme.comparerVoyages(depart, arrivee, voyageur, maxResultats, limites);

        System.out.println("RESULTATS VERSION 1 : MODALITE UNIQUE");
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
