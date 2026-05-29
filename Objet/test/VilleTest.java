import org.junit.Test;

import fr.ulille.but.sae_s2_2026.ModaliteTransport;

import static org.junit.Assert.*;

public class VilleTest {

    @Test
    public void testGetNom() {
        Arret v = new Arret("Paris", ModaliteTransport.TRAIN);
        assertEquals("Paris", v.getNom());
    }

    @Test
    public void testGetType() {
        Arret v = new Arret("Paris", ModaliteTransport.TRAIN);
        assertEquals(ModaliteTransport.TRAIN, v.getType());
    }

    @Test
    public void testToString() {
        Arret v = new Arret("Paris", ModaliteTransport.TRAIN);
        assertEquals("Paris (TRAIN)", v.toString());
    }

    @Test
    public void testEqualsMemeNom() {
        Arret v1 = new Arret("Paris", ModaliteTransport.TRAIN);
        Arret v2 = new Arret("Paris", ModaliteTransport.AVION);
        assertFalse(v1.equals(v2));
    }
    @Test
    public void testEqualsCasseInsensible() {
        Arret v1 = new Arret("paris", ModaliteTransport.TRAIN);
        Arret v2 = new Arret("PARIS", ModaliteTransport.TRAIN);
        assertTrue(v1.equals(v2));
    }

    @Test
    public void testEqualsNomDifferent() {
        Arret v1 = new Arret("Paris", ModaliteTransport.TRAIN);
        Arret v2 = new Arret("Lyon", ModaliteTransport.TRAIN);
        assertFalse(v1.equals(v2));
    }

    @Test
    public void testEqualsSoiMeme() {
        Arret v = new Arret("Paris", ModaliteTransport.TRAIN);
        assertTrue(v.equals(v));
    }

    @Test
    public void testEqualsAutreType() {
        Arret v = new Arret("Paris", ModaliteTransport.TRAIN);
        assertFalse(v.equals("Paris"));
    }
}
