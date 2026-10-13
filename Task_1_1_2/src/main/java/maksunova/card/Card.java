package maksunova.card;

/**
 * Represents a playing card with a rank and a suit.
 */
public class Card {

    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    /**
     * return the correct card name.
     */
    public String name() {
        if (rank == Rank.JACK || rank == Rank.KING) {
            return suit.getNameM() + " " + rank.getName();
        }

        if (rank == Rank.QUEEN) {
            return suit.getNameF() + " " + rank.getName();
        }

        return rank.getName() + " " + suit.getNameB();
    }


}
