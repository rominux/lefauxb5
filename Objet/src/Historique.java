import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Historique {
    private ArrayList<Voyage> voyages;
    private static final String FICHIER_HISTORIQUE = "historique.sae"; // extension custom

    public Historique() {
        File fichier = new File(FICHIER_HISTORIQUE);
        if (fichier.exists()) {
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier))) {
                this.voyages = (ArrayList<Voyage>) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                this.voyages = new ArrayList<>();
            }
        } else {
            this.voyages = new ArrayList<>();
        }
    }

    public void ajouterVoyage(Voyage voyage) {
        this.voyages.add(voyage);
    }

    public void sauvegarder() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHIER_HISTORIQUE))) {
            oos.writeObject(this.voyages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Voyage v : voyages) {
            sb.append(v.toStringHistorique()).append("\n");
        }
        return sb.toString();
    }
}
