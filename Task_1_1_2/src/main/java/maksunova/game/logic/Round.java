package maksunova.game.logic;

import maksunova.card.Card;
import maksunova.game.Deck;
import maksunova.game.ResOfTheGame;
import maksunova.hand.Dealer;
import maksunova.hand.Player;

/**
 * one round frame.
 */
public class Round {

    private final Deck deck;
    private final Player player;
    private final Dealer dealer;


    /**
     * round constructor.
     *
     * @param deck    deck
     * @param player  player
     * @param dealer  dealer
     */
    public Round(Deck deck, Player player, Dealer dealer) {
        this.deck = deck;
        this.player = player;
        this.dealer = dealer;
        giveCards();
    }

    public Deck getDeck() { return this.deck; }

    /**
     * Player move: take card from the deck.
     * @return taken card
     */
    public Card playerHit() {
        Card card = deck.pullout();
        player.takeCard(card);
        return card;
    }

    /**
     * Dealer move: take cards, while score less than 17.
     */
    public void dealerTurn() {
        while (dealer.shouldTakeCard()) {
            dealer.takeCard(deck.pullout());
        }
    }

    /**
     * chrck instant blackjack.
     */
    public ResOfTheGame checkInitialBlackjack() {
        if (player.getHand().isBlackjack() && dealer.getHand().isBlackjack()) {
            return ResOfTheGame.DRAW;
        }
        if (player.getHand().isBlackjack()) {
            return ResOfTheGame.WIN;
        }
        if (dealer.getHand().isBlackjack()) {
            return ResOfTheGame.LOSE;

        }
        return ResOfTheGame.UNDEF;
    }

    /**
     * initial deal of cards.
     */
    private void giveCards() {
        player.takeCard(deck.pullout());
        player.takeCard(deck.pullout());

        dealer.takeCard(deck.pullout());
        dealer.takeCard(deck.pullout());
    }

    /**
     * Decide winner of the round
     */
    public ResOfTheGame evaluateResult() {
        if (player.getHand().isOver21()) {
            return ResOfTheGame.LOSE;
        }
        if (dealer.getHand().isOver21()) {
            return ResOfTheGame.WIN;
        }

        int playerScore = player.getHand().getScore();
        int dealerScore = dealer.getHand().getScore();

        if (playerScore > dealerScore) {
            return ResOfTheGame.WIN;
        } else if (playerScore < dealerScore) {
            return ResOfTheGame.LOSE;
        } else {
            return ResOfTheGame.DRAW;
        }
    }
}

