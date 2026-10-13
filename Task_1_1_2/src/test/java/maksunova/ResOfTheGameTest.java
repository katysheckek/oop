package maksunova;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import maksunova.game.ResOfTheGame;
import org.junit.jupiter.api.Test;

class ResOfTheGameTest {

    @Test
    void values_containsAll() {
        assertArrayEquals(
                new ResOfTheGame[]{ResOfTheGame.WIN, ResOfTheGame.DRAW, ResOfTheGame.LOSE, ResOfTheGame.UNDEF},
                ResOfTheGame.values()
        );
    }

    @Test
    void valueOf_works() {
        assertEquals(ResOfTheGame.WIN, ResOfTheGame.valueOf("WIN"));
        assertEquals(ResOfTheGame.UNDEF, ResOfTheGame.valueOf("UNDEF"));
    }
}
