import org.junit.Test;

import fr.ulille.but.sae_s2_2026.ModaliteTransport;

import org.junit.Before;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class VoyageTest {

    private Voyage voyage;

    // Réseau de l'exemple 1 : A -> B -> D
    @Before
    public void setUp() {
        Arret villeA = new Arret("villeA", ModaliteTransport.TRAIN);
        Arret villeB = new Arret("villeB", ModaliteTransport.TRAIN);
        Arret villeD = new Arret("villeD", ModaliteTransport.TRAIN);

        Trajet t1 = new Trajet(villeA, villeB, ModaliteTransport.TRAIN, new Cout(80, 60, 1.7));
        Trajet t2 = new Trajet(villeB, villeD, ModaliteTransport.TRAIN, new Cout(40, 22, 2.4));

        List<Trajet> etapes = new ArrayList<>();
        etapes.add(t1);
        etapes.add(t2);

        voyage = new Voyage(etapes);
    }

    @Test
    public void testGetEtapes() {
        assertEquals(2, voyage.getEtapes().size());
    }

    @Test
    public void testGetCoutTotalTemps() {
        assertEquals(120.0, voyage.getCoutTotal(TypeCout.TEMPS), 0.001);
    }

    @Test
    public void testGetCoutTotalPrix() {
        assertEquals(82.0, voyage.getCoutTotal(TypeCout.PRIX), 0.001);
    }

    @Test
    public void testGetCoutTotalCO2() {
        assertEquals(4.1, voyage.getCoutTotal(TypeCout.CO2), 0.001);
    }

    @Test
    public void testToStringContientVilles() {
        String resultat = voyage.toString();
        assertTrue(resultat.contains("villeA"));
        assertTrue(resultat.contains("villeB"));
        assertTrue(resultat.contains("villeD"));
    }

    @Test
    public void testToStringContientCouts() {
        String resultat = voyage.toString();
        assertTrue(resultat.contains("120"));
        assertTrue(resultat.contains("82"));
        assertTrue(resultat.contains("4.1"));
    }
}
