import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GameTest {
    private Game game;

    @BeforeEach
    public void setup(){
        this.game = new Game();
    }

    @Test
    public void displayTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        initMethod();
        //when
        displayMethod();
        //then
    }

    private void initMethod() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method field = Game.class.getDeclaredMethod("init");
        field.setAccessible(true);
        field.invoke(game);
    }

    private void displayMethod() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method field;
        field = Game.class.getDeclaredMethod("display");
        field.setAccessible(true);
        field.invoke(game);
    }

    private boolean checkWin(Player player) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method field = Game.class.getDeclaredMethod("checkWin", Player.class);
        field.setAccessible(true);
        return (boolean) field.invoke(game, player);
    }

    private Board board() throws NoSuchFieldException, IllegalAccessException {
        Field field = Game.class.getDeclaredField("board");
        field.setAccessible(true);
        return (Board) field.get(game);
    }

    private Player whitePlayer() throws IllegalAccessException, NoSuchFieldException {
        Field field = Game.class.getDeclaredField("whitePlayer");
        field.setAccessible(true);
        return (Player) field.get(game);
    }

    private Player blackPlayer() throws NoSuchFieldException, IllegalAccessException {
        Field field = Game.class.getDeclaredField("blackPlayer");
        field.setAccessible(true);
        return (Player) field.get(game);
    }
}