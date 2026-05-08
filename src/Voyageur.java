public class Voyageur {
 
    private String nom;
    private TypeCout critere;
 
    public Voyageur(String nom, TypeCout critere) {
        this.nom = nom;
        this.critere = critere;
    }
 
    public String getNom() {
        return this.nom;
    }
 
    public void setNom(String nom) {
        this.nom = nom;
    }
 
    public TypeCout getCritere() {
        return this.critere;
    }
 
    public void setCritere(TypeCout critere) {
        this.critere = critere;
    }
 
    @Override
    public String toString() {
        return "Voyageur : " + this.nom + " | Critère : " +this.critere;
    }

}