import java.util.HashMap;
import java.util.Map;
 
public class Cout {
 
    private Map<TypeCout, Double> valeurs;
 
    public Cout(double temps, double prix, double co2) {
        valeurs = new HashMap<>();
        valeurs.put(TypeCout.TEMPS, temps);
        valeurs.put(TypeCout.PRIX, prix);
        valeurs.put(TypeCout.CO2, co2);
    }
 
    public double getValeur(TypeCout type) {
        return valeurs.get(type);
    }
 
    public void setValeur(TypeCout type, double valeur) {
        valeurs.put(type, valeur);
    }
 
    @Override
    public String toString() {
        return valeurs.get(TypeCout.TEMPS) + " min | " + valeurs.get(TypeCout.CO2) + " kg CO2e | " + valeurs.get(TypeCout.PRIX) + " €";
    }
}