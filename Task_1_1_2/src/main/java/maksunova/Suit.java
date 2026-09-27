package maksunova;

/**
 * all 4 suits the card can hold.
 */
public enum Suit {
    HEARTS("Червы", "Червовый", "Червовая"),
    CLUBS("Трефы", "Трефовый", "Трефовая"),
    SPADES("Пики", "Пиковый", "Пиковая"),
    DIAMONDS("Бубны", "Бубновый", "Бубновая");

    public final String basename;
    public final String malename;
    public final String femalename;

    Suit(String basename, String malename, String femalename) {
        this.basename = basename;
        this.malename = malename;
        this.femalename = femalename;
    }

    public String getName_b() {
        return basename;
    }
    public String getName_m() {
        return malename;
    }
    public String getName_f() {
        return femalename;
    }
}
