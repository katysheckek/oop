package maksunova.hand;

import maksunova.card.Card;

/**
 * Player hand realization.
 */
public class Player {
    public final Hand hand = new Hand();

    public Hand getHand() {
        return hand;
    }

    public void takeCard(Card card) {
        hand.takeCard(card);
    }

}
