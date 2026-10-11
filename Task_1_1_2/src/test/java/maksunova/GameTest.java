package maksunova;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import maksunova.game.console.ConsoleInteraction;
import maksunova.game.logic.Game;
import org.junit.jupiter.api.Test;

class GameTest {
    /**
     * additional fake console.
     */
    private static class FakeConsole extends ConsoleInteraction {
        private final boolean[] actions;
        private final boolean[] newGames;
        private int actionIdx = 0;
        private int newGameIdx = 0;

        FakeConsole(boolean[] actions, boolean[] newGames) {
            this.actions = actions;
            this.newGames = newGames;
        }

        @Override
        public boolean askAction() {
            return actions[actionIdx++];
        }

        @Override
        public boolean askNewGame() {
            return newGames[newGameIdx++];
        }
    }

    @Test
    void start_playerStopsImmediately_endsAfterOneRound() {
        FakeConsole console = new FakeConsole(
                new boolean[]{false},
                new boolean[]{false}
        );

        Game game = new Game(console);

        assertDoesNotThrow(game::start);
    }

    @Test
    void start_playerHitsSeveralTimes() {
        FakeConsole console = new FakeConsole(
                new boolean[]{true, true, true, true, true, true, false},
                new boolean[]{false}
        );

        Game game = new Game(console);

        assertDoesNotThrow(game::start);
    }

    @Test
    void start_twoRounds() {
        FakeConsole console = new FakeConsole(
                new boolean[]{false, false},
                new boolean[]{true, false}
        );

        Game game = new Game(console);

        assertDoesNotThrow(game::start);
    }
}