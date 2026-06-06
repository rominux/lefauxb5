import org.junit.Test;
import fr.ulille.but.sae_s2_2026.ModaliteTransport;
import static org.junit.Assert.*;

public class ArretTest {

    @Test
    public void testConstructeurEtGetters() {
        Arret arret = new Arret("Lille-Europe", ModaliteTransport.TRAIN);
        assertEquals("Lille-Europe", arret.getNom());
        assertEquals(ModaliteTransport.TRAIN, arret.getType());
    }

    @Test
    public void testToString() {
        Arret arret = new Arret("Gare de l'Est", ModaliteTransport.TRAIN);
        assertEquals("Gare de l'Est (TRAIN)", arret.toString());
    }

    @Test
    public void testEqualsEtHashCode() {
        Arret arret1 = new Arret("Lille", ModaliteTransport.TRAIN);
        Arret arret2 = new Arret("Lille", ModaliteTransport.TRAIN);
        Arret arretDiffNom = new Arret("Paris", ModaliteTransport.TRAIN);
        Arret arretDiffType = new Arret("Lille", ModaliteTransport.AVION);

        // Test de l'égalité réflexive et symétrique
        assertEquals(arret1, arret2);
        assertEquals(arret1.hashCode(), arret2.hashCode());

        // Test des différences
        assertNotEquals(arret1, arretDiffNom);
        assertNotEquals(arret1, arretDiffType);
        assertNotEquals(null, arret1);
        assertNotEquals("Une Chaîne de caractères", arret1);
    }
}