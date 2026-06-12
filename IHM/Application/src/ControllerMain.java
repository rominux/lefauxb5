import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.ulille.but.sae_s2_2026.ModaliteTransport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ControllerMain {

    private Plateforme plateforme;
    private Historique historique;

    private Node draggedNode;

    private static final String[][] CRITERES = {
        {"Prix", "€", "200.0", "1.0"},
        {"Temps", "min", "120.0", "1.0"},
        {"Pollution", "kg CO₂e", "2.0", "0.1"}
    };

    @FXML private VBox vboxCriteres;
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

    @FXML
    public void initialize() {
        plateforme = new Plateforme();
        historique = new Historique();

        File fichierReseau = new File(Plateforme.FICHIER_RESEAU);
        File fichierCorrespondances = new File(Plateforme.FICHIER_CORRESPONDANCES);
        if (fichierReseau.exists()) plateforme.chargerReseau(fichierReseau);
        if (fichierCorrespondances.exists()) plateforme.chargerCorrespondances(fichierCorrespondances);

        List<String> villes = plateforme.getVilles();
        comboDepart.getItems().addAll(villes);
        comboArrivee.getItems().addAll(villes);

        initialiserListeCriteres();

        btnInverserVilles.setOnAction(this::inverserVilles);
        btnRechercher.setOnAction(this::rechercher);

        styliserToggle(tbBus);
        styliserToggle(tbTrain);
        styliserToggle(tbAvion);

        afficherHistorique();
    }

    private void initialiserListeCriteres() {
        for (String[] c : CRITERES) {
            vboxCriteres.getChildren().add(creerCarteCritere(c[0], c[1], Double.parseDouble(c[2]), Double.parseDouble(c[3])));
        }
        mettreAJourFleches();
    }

    private HBox creerCarteCritere(String titre, String unite, double valeurDefaut, double pas) {
        HBox carte = new HBox();
        carte.setStyle("-fx-border-color: #9ca3af; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-color: white; -fx-background-radius: 5;");
        carte.setAlignment(Pos.CENTER_LEFT);
        carte.setUserData(titre);
        carte.setMaxWidth(Double.MAX_VALUE);

        StackPane zoneGlisse = new StackPane();
        zoneGlisse.setStyle("-fx-background-color: #e5e7eb; -fx-background-radius: 4 0 0 4; -fx-cursor: hand;");
        zoneGlisse.setPrefWidth(30);
        zoneGlisse.setMaxHeight(Double.MAX_VALUE);

        Label iconeGlisse = new Label("⋮⋮");
        iconeGlisse.setStyle("-fx-font-size: 18px; -fx-text-fill: #6b7280;");
        zoneGlisse.getChildren().add(iconeGlisse);

        VBox contenu = new VBox(4);
        contenu.setPadding(new Insets(8, 8, 8, 10));

        Label lblTitre = new Label(titre);
        lblTitre.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #374151;");

        CheckBox chkLimite = new CheckBox("Limiter \u00e0:");
        chkLimite.setStyle("-fx-font-size: 11px;");

        HBox ligneValeur = new HBox(4);
        ligneValeur.setAlignment(Pos.CENTER_LEFT);

        Spinner<Double> spinner = new Spinner<>();
        SpinnerValueFactory<Double> factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 99999.0, valeurDefaut, pas);
        spinner.setValueFactory(factory);
        spinner.setEditable(true);
        spinner.setPrefWidth(70);
        HBox.setHgrow(spinner, Priority.ALWAYS);
        spinner.disableProperty().bind(chkLimite.selectedProperty().not());
        spinner.setOnScroll(ev -> {
            if (!spinner.isDisable()) {
                if (ev.getDeltaY() > 0) spinner.increment();
                else if (ev.getDeltaY() < 0) spinner.decrement();
            }
        });

        Label lblUnite = new Label(unite);
        lblUnite.setStyle("-fx-font-size: 11px; -fx-text-fill: #6b7280;");

        ligneValeur.getChildren().addAll(spinner, lblUnite);
        contenu.getChildren().addAll(lblTitre, chkLimite, ligneValeur);

        HBox.setHgrow(contenu, Priority.ALWAYS);

        VBox boutons = new VBox(4);
        boutons.setPadding(new Insets(0, 8, 0, 0));
        boutons.setAlignment(Pos.CENTER);

        Button btnHaut = new Button("\u2191");
        Button btnBas = new Button("\u2193");
        btnHaut.setPrefSize(28, 28);
        btnBas.setPrefSize(28, 28);
        String styleBtn = "-fx-cursor: hand; -fx-font-weight: bold; -fx-font-size: 11px; -fx-background-color: #f3f4f6; -fx-border-color: #d1d5db; -fx-border-radius: 3;";
        btnHaut.setStyle(styleBtn);
        btnBas.setStyle(styleBtn);

        btnHaut.setOnAction(e -> deplacerCarte(carte, -1));
        btnBas.setOnAction(e -> deplacerCarte(carte, 1));

        boutons.getChildren().addAll(btnHaut, btnBas);

        carte.getChildren().addAll(zoneGlisse, contenu, boutons);

        zoneGlisse.setOnDragDetected(ev -> {
            Dragboard db = zoneGlisse.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent cont = new ClipboardContent();
            cont.putString(titre);
            db.setContent(cont);
            draggedNode = carte;
            carte.setOpacity(0.4);
            ev.consume();
        });

        zoneGlisse.setOnDragDone(ev -> {
            carte.setOpacity(1.0);
            draggedNode = null;
        });

        carte.setOnDragOver(ev -> {
            if (ev.getGestureSource() != carte && ev.getDragboard().hasString()) {
                ev.acceptTransferModes(TransferMode.MOVE);
            }
            ev.consume();
        });

        carte.setOnDragDropped(ev -> {
            boolean ok = false;
            if (draggedNode != null && draggedNode != carte) {
                int idx = vboxCriteres.getChildren().indexOf(carte);
                vboxCriteres.getChildren().remove(draggedNode);
                vboxCriteres.getChildren().add(idx, draggedNode);
                ok = true;
                mettreAJourFleches();
            }
            ev.setDropCompleted(ok);
            ev.consume();
        });

        return carte;
    }

    private void deplacerCarte(Node carte, int direction) {
        int idx = vboxCriteres.getChildren().indexOf(carte);
        int cible = idx + direction;
        if (cible >= 0 && cible < vboxCriteres.getChildren().size()) {
            vboxCriteres.getChildren().remove(carte);
            vboxCriteres.getChildren().add(cible, carte);
            mettreAJourFleches();
        }
    }

    private void mettreAJourFleches() {
        int taille = vboxCriteres.getChildren().size();
        for (int i = 0; i < taille; i++) {
            HBox carte = (HBox) vboxCriteres.getChildren().get(i);
            VBox btns = (VBox) carte.getChildren().get(2);
            btns.getChildren().get(0).setVisible(i != 0);
            btns.getChildren().get(1).setVisible(i != taille - 1);
        }
    }

    private TypeCout typeDepuisTitre(String titre) {
        if (titre.equals("Prix")) return TypeCout.PRIX;
        if (titre.equals("Temps")) return TypeCout.TEMPS;
        return TypeCout.CO2;
    }

    private void styliserToggle(ToggleButton tb) {
        String vert = "-fx-background-color: #22c55e; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;";
        String rouge = "-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;";
        tb.setStyle(vert);
        tb.selectedProperty().addListener((obs, oldVal, newVal) ->
            tb.setStyle(newVal ? vert : rouge)
        );
    }

    @FXML
    private void inverserVilles(ActionEvent e) {
        String tmp = comboDepart.getValue();
        comboDepart.setValue(comboArrivee.getValue());
        comboArrivee.setValue(tmp);
    }

    @FXML
    private void rechercher(ActionEvent e) {
        lblErreur.setVisible(false);
        vboxResultats.getChildren().clear();

        try {
            String villeDepart = comboDepart.getValue();
            String villeArrivee = comboArrivee.getValue();

            if (villeDepart == null || villeArrivee == null) {
                lblErreur.setText("Veuillez sélectionner une ville de départ et une ville d'arrivée.");
                lblErreur.setVisible(true);
                return;
            }
            if (villeDepart.equals(villeArrivee)) {
                lblErreur.setText("Les villes de départ et d'arrivée doivent être différentes.");
                lblErreur.setVisible(true);
                return;
            }

            Set<ModaliteTransport> modes = new HashSet<>();
            if (tbBus.isSelected()) modes.add(ModaliteTransport.BUS);
            if (tbTrain.isSelected()) modes.add(ModaliteTransport.TRAIN);
            if (tbAvion.isSelected()) modes.add(ModaliteTransport.AVION);
            if (modes.isEmpty()) {
                lblErreur.setText("Sélectionnez au moins un mode de transport.");
                lblErreur.setVisible(true);
                return;
            }

            List<Node> cartes = vboxCriteres.getChildren();
            if (cartes.isEmpty()) {
                lblErreur.setText("Aucun critère défini.");
                lblErreur.setVisible(true);
                return;
            }

            String titrePrincipal = (String) ((HBox) cartes.get(0)).getUserData();
            TypeCout criterePrincipal = typeDepuisTitre(titrePrincipal);

            Map<TypeCout, Double> limites = new HashMap<>();
            for (Node node : cartes) {
                HBox carte = (HBox) node;
                String t = (String) carte.getUserData();
                TypeCout type = typeDepuisTitre(t);
                VBox contenu = (VBox) carte.getChildren().get(1);
                CheckBox chk = (CheckBox) contenu.getChildren().get(1);
                if (chk.isSelected()) {
                    HBox ligneVal = (HBox) contenu.getChildren().get(2);
                    Spinner<Double> sp = (Spinner<Double>) ligneVal.getChildren().get(0);
                    limites.put(type, sp.getValue());
                }
            }

            Arret depart = plateforme.creerArretVille(villeDepart, false);
            Arret arrivee = plateforme.creerArretVille(villeArrivee, true);

            Voyageur voyageur = new Voyageur("Utilisateur", criterePrincipal);
            List<Voyage> voyages = plateforme.comparerVoyages(depart, arrivee, voyageur, 20, limites, modes);

            afficherResultats(voyages);

        } catch (NoResultException ex) {
            lblErreur.setText("Aucun itinéraire trouvé entre ces villes.");
            lblErreur.setVisible(true);
        } catch (Exception ex) {
            lblErreur.setText("Erreur : " + ex.getMessage());
            lblErreur.setVisible(true);
            ex.printStackTrace();
        }
    }

    private void afficherResultats(List<Voyage> voyages) {
        double minCO2 = Double.MAX_VALUE;
        for (Voyage v : voyages) {
            double co2 = v.getCoutTotal(TypeCout.CO2);
            if (co2 < minCO2) minCO2 = co2;
        }
        double seuilFortImpact = Double.MAX_VALUE;
        if (voyages.size() >= 3) {
            seuilFortImpact = 2.0 * voyages.get(2).getCoutTotal(TypeCout.CO2);
        }

        for (Voyage voyage : voyages) {
            VBox carte = new VBox(6);
            carte.setStyle("-fx-border-color: #d1d5db; -fx-border-radius: 6; -fx-background-color: white; -fx-background-radius: 6; -fx-padding: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.06), 4, 0, 0, 2);");

            double co2 = voyage.getCoutTotal(TypeCout.CO2);
            boolean estLeMeilleur = co2 == minCO2;

            if (estLeMeilleur) {
                Label badge = new Label("\uD83C\uDF3F Choix le plus \u00e9cologique");
                badge.setStyle("-fx-background-color: #22c55e; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px; -fx-padding: 4 10; -fx-background-radius: 12;");
                badge.setMaxWidth(Double.MAX_VALUE);
                badge.setAlignment(Pos.CENTER);
                carte.getChildren().add(badge);
            }

            HBox ligneHaut = new HBox(15);
            ligneHaut.setAlignment(Pos.CENTER_LEFT);

            Label lblVilles = new Label(voyage.getVillesUniques());
            lblVilles.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #1f2937;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            String styleBoite = "-fx-background-color: #f3f4f6; -fx-padding: 4 8; -fx-background-radius: 6; -fx-border-color: #e5e7eb; -fx-border-radius: 6; -fx-font-size: 12px; -fx-text-fill: #374151; -fx-font-weight: bold;";
            String styleBoiteRouge = "-fx-background-color: #fef2f2; -fx-padding: 4 8; -fx-background-radius: 6; -fx-border-color: #fca5a5; -fx-border-radius: 6; -fx-font-size: 12px; -fx-text-fill: #dc2626; -fx-font-weight: bold;";

            Label lblPrix = new Label(String.format("%.2f", voyage.getCoutTotal(TypeCout.PRIX)) + " €");
            Label lblCo2 = new Label(String.format("%.2f", voyage.getCoutTotal(TypeCout.CO2)) + " kg");
            Label lblTemps = new Label((int) voyage.getCoutTotal(TypeCout.TEMPS) + " min");

            boolean alerteCO2 = co2 > seuilFortImpact;
            lblCo2.setStyle(alerteCO2 ? styleBoiteRouge : styleBoite);
            lblPrix.setStyle(styleBoite);
            lblTemps.setStyle(styleBoite);

            ligneHaut.getChildren().addAll(lblVilles, spacer, lblPrix, lblCo2, lblTemps);

            Label lblTrajets = new Label(voyage.formaterItineraire());
            lblTrajets.setStyle("-fx-font-size: 12px; -fx-text-fill: #6b7280;");
            lblTrajets.setWrapText(true);

            if (alerteCO2) {
                Label alerte = new Label("Fort impact carbone");
                alerte.setStyle("-fx-background-color: #fef2f2; -fx-text-fill: #dc2626; -fx-font-weight: bold; -fx-font-size: 11px; -fx-padding: 3 8; -fx-background-radius: 8; -fx-border-color: #fca5a5; -fx-border-radius: 8;");
                alerte.setMaxWidth(Double.MAX_VALUE);
                alerte.setAlignment(Pos.CENTER);
                carte.getChildren().add(alerte);
            }

            Button btnEnregistrer = new Button("Enregistrer");
            btnEnregistrer.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 4 12; -fx-background-radius: 4; -fx-cursor: hand;");

            Voyage voyageFinal = voyage;
            btnEnregistrer.setOnAction(ev -> {
                historique.ajouterVoyage(voyageFinal);
                historique.sauvegarder();
                afficherHistorique();
                btnEnregistrer.setDisable(true);
                btnEnregistrer.setText("Enregistré");
                btnEnregistrer.setStyle("-fx-background-color: #6b7280; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 4 12; -fx-background-radius: 4;");
            });

            HBox bas = new HBox();
            bas.setAlignment(Pos.CENTER_RIGHT);
            bas.getChildren().add(btnEnregistrer);

            carte.getChildren().addAll(ligneHaut, lblTrajets, bas);
            vboxResultats.getChildren().add(carte);
        }
    }

    private void afficherHistorique() {
        vboxHistorique.getChildren().clear();

        List<Voyage> voyages = historique.getVoyages();
        if (voyages == null || voyages.isEmpty()) {
            Label empty = new Label("Aucun trajet enregistré.");
            empty.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 12px;");
            vboxHistorique.getChildren().add(empty);
            return;
        }

        for (Voyage voyage : voyages) {
            VBox carte = new VBox(6);
            carte.setStyle("-fx-border-color: #d1d5db; -fx-border-radius: 4; -fx-background-color: white; -fx-background-radius: 4; -fx-padding: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.04), 2, 0, 0, 1);");

            List<Trajet> etapes = voyage.getEtapes();
            String texteVilles = "Voyage vide";
            if (etapes != null && !etapes.isEmpty()) {
                texteVilles = etapes.get(0).getDepart().getNom() + " - " + etapes.get(etapes.size() - 1).getArrivee().getNom();
            }

            Label lblVilles = new Label(texteVilles);
            lblVilles.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #1f2937;");
            lblVilles.setWrapText(true);
            lblVilles.setMaxWidth(Double.MAX_VALUE);
            lblVilles.setAlignment(Pos.CENTER);

            HBox ligneCouts = new HBox(8);
            ligneCouts.setAlignment(Pos.CENTER);

            String styleBoite = "-fx-background-color: #f3f4f6; -fx-padding: 3 6; -fx-background-radius: 4; -fx-border-color: #e5e7eb; -fx-border-radius: 4; -fx-font-size: 11px; -fx-text-fill: #4b5563; -fx-font-weight: bold;";

            Label lblPrix = new Label(String.format("%.2f", voyage.getCoutTotal(TypeCout.PRIX)) + " \u20AC");
            Label lblCo2 = new Label(String.format("%.2f", voyage.getCoutTotal(TypeCout.CO2)) + " kg");
            Label lblTemps = new Label((int) voyage.getCoutTotal(TypeCout.TEMPS) + " min");

            lblPrix.setStyle(styleBoite);
            lblCo2.setStyle(styleBoite);
            lblTemps.setStyle(styleBoite);

            lblPrix.setMaxWidth(Double.MAX_VALUE);
            lblCo2.setMaxWidth(Double.MAX_VALUE);
            lblTemps.setMaxWidth(Double.MAX_VALUE);

            lblPrix.setAlignment(Pos.CENTER);
            lblCo2.setAlignment(Pos.CENTER);
            lblTemps.setAlignment(Pos.CENTER);

            HBox.setHgrow(lblPrix, Priority.ALWAYS);
            HBox.setHgrow(lblCo2, Priority.ALWAYS);
            HBox.setHgrow(lblTemps, Priority.ALWAYS);

            ligneCouts.getChildren().addAll(lblPrix, lblCo2, lblTemps);

            carte.getChildren().addAll(lblVilles, ligneCouts);
            vboxHistorique.getChildren().add(carte);
        }
    }

}
