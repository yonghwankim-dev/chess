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
	void accept() {
		// given
		Board board = new Board();
		Piece whitePawn = PieceFactory.getInstance().whitePawn("a2");
		Location destination = Location.from("a3");
		// when
		boolean actual = board.accept(whitePawn, destination);
		// then
		Assertions.assertThat(actual).isTrue();
	}

}
