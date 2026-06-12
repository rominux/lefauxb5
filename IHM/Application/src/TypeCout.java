public enum TypeCout {
    TEMPS,
    PRIX,
    CO2;

    public static String uniteDepuis(String critere) {
        if (critere == null) return "";
        if (critere.contains("min")) return "min";
        if (critere.contains("CO2e")) return "kg CO2e";
        if (critere.contains("\u20AC")) return "\u20AC";
        return "";
    }
}