import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PawnTest {
    private Board board;

    @BeforeEach
    public void setup(){
        board = new Board();
    }

    @Test
    @DisplayName("백폰이 흑폰을 대각선으로 잡는지 테스트")
    public void testcase1(){
        //given
        board.init();
        boolean move1 = board.move(BoardTest.position(2, 2), BoardTest.position(2, 3));
        boolean move2 = board.move(BoardTest.position(2, 3), BoardTest.position(2, 4));
        boolean move3 = board.move(BoardTest.position(2, 4), BoardTest.position(2, 5));
        board.move(BoardTest.position(2, 5), BoardTest.position(2, 6));
        //when
        boolean move = board.move(BoardTest.position(1, 7), BoardTest.position(2, 6));
        //then
        List<List<Piece>> display = board.display();
        Assertions.assertThat(move).isTrue();
        Assertions.assertThat(display.get(5).get(1)).isEqualTo(PieceFactory.pawn(Color.WHITE, Position.create("B6")));
    }

    @Test
    @DisplayName("백폰이 빈곳에 대각선으로 이동이 안되는지 테스트")
    public void testcase2(){
        //given
        board.init();
        //when
        boolean move = board.move(BoardTest.position(1, 7), BoardTest.position(2, 6));
        //then
        List<List<Piece>> display = board.display();
        Assertions.assertThat(display.get(6).get(0)).isEqualTo(PieceFactory.pawn(Color.WHITE, Position.create("A7")));
        Assertions.assertThat(move).isFalse();
    }

}
