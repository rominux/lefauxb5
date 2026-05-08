import org.junit.Test;
import static org.junit.Assert.*;

public class VoyageurTest {

    @Test
    public void testGetNom() {
        Voyageur v = new Voyageur("Alice", TypeCout.PRIX);
        assertEquals("Alice", v.getNom());
    }

    @Test
    public void testGetCritere() {
        Voyageur v = new Voyageur("Alice", TypeCout.PRIX);
        assertEquals(TypeCout.PRIX, v.getCritere());
    }

    @Test
    public void testSetNom() {
        Voyageur v = new Voyageur("Alice", TypeCout.PRIX);
        v.setNom("Bob");
        assertEquals("Bob", v.getNom());
    }

    @Test
    public void testSetCritere() {
        Voyageur v = new Voyageur("Alice", TypeCout.PRIX);
        v.setCritere(TypeCout.CO2);
        assertEquals(TypeCout.CO2, v.getCritere());
    }

    @Test
    public void testToString() {
        Voyageur v = new Voyageur("Alice", TypeCout.PRIX);
        String resultat = v.toString();
        assertTrue(resultat.contains("Alice"));
        assertTrue(resultat.contains("PRIX"));
    }
}
