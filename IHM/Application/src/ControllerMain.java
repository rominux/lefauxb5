import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

public class ControllerMain {

    @FXML private VBox vboxCriteres;
    @FXML private ComboBox<String> comboCritere1;
    @FXML private ComboBox<String> comboCritere2;
    @FXML private ComboBox<String> comboCritere3;
    @FXML private TextField txtMaxCritere1;
    @FXML private TextField txtMaxCritere2;
    @FXML private TextField txtMaxCritere3;
    @FXML private Label lblUnite1;
    @FXML private Label lblUnite2;
    @FXML private Label lblUnite3;
    @FXML private ComboBox<String> comboDepart;
    @FXML private ComboBox<String> comboArrivee;
    @FXML private Button btnInverserVilles;
    @FXML private ToggleButton tbBus;
    @FXML private ToggleButton tbTrain;
    @FXML private ToggleButton tbAvion;
    @FXML private Button btnRechercher;
    @FXML private Label lblErreur;
    @FXML private VBox vboxResultats;
    @FXML private VBox vboxHistorique;

    private Plateforme plateforme;

    @FXML
    public void initialize() {
        plateforme = new Plateforme();

        String[] criteres = {"temps (min)", "impact (kg CO2e)", "prix (€)"};
        comboCritere1.getItems().addAll(criteres);
        comboCritere2.getItems().addAll(criteres);
        comboCritere3.getItems().addAll(criteres);
    }
}
