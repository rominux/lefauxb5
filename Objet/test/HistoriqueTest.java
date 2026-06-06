import org.junit.Test;

import fr.ulille.but.sae_s2_2026.ModaliteTransport;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;

public class HistoriqueTest {

    @Test
    public void testSauvegarderEtChargerHistorique() {
        File fichier = new File("historique.sae");
        // ensure clean state
        if (fichier.exists()) {
            fichier.delete();
        }

        try {
            Arret a = new Arret("A", ModaliteTransport.TRAIN);
            Arret b = new Arret("B", ModaliteTransport.TRAIN);

            Trajet t1 = new Trajet(a, b, ModaliteTransport.TRAIN, new Cout(20.0, 3.0, 10.0));
            Trajet t2 = new Trajet(b, a, ModaliteTransport.TRAIN, new Cout(30.0, 6.0, 5.0));

            Voyage v1 = new Voyage(Arrays.asList(t1));
            Voyage v2 = new Voyage(Arrays.asList(t2));

            Historique h = new Historique();
            h.ajouterVoyage(v1);
            h.ajouterVoyage(v2);
            String before = h.toString();

            h.sauvegarder();

            Historique loaded = new Historique();
            String after = loaded.toString();

            assertEquals("Historique rechargé doit correspondre à l'original", before, after);
        } finally {
            if (fichier.exists()) {
                fichier.delete();
            }
        }
    }
}
