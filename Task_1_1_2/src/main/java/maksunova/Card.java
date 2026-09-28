package maksunova;

/**
 *
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
            return suit.getName_m() + " " + rank.getName();
        }

        if (rank == Rank.QUEEN) {
            return suit.getName_f() + " " + rank.getName();
        }

        return rank.getName() + " " + suit.getName_b();
    }


}
