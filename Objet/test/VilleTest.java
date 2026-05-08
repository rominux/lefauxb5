import org.junit.Test;

import fr.ulille.but.sae_s2_2026.ModaliteTransport;

import static org.junit.Assert.*;

public class VilleTest {

    @Test
    public void testGetNom() {
        Ville v = new Ville("Paris", ModaliteTransport.TRAIN);
        assertEquals("Paris", v.getNom());
    }

    @Test
    public void testGetType() {
        Ville v = new Ville("Paris", ModaliteTransport.TRAIN);
        assertEquals(ModaliteTransport.TRAIN, v.getType());
    }

    @Test
    public void testToString() {
        Ville v = new Ville("Paris", ModaliteTransport.TRAIN);
        assertEquals("Paris", v.toString());
    }

    @Test
    public void testEqualsMemeNom() {
        Ville v1 = new Ville("Paris", ModaliteTransport.TRAIN);
        Ville v2 = new Ville("Paris", ModaliteTransport.AVION);
        assertTrue(v1.equals(v2));
    }

    @Test
    public void testEqualsCasseInsensible() {
        Ville v1 = new Ville("paris", ModaliteTransport.TRAIN);
        Ville v2 = new Ville("PARIS", ModaliteTransport.TRAIN);
        assertTrue(v1.equals(v2));
    }

    @Test
    public void testEqualsNomDifferent() {
        Ville v1 = new Ville("Paris", ModaliteTransport.TRAIN);
        Ville v2 = new Ville("Lyon", ModaliteTransport.TRAIN);
        assertFalse(v1.equals(v2));
    }

    @Test
    public void testEqualsSoiMeme() {
        Ville v = new Ville("Paris", ModaliteTransport.TRAIN);
        assertTrue(v.equals(v));
    }

    @Test
    public void testEqualsAutreType() {
        Ville v = new Ville("Paris", ModaliteTransport.TRAIN);
        assertFalse(v.equals("Paris"));
    }
}
