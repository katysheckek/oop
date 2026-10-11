package maksunova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import maksunova.card.Card;
import maksunova.game.logic.Round;
import maksunova.hand.Dealer;
import maksunova.game.Deck;
import maksunova.card.Rank;
import maksunova.card.Suit;
import maksunova.game.ResOfTheGame;
import maksunova.hand.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        // pullout идёт с конца: FIVE, FOUR, THREE, TWO
        // player: FIVE, THREE ; dealer: FOUR, TWO
        assertEquals(2, player.getHand().getCards().size());
        assertEquals(2, dealer.getHand().getCards().size());
        assertEquals(0, d.size());
    }

    @Test
    void playerHit_pullsCardAndGivesToPlayer() {
        Deck d = deck(
                card(Rank.TWO), card(Rank.THREE),
                card(Rank.FOUR), card(Rank.FIVE),
                card(Rank.SIX)
        );
        Player player = new Player();
        Dealer dealer = new Dealer();
        Round round = new Round(d, player, dealer);

        Card taken = round.playerHit();

        assertEquals(Rank.SIX, taken.getRank());
        assertEquals(3, player.getHand().getCards().size());
        assertTrue(player.getHand().getCards().contains(taken));
    }

    @Test
    void dealerTurn_stopsAt17() {
        // Дилеру раздаём 10 + 6 = 16, потом вытянет 2 -> 18 и остановится
        // Порядок pullout: с конца списка
        // giveCards: player<-e5, dealer<-e4, player<-e3, dealer<-e2
        // Возьмём так, чтобы у дилера были 10 и 6:
        // player: e5, e3 ; dealer: e4, e2
        Deck d = deck(
                card(Rank.TWO),   // e1 -> достанется дилеру последним -> нужно 6
                card(Rank.SIX),   // e2 -> дилеру первым
                card(Rank.THREE), // e3 -> игроку вторым
                card(Rank.TEN),   // e4 -> дилеру вторым
                card(Rank.FOUR),  // e5 -> игроку первым
                card(Rank.TWO)    // e6 -> дилеру в dealerTurn (16+2=18, стоп)
        );
        Player player = new Player();
        Dealer dealer = new Dealer();
        Round round = new Round(d, player, dealer);

        round.dealerTurn();

        assertTrue(dealer.getHand().getScore() >= 17);
    }

    @Test
    void checkInitialBlackjack_playerWins() {
        // player: A + 10 = 21 ; dealer: 10 + 9 = 19
        Deck d = deck(
                card(Rank.NINE),   // e1 -> дилеру вторым
                card(Rank.TEN),    // e2 -> дилеру первым
                card(Rank.TEN),    // e3 -> игроку вторым
                card(Rank.ACE)     // e4 -> игроку первым
        );
        Player player = new Player();
        Dealer dealer = new Dealer();
        Round round = new Round(d, player, dealer);

        assertEquals(ResOfTheGame.WIN, round.checkInitialBlackjack());
    }

    @Test
    void checkInitialBlackjack_dealerWins() {
        // player: 10 + 9 = 19 ; dealer: A + 10 = 21
        Deck d = deck(
                card(Rank.TEN),    // e1 -> дилеру вторым
                card(Rank.ACE),    // e2 -> дилеру первым
                card(Rank.NINE),   // e3 -> игроку вторым
                card(Rank.TEN)     // e4 -> игроку первым
        );
        Player player = new Player();
        Dealer dealer = new Dealer();
        Round round = new Round(d, player, dealer);

        assertEquals(ResOfTheGame.LOSE, round.checkInitialBlackjack());
    }

    @Test
    void checkInitialBlackjack_undefined() {
        Deck d = deck(
                card(Rank.TEN), card(Rank.NINE),
                card(Rank.EIGHT), card(Rank.SEVEN)
        );
        Round round = new Round(d, new Player(), new Dealer());

        assertEquals(ResOfTheGame.UNDEF, round.checkInitialBlackjack());
    }

    @Test
    void evaluateResult_playerOver21_lose() {
        // player: 10 + 10 + 10 = 30 ; dealer: 10 + 9 = 19
        Deck d = deck(
                card(Rank.NINE),  // дилеру вторым
                card(Rank.TEN),   // дилеру первым
                card(Rank.TEN),   // игроку вторым
                card(Rank.TEN),   // игроку первым
                card(Rank.TEN)    // игроку в playerHit -> 30
        );
        Player player = new Player();
        Dealer dealer = new Dealer();
        Round round = new Round(d, player, dealer);
        round.playerHit();

        assertEquals(ResOfTheGame.LOSE, round.evaluateResult());
    }

    @Test
    void evaluateResult_dealerOver21_win() {
        // player: 10 + 10 = 20 ; dealer: 10 + 9 = 19, потом доберёт 5 -> 24
        Deck d = deck(
                card(Rank.FIVE),  // дилеру в dealerTurn
                card(Rank.NINE),  // дилеру вторым
                card(Rank.TEN),   // дилеру первым
                card(Rank.TEN),   // игроку вторым
                card(Rank.TEN)    // игроку первым
        );
        Player player = new Player();
        Dealer dealer = new Dealer();
        Round round = new Round(d, player, dealer);
        round.dealerTurn();

        assertEquals(ResOfTheGame.WIN, round.evaluateResult());
    }

    @Test
    void evaluateResult_playerHigher_win() {
        // player: 10 + 9 = 19 ; dealer: 10 + 8 = 18
        Deck d = deck(
                card(Rank.EIGHT), // дилеру вторым
                card(Rank.TEN),   // дилеру первым
                card(Rank.NINE),  // игроку вторым
                card(Rank.TEN)    // игроку первым
        );
        Round round = new Round(d, new Player(), new Dealer());

        assertEquals(ResOfTheGame.WIN, round.evaluateResult());
    }

    @Test
    void evaluateResult_dealerHigher_lose() {
        // player: 10 + 7 = 17 ; dealer: 10 + 9 = 19
        Deck d = deck(
                card(Rank.NINE),  // дилеру вторым
                card(Rank.TEN),   // дилеру первым
                card(Rank.SEVEN), // игроку вторым
                card(Rank.TEN)    // игроку первым
        );
        Round round = new Round(d, new Player(), new Dealer());

        assertEquals(ResOfTheGame.LOSE, round.evaluateResult());
    }

    @Test
    void evaluateResult_equal_draw() {
        // player: 10 + 9 = 19 ; dealer: 10 + 9 = 19
        Deck d = deck(
                card(Rank.NINE),  // дилеру вторым
                card(Rank.TEN),   // дилеру первым
                card(Rank.NINE),  // игроку вторым
                card(Rank.TEN)    // игроку первым
        );
        Round round = new Round(d, new Player(), new Dealer());

        assertEquals(ResOfTheGame.DRAW, round.evaluateResult());
    }
}
