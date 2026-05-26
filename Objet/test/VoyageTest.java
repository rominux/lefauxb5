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
        assertTrue(resultat.contains("villeD"));
    }
    @Test
    public void testToStringContientCouts() {
        String resultat = voyage.toString();
        assertTrue(resultat.contains("120"));
        assertTrue(resultat.contains("82"));
        assertTrue(resultat.contains("4.1"));
    }

    @Test
public void testToStringPointsDInteretUniquement() {
    Arret lille = new Arret("Lille", ModaliteTransport.TRAIN);
    Arret douai = new Arret("Douai", ModaliteTransport.TRAIN);
    Arret arras = new Arret("Arras", ModaliteTransport.BUS);
    Arret bapaume = new Arret("Bapaume", ModaliteTransport.BUS);

    List<Trajet> etapes = new ArrayList<>();
    etapes.add(new Trajet(lille, douai, ModaliteTransport.TRAIN, new Cout(20, 10, 0.5)));
    etapes.add(new Trajet(douai, douai, null, new Cout(10, 0, 0)));
    etapes.add(new Trajet(douai, arras, ModaliteTransport.BUS, new Cout(30, 5, 0.2)));
    etapes.add(new Trajet(arras, bapaume, ModaliteTransport.BUS, new Cout(20, 3, 0.1)));

    Voyage voyageMultimodal = new Voyage(etapes);
    String affichage = voyageMultimodal.toString();

    assertTrue(affichage.contains("Lille (TRAIN)"));
    assertTrue(affichage.contains("Douai [Changement] (BUS)"));
    assertTrue(affichage.contains("Bapaume"));

    assertFalse(affichage.contains("Arras"));
}
}
