import org.junit.Before;
import org.junit.Test;

import fr.ulille.but.sae_s2_2026.Chemin;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;

import java.util.List;

import static org.junit.Assert.*;

public class PlateformeTest {

    private Plateforme plateforme;
    private Ville paris;
    private Ville lyon;
    private Ville marseille;
    private Trajet trajetParisLyon;
    private Trajet trajetLyonMarseille;

    @Before
    public void setUp() {
        plateforme = new Plateforme();
        paris = new Ville("Paris", ModaliteTransport.TRAIN);
        lyon = new Ville("Lyon", ModaliteTransport.TRAIN);
        marseille = new Ville("Marseille", ModaliteTransport.TRAIN);

        Cout cout1 = new Cout(10.0, 5.0, 120.0);
        trajetParisLyon = new Trajet(paris, lyon, ModaliteTransport.TRAIN, cout1);

        Cout cout2 = new Cout(15.0, 7.0, 90.0);
        trajetLyonMarseille = new Trajet(lyon, marseille, ModaliteTransport.TRAIN, cout2);
    }

    @Test
    public void testAjouterVille() {
        plateforme.ajouterVille(paris);
        plateforme.ajouterVille(lyon);
    }

    @Test
    public void testAjouterTrajet() {
        plateforme.ajouterVille(paris);
        plateforme.ajouterVille(lyon);
        plateforme.ajouterTrajet(trajetParisLyon);
    }

    @Test
    public void testComparerAvecChemin() {
        plateforme.ajouterVille(paris);
        plateforme.ajouterVille(lyon);
        plateforme.ajouterVille(marseille);
        plateforme.ajouterTrajet(trajetParisLyon);
        plateforme.ajouterTrajet(trajetLyonMarseille);

        Voyageur voyageur = new Voyageur("Toto", TypeCout.TEMPS);
        List<Chemin> resultats = plateforme.comparer(paris, marseille, voyageur);
        assertNotNull(resultats);
    }

    @Test
    public void testComparerSansChemin() {
        plateforme.ajouterVille(paris);
        plateforme.ajouterVille(marseille);

        Voyageur voyageur = new Voyageur("Toto", TypeCout.PRIX);
        List<Chemin> resultats = plateforme.comparer(paris, marseille, voyageur);
        assertNotNull(resultats);
    }
}