package co.nemo.chess.domain.board;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.piece.PieceFactory;

class BoardTest {

	@DisplayName("폰 기물과 목적지를 전달하여 보드에 접수한다")
	@Test
	void move() {
		// given
		Board board = Board.empty();
		board.addPiece(PieceFactory.getInstance().whitePawn("a2"));

		Location src = Location.from("a2");
		Location dst = Location.from("a3");
		// when
		boolean actual = board.move(src, dst);
		// then
		Assertions.assertThat(actual).isTrue();
		Piece expected = PieceFactory.getInstance().whitePawn("a3").withMoved();
		Assertions.assertThat(board.find(dst)).isEqualTo(expected);
		Assertions.assertThat(board.size()).isEqualTo(1);
	}

	@DisplayName("보드판에 아무것도 없는 위치에서 기물을 움직이려고 하는 경우 예외 발생한다")
	@Test
	void givenBoard_whenEmptyMove_thenThrowsException() {
		// given
		Board board = Board.empty();
		Location src = Location.from("a2");
		Location dst = Location.from("a3");
		// when
		Throwable throwable = Assertions.catchThrowable(() -> board.move(src, dst));
		// then
		Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("not found piece, location=" + src);
	}
}
