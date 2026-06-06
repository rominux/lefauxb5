import org.junit.Before;
import org.junit.Test;

import fr.ulille.but.sae_s2_2026.Chemin;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

import static org.junit.Assert.*;

public class PlateformeTest {

    private Plateforme plateforme;
    private Arret paris;
    private Arret lyon;
    private Arret marseille;
    private Trajet trajetParisLyon;
    private Trajet trajetLyonMarseille;

    @Before
    public void setUp() {
        plateforme = new Plateforme();
        paris = new Arret("Paris", ModaliteTransport.TRAIN);
        lyon = new Arret("Lyon", ModaliteTransport.TRAIN);
        marseille = new Arret("Marseille", ModaliteTransport.TRAIN);

        Cout cout1 = new Cout(10.0, 5.0, 120.0);
        trajetParisLyon = new Trajet(paris, lyon, ModaliteTransport.TRAIN, cout1);

        Cout cout2 = new Cout(15.0, 7.0, 90.0);
        trajetLyonMarseille = new Trajet(lyon, marseille, ModaliteTransport.TRAIN, cout2);
    }

    @Test
    public void testEnregistrerArret() {
        plateforme.enregistrerArret("Paris", ModaliteTransport.TRAIN);
        plateforme.enregistrerArret("Lyon", ModaliteTransport.TRAIN);
    }

    @Test
    public void testAjouterTrajet() {
        plateforme.enregistrerArret("Paris", ModaliteTransport.TRAIN);
        plateforme.enregistrerArret("Lyon", ModaliteTransport.TRAIN);
        plateforme.ajouterTrajet(trajetParisLyon);
    }

    @Test
    public void testComparerAvecChemin() throws NoResultException {
        plateforme.enregistrerArret("Paris", ModaliteTransport.TRAIN);
        plateforme.enregistrerArret("Lyon", ModaliteTransport.TRAIN);
        plateforme.enregistrerArret("Marseille", ModaliteTransport.TRAIN);
        plateforme.ajouterTrajet(trajetParisLyon);
        plateforme.ajouterTrajet(trajetLyonMarseille);

        Voyageur voyageur = new Voyageur("Toto", TypeCout.TEMPS);
        List<Chemin> resultats = plateforme.comparer(paris, marseille, voyageur);
        assertNotNull(resultats);
    }

    @Test
    public void testComparerSansChemin() throws NoResultException {
        plateforme.enregistrerArret("Paris", ModaliteTransport.TRAIN);
        plateforme.enregistrerArret("Marseille", ModaliteTransport.TRAIN);

        Voyageur voyageur = new Voyageur("Toto", TypeCout.PRIX);
        try {
            plateforme.comparer(paris, marseille, voyageur);
            fail("L'exception NoResultException aurait dû être levée.");
        } catch (NoResultException e) {
            assertNotNull(e);
        } catch (Exception e) {
            fail("Une mauvaise exception a été levée : " + e.getMessage());
        }
    }

    @Test
    public void testExceptionSiAucunCheminExiste() {
        Plateforme p = new Plateforme();
        Arret lille = new Arret("Lille", ModaliteTransport.TRAIN);
        Arret nantes = new Arret("Nantes", ModaliteTransport.TRAIN);
        p.enregistrerArret("Lille", ModaliteTransport.TRAIN);
        p.enregistrerArret("Nantes", ModaliteTransport.TRAIN);
        
        try {
            p.comparerVoyages(lille, nantes, new Voyageur("Test", TypeCout.PRIX), 1, new HashMap<>());
            fail("L'exception NoResultException aurait dû être levée.");
        } catch (NoResultException e) {
        } catch (Exception e) {
            fail("Une mauvaise exception a été levée : " + e.getMessage());
        }
    }

    @Test
    public void testExceptionSiTousLesCheminsSontFiltres() {
        Plateforme p = new Plateforme();
        Arret lille = new Arret("Lille", ModaliteTransport.TRAIN);
        Arret nantes = new Arret("Nantes", ModaliteTransport.TRAIN);
        p.enregistrerArret("Lille", ModaliteTransport.TRAIN);
        p.enregistrerArret("Nantes", ModaliteTransport.TRAIN);
        
        p.ajouterTrajet(new Trajet(lille, nantes, ModaliteTransport.TRAIN, new Cout(150.0, 20.0, 1.5)));
        
        Map<TypeCout, Double> limites = new HashMap<>();
        limites.put(TypeCout.TEMPS, 30.0);
        
        try {
            p.comparerVoyages(lille, nantes, new Voyageur("Test", TypeCout.PRIX), 1, limites);
            fail("L'exception AllResultFilteredException aurait dû être levée.");
        } catch (AllResultFilteredException e) {
        } catch (Exception e) {
            fail("Une mauvaise exception a été levée : " + e.getMessage());
        }
    }

    @Test
    public void testChargementFichiersCSV() {
        Plateforme p = new Plateforme();
        File fichierReseau = new File(Plateforme.FICHIER_RESEAU);
        File fichierCorres = new File(Plateforme.FICHIER_CORRESPONDANCES);
        
        if (fichierReseau.exists() && fichierCorres.exists()) {
            try {
                p.chargerReseau(fichierReseau);
                p.chargerCorrespondances(fichierCorres);
                assertNotNull(p.getArret("A", ModaliteTransport.TRAIN));
            } catch (Exception e) {
                fail("Le chargement des fichiers CSV a généré une exception : " + e.getMessage());
            }
        }
    }

    @Test
    public void testTrierVoyages() {
        Arret a = new Arret("A", ModaliteTransport.TRAIN);
        Arret b = new Arret("B", ModaliteTransport.TRAIN);
        Arret c = new Arret("C", ModaliteTransport.TRAIN);

        Trajet t1 = new Trajet(a, b, ModaliteTransport.TRAIN, new Cout(10.0, 5.0, 50.0));
        ArrayList<Trajet> liste1 = new ArrayList<>();
        liste1.add(t1);
        Voyage v1 = new Voyage(liste1);

        Trajet t2 = new Trajet(a, c, ModaliteTransport.TRAIN, new Cout(8.0, 8.0, 60.0));
        ArrayList<Trajet> liste2 = new ArrayList<>();
        liste2.add(t2);
        Voyage v2 = new Voyage(liste2);

        Trajet t3 = new Trajet(b, c, ModaliteTransport.TRAIN, new Cout(10.0, 4.0, 70.0));
        ArrayList<Trajet> liste3 = new ArrayList<>();
        liste3.add(t3);
        Voyage v3 = new Voyage(liste3);

        List<Voyage> liste = new ArrayList<>();
        liste.add(v1);
        liste.add(v2);
        liste.add(v3);

        Plateforme.trierVoyages(liste, TypeCout.TEMPS, TypeCout.PRIX, TypeCout.CO2);

        assertEquals(v2, liste.get(0));
        assertEquals(v3, liste.get(1));
        assertEquals(v1, liste.get(2));
    }
}