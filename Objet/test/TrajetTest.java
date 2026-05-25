import org.junit.Test;

import fr.ulille.but.sae_s2_2026.ModaliteTransport;

import static org.junit.Assert.*;

public class TrajetTest {

    private Arret villeA = new Arret("villeA", ModaliteTransport.TRAIN);
    private Arret villeB = new Arret("villeB", ModaliteTransport.TRAIN);
    private Cout cout    = new Cout(80, 60, 1.7);
    private Trajet trajet = new Trajet(villeA, villeB, ModaliteTransport.TRAIN, cout);

    @Test
    public void testGetDepart() {
        assertEquals(villeA, trajet.getDepart());
    }

    @Test
    public void testGetArrivee() {
        assertEquals(villeB, trajet.getArrivee());
    }

    @Test
    public void testGetModalite() {
        assertEquals(ModaliteTransport.TRAIN, trajet.getModalite());
    }

    @Test
    public void testGetCout() {
        assertEquals(cout, trajet.getCout());
    }

    @Test
    public void testToString() {
        String resultat = trajet.toString();
        assertTrue(resultat.contains("villeA"));
        assertTrue(resultat.contains("villeB"));
    }
}
