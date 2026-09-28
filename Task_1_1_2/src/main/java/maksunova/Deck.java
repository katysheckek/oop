package maksunova;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * the 52 cards deck after permutations.
 */
public class Deck {

    private final List<Card> cards;

    /**
     * create and shuffle new deck.
     */
    public Deck() {
        cards = new ArrayList<>();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }

        Collections.shuffle(cards);
    }

    /**
     * pulls out a card from the top.
     *
     * @return pulled out card
     * @throws IllegalStateException if no cards in the deck
     */
    public Card pullout() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Пустая колода");
        }

        return cards.removeLast();
    }

    /**
     * how many cards in the deck right now.
     */
    public int size() {
        return cards.size();
    }
}