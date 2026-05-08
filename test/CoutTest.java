import org.junit.Test;
import static org.junit.Assert.*;

public class CoutTest {

    @Test
    public void testGetValeurTemps() {
        Cout cout = new Cout(120, 80, 4.1);
        assertEquals(120.0, cout.getValeur(TypeCout.TEMPS), 0.001);
    }

    @Test
    public void testGetValeurPrix() {
        Cout cout = new Cout(120, 80, 4.1);
        assertEquals(80.0, cout.getValeur(TypeCout.PRIX), 0.001);
    }

    @Test
    public void testGetValeurCO2() {
        Cout cout = new Cout(120, 80, 4.1);
        assertEquals(4.1, cout.getValeur(TypeCout.CO2), 0.001);
    }

    @Test
    public void testSetValeur() {
        Cout cout = new Cout(120, 80, 4.1);
        cout.setValeur(TypeCout.PRIX, 50.0);
        assertEquals(50.0, cout.getValeur(TypeCout.PRIX), 0.001);
    }

    @Test
    public void testToString() {
        Cout cout = new Cout(120.0, 80.0, 4.1);
        String resultat = cout.toString();
        assertTrue(resultat.contains("120"));
        assertTrue(resultat.contains("80"));
        assertTrue(resultat.contains("4.1"));
    }
}
