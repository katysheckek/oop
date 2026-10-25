package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import maksunova.card.Card;
import maksunova.card.Rank;
import maksunova.card.Suit;
import maksunova.game.logic.Round;
import maksunova.game.Deck;
import maksunova.game.ResOfTheGame;
import maksunova.hand.Dealer;
import maksunova.hand.Player;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class RoundTest {

    private static Deck deck(Card... cards) {
        return new Deck(List.of(cards));
    }

    private static Card card(Rank rank) {
        return new Card(rank, Suit.HEARTS);
    }

    @Test
    void constructor_dealsTwoCardsEach() {
        Deck d = deck(
                card(Rank.TWO), card(Rank.THREE),
                card(Rank.FOUR), card(Rank.FIVE)
        );
        Player player = new Player();
        Dealer dealer = new Dealer();

        new Round(d, player, dealer);

        // FIVE, FOUR, THREE, TWO`
        // player: TWO, THREE ; dealer: FOUR, FIVE
        assertEquals(2, player.getHand().getCards().size());
        assertEquals(2, dealer.getHand().getCards().size());
        assertEquals(0, d.size());
    }

    @Test
    void playerHitPullsCardAndGivesToPlayer() {

        List<Card> list = new ArrayList<>(List.of(
                card(Rank.TWO), card(Rank.THREE),
                card(Rank.FOUR), card(Rank.FIVE), card(Rank.SIX)
        ));
        Deck d = new Deck(
                list
        );
        Player player = new Player();
        Dealer dealer = new Dealer();
        Round round = new Round(d, player, dealer);

        Card taken = round.playerHit();

        assertEquals(d, round.getDeck());
        assertEquals(Rank.TWO, taken.getRank());

        assertEquals(3, player.getHand().getCards().size());
        assertTrue(player.getHand().getCards().contains(taken));
    }

    @Test
    void dealerTurnStopsAt17() {
        Deck d = deck(
                card(Rank.TWO),
                card(Rank.SIX),
                card(Rank.THREE),
                card(Rank.TEN),   // 16+2=18, стоп
                card(Rank.FOUR),
                card(Rank.TWO)
        );
        Player player = new Player();
        Dealer dealer = new Dealer();
        Round round = new Round(d, player, dealer);

        round.dealerTurn();

        assertTrue(dealer.getHand().getScore() >= 17);
    }

    @Test
    void checkInitialBlackjackPlayerWins() {
        // player: A + 10 = 21 ; dealer: 10 + 9 = 19
        Deck d = deck(
                card(Rank.NINE),
                card(Rank.TEN),
                card(Rank.TEN),
                card(Rank.ACE)
        );
        Player player = new Player();
        Dealer dealer = new Dealer();
        Round round = new Round(d, player, dealer);

        assertEquals(ResOfTheGame.WIN, round.checkInitialBlackjack());
    }

    @Test
    void checkInitialBlackjackDealerWins() {
        // player: 10 + 9 = 19 ; dealer: A + 10 = 21
        Deck d = deck(
                card(Rank.TEN),
                card(Rank.ACE),
                card(Rank.NINE),
                card(Rank.TEN)
        );
        Player player = new Player();
        Dealer dealer = new Dealer();
        Round round = new Round(d, player, dealer);

        assertEquals(ResOfTheGame.LOSE, round.checkInitialBlackjack());
    }

    @Test
    void checkInitialBlackjackUndefined() {
        Deck d = deck(
                card(Rank.TEN), card(Rank.NINE),
                card(Rank.EIGHT), card(Rank.SEVEN)
        );
        Round round = new Round(d, new Player(), new Dealer());

        assertEquals(ResOfTheGame.UNDEF, round.checkInitialBlackjack());
    }

    @Test
    void evaluateResultPlayerOver21Lose() {
        // player: 10 + 10 + 10 = 30 ; dealer: 10 + 9 = 19
        Deck d = deck(
                card(Rank.NINE),
                card(Rank.TEN),
                card(Rank.TEN),
                card(Rank.TEN),
                card(Rank.TEN)
        );
        Player player = new Player();
        Dealer dealer = new Dealer();
        Round round = new Round(d, player, dealer);
        round.playerHit();

        assertEquals(ResOfTheGame.LOSE, round.evaluateResult());
    }

    @Test
    void evaluateResultDealerOver21Win() {
        // player: 10 + 10 = 20 ; dealer: 10 + 9 = 19, потом доберёт 5 -> 24
        Deck d = deck(
                card(Rank.FIVE),
                card(Rank.NINE),
                card(Rank.TEN),
                card(Rank.TEN),
                card(Rank.TEN)
        );
        Player player = new Player();
        Dealer dealer = new Dealer();
        Round round = new Round(d, player, dealer);
        round.dealerTurn();

        assertEquals(ResOfTheGame.WIN, round.evaluateResult());
    }

    @Test
    void evaluateResultPlayerHigherWin() {
        // player: 10 + 9 = 19 ; dealer: 10 + 8 = 18
        Deck d = deck(
                card(Rank.EIGHT),
                card(Rank.TEN),
                card(Rank.NINE),
                card(Rank.TEN)
        );
        Round round = new Round(d, new Player(), new Dealer());

        assertEquals(ResOfTheGame.WIN, round.evaluateResult());
    }

    @Test
    void evaluateResultDealerHigherLose() {
        // player: 10 + 7 = 17 ; dealer: 10 + 9 = 19
        Deck d = deck(
                card(Rank.NINE),
                card(Rank.TEN),
                card(Rank.SEVEN),
                card(Rank.TEN)
        );
        Round round = new Round(d, new Player(), new Dealer());

        assertEquals(ResOfTheGame.LOSE, round.evaluateResult());
    }

    @Test
    void evaluateResultEqualDraw() {
        // player: 10 + 9 = 19 ; dealer: 10 + 9 = 19
        Deck d = deck(
                card(Rank.NINE),
                card(Rank.TEN),
                card(Rank.NINE),
                card(Rank.TEN)
        );
        Round round = new Round(d, new Player(), new Dealer());

        assertEquals(ResOfTheGame.DRAW, round.evaluateResult());
    }
}
