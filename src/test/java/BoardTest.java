import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class BoardTest {
    private Board board;

    @BeforeEach
    public void setup(){
        board = new Board();
    }

    @Test
    @DisplayName("흑/백 기물들이 잘 초기화되는지 테스트")
    public void init() throws NoSuchFieldException, IllegalAccessException {
        //given
        List<Piece> whitePawnsExpected = Arrays.stream(File.values())
                                               .filter(f->f != File.INVALID)
                                               .map(f -> PieceFactory.pawn(Color.WHITE, position(f.value(),7)))
                                               .collect(Collectors.toList());
        List<Piece> blackPawnsExpected = Arrays.stream(File.values())
                                                .filter(f->f != File.INVALID)
                                                .map(f->PieceFactory.pawn(Color.BLACK, position(f.value(), 2)))
                                                .collect(Collectors.toList());
        //when
        board.init();
        //then
        List<Piece> whitePawns = filterPawns(pieceList(manager(pieces(board))), Color.WHITE);
        List<Piece> blackPawns = filterPawns(pieceList(manager(pieces(board))), Color.BLACK);

        assertThat(whitePawns.containsAll(whitePawnsExpected)).isTrue();
        assertThat(blackPawns.containsAll(blackPawnsExpected)).isTrue();
    }

    @Test
    @DisplayName("A7폰을 A6로 이동")
    public void move() throws NoSuchFieldException, IllegalAccessException {
        //given
        board.init();
        Position from = position(1, 7);
        Position to = position(1, 6);
        //when
        boolean actual = board.move(from, to);
        //then
        assertThat(actual).isTrue();
        Piece piece = manager(pieces(board)).find(to);
        assertThat(piece.lastPosition()).isEqualTo(to);
    }


    @Test
    @DisplayName("A7폰이 이동하여 A2폰을 잡는 테스트")
    public void move_RemovePiece() throws NoSuchFieldException, IllegalAccessException {
        //given
        board.init();
        Position from = position(1, 7);
        Position[] positions = {position(1, 6),
                                position(1, 5),
                                position(1, 4),
                                position(1, 3),
                                position(1, 2)};
        //when
        for(Position to : positions){
            board.move(from, to);
            from = to;
        }
        //then
        List<Piece> pieces = pieceList(manager(pieces(board)));
        List<Piece> blackPawns = filterPawns(pieces, Color.BLACK);
        assertThat(blackPawns.stream()
                            .noneMatch(pawn -> pawn.equals(PieceFactory.pawn(Color.BLACK, position(1, 2)))))
                            .isTrue();
    }

    @Test
    @DisplayName("흑백룩을 A1->A5로 이동하고자 할때 A2 흑백폰에 막히는 테스트")
    public void move_rook_fail(){
        //given
        board.init();
        //when
        boolean actual = board.move(position(1, 1), position(1, 5));
        //then
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("흑백폰 A2->A3->A4, 흑백룩 A1->A4 이동이 안되는지 테스트")
    public void move_rook_fail2(){
        //given
        board.init();
        board.move(position(1, 2), position(1, 3));
        board.move(position(1, 3), position(1, 4));
        //when
        boolean actual = board.move(position(1, 1), position(1, 4));
        //then
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("흑백폰 A2->A3->A4, 흑백룩 A1->A3 이동이 되는지 테스트")
    public void move_rook_success(){
        //given
        board.init();
        board.move(position(1, 2), position(1, 3));
        board.move(position(1, 3), position(1, 4));
        //when
        boolean actual = board.move(position(1, 1), position(1, 3));
        //then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("흑백폰 A2->A3->A4, 흑백폰 C2->C3를 하고 흑백룩 A1->A3->D3 이동이 안되는지 테스트")
    public void move_rook_fail3(){
        //given
        board.init();
        board.move(position(1, 2), position(1, 3));
        board.move(position(1, 3), position(1, 4));
        board.move(position(3, 2), position(3, 3));
        board.move(position(1, 1), position(1, 3));
        //when
        boolean actual = board.move(position(1, 3), position(4, 3));
        //then
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("C1->E3 비숍 이동되는지 테스트")
    public void move_bishop_success1(){
        //given
        // D2->D3, F2->F3->F4 폰이동
        board.init();
        board.move(position(4, 2), position(4, 3));
        board.move(position(6, 2), position(6, 3));
        board.move(position(6, 3), position(6, 4));
        //when
        boolean actual = board.move(position(3, 1), position(5, 3));
        //then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("C1->F4 비숍 이동이 안되는지 테스트")
    public void move_bishop_fail1(){
        //given
        // D2->D3, F2->F3->F4 폰이동
        board.init();
        board.move(position(4, 2), position(4, 3));
        board.move(position(6, 2), position(6, 3));
        board.move(position(6, 3), position(6, 4));
        //when
        boolean actual = board.move(position(3, 1), position(5, 4));
        //then
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("E1->H4->A4 이동되는 테스트")
    public void move_queen_success(){
        //given
        board.init();
        board.move(position(6, 2), position(6, 3));
        //when
        boolean actual = board.move(position(5, 1), position(8, 4));
        boolean actual2 = board.move(position(8, 4), position(1, 4));
        //then
        assertThat(actual).isTrue();
        assertThat(actual2).isTrue();
    }

    @Test
    @DisplayName("퀸 이동이 안되는지 테스트")
    public void move_queen_fail1(){
        //given
        board.init();

        //when
        boolean actual = board.move(position(5, 1), position(6, 1));
        boolean actual2 = board.move(position(5, 1), position(7, 1));
        boolean actual3 = board.move(position(5, 1), position(5, 3));
        boolean actual4 = board.move(position(5, 1), position(6, 2));
        boolean actual5 = board.move(position(5, 1), position(7, 3));

        //then
        assertThat(actual).isFalse();
        assertThat(actual2).isFalse();
        assertThat(actual3).isFalse();
        assertThat(actual4).isFalse();
        assertThat(actual5).isFalse();
    }

    @Test
    @DisplayName("나이트가 이동이 되는지 테스트")
    public void move_knight_success(){
        //given
        board.init();
        //when
        boolean actual = board.move(position(2, 1), position(1, 3));
        //then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("킹이 이동이 되는지 테스트")
    public void move_king_success(){
        //given
        board.init();
        board.move(position(4, 2), position(4,3));

        //when
        board.move(position(4, 1), position(4,2));
        boolean actual = board.move(position(4, 2), position(5, 3));
        //then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("킹이 이동할 수 있는 경로 확인 테스트")
    public void possiblePositions_king(){
        //given
        board.init();
        board.move(position(4, 2), position(4,3));

        //when
        board.move(position(4, 1), position(4,2));
        List<Position> positions = board.possiblePositions(position(4, 2));
        //then
        List<Position> expected = new ArrayList<>(List.of(
           position(4,1), position(3,3),
            position(5,3)
        ));
        Collections.sort(expected);
        assertThat(positions).isEqualTo(expected);
    }

    @Test
    @DisplayName("승진이 되는지 테스트")
    public void promotion() throws NoSuchFieldException, IllegalAccessException {
        //given
        board.init();
        //when]
        board.move(position(1, 2), position(1, 3));
        board.move(position(1, 3), position(1, 4));
        board.move(position(1, 4), position(1, 5));
        board.move(position(1, 5), position(1, 6));
        board.move(position(1, 6), position(1, 7));
        board.move(position(1, 7), position(1, 8));
        //then
        Piece piece = manager(pieces(board)).find(position(1, 8));
        assertThat(piece).isEqualTo(PieceFactory.queen(Color.BLACK, position(1, 8)));
    }

    @Test
    @DisplayName("기물이 없는곳에서 이동하는 테스트")
    public void move_emptyPiece(){
        //given
        board.init();
        //when
        boolean actual = board.move(position(1, 3), position(1, 4));
        //then
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("A7폰의 이동 경로 확인 테스트")
    public void possiblePosition_pawn(){
        //given
        board.init();
        //when
        List<Position> positions = board.possiblePositions(position(1, 7));
        //then
        assertThat(positions).isEqualTo(List.of(position(1, 6)));
    }

    @Test
    @DisplayName("A3에 있는 ROOK이 이동할 수 있는 경로 확인하는 테스트")
    public void possiblePosition_rook(){
        //given
        board.init();
        board.move(position(1, 2), position(1, 3)); // A2->A3
        board.move(position(1, 3), position(1, 4)); // A3->A4
        board.move(position(1, 1), position(1, 3)); // A1->A3
        //when
        List<Position> positions = board.possiblePositions(position(1, 3));
        //then
        List<Position> expected = List.of(
                                    position(1,1), position(1,2),
                                    position(2,3), position(3,3),
                                    position(4,3), position(5,3),
                                    position(6,3), position(7,3),
                                    position(8,3));
        assertThat(positions.containsAll(expected));
    }

    @Test
    @DisplayName("F4에 있는 흑비숍이 이동경로 확인하는 테스트")
    public void possiblePositions_bishop(){
        //given
        board.init();
        boolean move = board.move(position(4, 2), position(4, 3));// D2->D3
        move = board.move(position(3,1), position(6,4)); // C1->F4
        //when
        List<Position> positions = board.possiblePositions(position(6, 4));
        //then
        List<Position> expected = List.of(
                position(3,1), position(4,2),
                position(5,3), position(7,3),
                position(5,5), position(4,6),
                position(3,7), position(7,5),
                position(8,6));
        assertThat(positions.containsAll(expected)).isTrue();
    }

    @Test
    @DisplayName("C3에 있는 나이트가 이동할 수 있는 경로 테스트")
    public void possiblePositions_knight(){
        //given
        board.init();
        board.move(position(2, 1), position(3, 3)); // B1->C3
        //when
        List<Position> positions = board.possiblePositions(position(3, 3));
        //then
        List<Position> expected = new ArrayList<>(List.of(
                position(2,1),
                position(1,4), position(2,5),
                position(4,5), position(5,4)));
        Collections.sort(expected);
        assertThat(positions).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("E4에 있는 퀸이 이동할 수 있는 경로 테스트")
    public void possiblePositions_queen(){
        //given
        board.init();
        boolean move = board.move(position(4, 2), position(4, 3));// D2->D3
        move = board.move(position(5,1), position(2,4)); // E1->B4
        move = board.move(position(2,4), position(5,4)); // B4->E4
        //when
        List<Position> positions = board.possiblePositions(position(5, 4));
        //then
        List<Position> expected = new ArrayList<>(List.of(
                position(5,3), position(6,3),
                position(1,4), position(2,4),
                position(3,4), position(4,4),
                position(6,4), position(7,4),
                position(8,4), position(4,5),
                position(5,5), position(6,5),
                position(3,6), position(5,6),
                position(7,6), position(2,7),
                position(5,7), position(8,7)));
        Collections.sort(expected);
        assertThat(positions.containsAll(expected)).isTrue();
    }

    @Test
    @DisplayName("룩이 움직일 경로가 없는지 테스트")
    public void possiblePosition_rook_empty(){
        //given
        board.init();
        //when
        List<Position> positions = board.possiblePositions(position(1, 1));
        //then
        assertThat(positions.isEmpty()).isTrue();
    }

    private Position position(int file, int rank){
        return Position.create(File.find(file), Rank.find(rank));
    }

    private Pieces pieces(Board board) throws IllegalAccessException, NoSuchFieldException {
        Field pieces_field = Board.class.getDeclaredField("pieces");
        pieces_field.setAccessible(true);
        return (Pieces) pieces_field.get(board);
    }

    private List<Piece> pieceList(PieceManager manager) throws IllegalAccessException, NoSuchFieldException {
        Field pieces_list_field = manager.getClass().getDeclaredField("pieces");
        pieces_list_field.setAccessible(true);
        return (List<Piece>) pieces_list_field.get(manager);
    }

    private List<Piece> filterPawns(List<Piece> pieces, Color color){
        return pieces.stream().filter(p->p.equalColor(color))
                              .filter(Pawn.class::isInstance)
                              .collect(Collectors.toList());
    }

    private PieceManager manager(Pieces pieces) throws IllegalAccessException, NoSuchFieldException {
        Field field = pieces.getClass().getDeclaredField("manager");
        field.setAccessible(true);
        return (PieceManager) field.get(pieces);
    }

}