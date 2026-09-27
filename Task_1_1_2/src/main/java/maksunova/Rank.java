package maksunova;

/**
 * all possible ranking of the card.
 */
public enum Rank {
    TWO("Двойка", 2),
    THREE("Тройка" , 3),
    FOUR("Четверка", 4),
    FIVE("Пятерка", 5),
    SIX("Шестерка", 6),
    SEVEN("Семерка", 7),
    EIGHT("Восьмерка", 8),
    NINE("Девятка", 9),
    TEN("Десятка", 10),
    JACK("валет", 10),
    QUEEN("королева", 10),
    KING("король", 10),
    ACE("Туз", 11);

    private int value;
    private String name;

    Rank(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public int getScore() {
        return value;
    }
}
