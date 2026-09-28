package maksunova;

import maksunova.card.Card;

/**
 * Dealer.
 */
public class Dealer {
    public final Hand hand = new Hand();

    public Hand seeHand() {
        return hand;
    }

    public void takeCard(Card card) {
        hand.takeCard(card);
    }

    /**
     * decide, should the diller take another card.
     *
     * @return true, if score less than 17
     */
    public boolean shouldTakeCard() {
        return hand.seeScore() < 17;
    }
}
