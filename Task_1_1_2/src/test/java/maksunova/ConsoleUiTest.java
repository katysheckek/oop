package maksunova;

import java.io.ByteArrayOutputStream;
import maksunova.card.Card;
import maksunova.game.console.ConsoleUi;
import maksunova.hand.Dealer;
import maksunova.card.Rank;
import maksunova.card.Suit;
import maksunova.hand.Player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleUinterTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream captured;
    ConsoleUi consoleUi;

    @BeforeEach
    void setUp() {
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String output() {
        return captured.toString();
    }

    private static Player playerWith(Card... cards) {
        Player p = new Player();
        for (Card c : cards) {
            p.takeCard(c);
        }
        return p;
    }

    private static Dealer dealerWith(Card... cards) {
        Dealer d = new Dealer();
        for (Card c : cards) {
            d.takeCard(c);
        }
        return d;
    }

    private static Card card(Rank rank) {
        return new Card(rank, Suit.HEARTS);
    }

    @Test
    void showHandsHiddenDealer_printsPlayerHandAndScore() {
        Player player = playerWith(card(Rank.TEN), card(Rank.SEVEN));
        Dealer dealer = dealerWith(card(Rank.ACE), card(Rank.FIVE));

        consoleUi.showHandsHiddenDealer(player, dealer);

        String out = output();
        assertTrue(out.contains("Ваши карты:"));
        assertTrue(out.contains("17"));
    }

    @Test
    void showHandsHiddenDealer_hidesSecondDealerCard() {
        Player player = playerWith(card(Rank.TEN), card(Rank.SEVEN));
        Dealer dealer = dealerWith(card(Rank.ACE), card(Rank.FIVE));

        consoleUi.showHandsHiddenDealer(player, dealer);

        assertTrue(output().contains("<закрытая карта]"));
    }

    @Test
    void showHandsOpenDealer_printsBothScores() {
        Player player = playerWith(card(Rank.TEN), card(Rank.SEVEN));
        Dealer dealer = dealerWith(card(Rank.ACE), card(Rank.FIVE));

        consoleUi.showHandsOpenDealer(player, dealer);

        String out = output();
        assertTrue(out.contains("Ваши карты:"));
        assertTrue(out.contains("Карты дилера:"));
        assertTrue(out.contains("17"));
        assertTrue(out.contains("16"));
    }

    @Test
    void revealDealerCard_printsHiddenCard() {
        Player player = playerWith(card(Rank.TEN), card(Rank.SEVEN));
        Dealer dealer = dealerWith(card(Rank.ACE), card(Rank.FIVE));

        consoleUi.revealDealerCard(player, dealer);

        assertTrue(output().contains("Дилер открывает закрытую карту"));
    }

    @Test
    void showHandsOpenDealer_emptyPlayerHand_stillPrints() {
        Player player = playerWith();
        Dealer dealer = dealerWith(card(Rank.ACE), card(Rank.FIVE));


        consoleUi.showHandsOpenDealer(player, dealer);

        assertTrue(output().contains("[]"));
    }
}