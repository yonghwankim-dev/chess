package co.nemo.chess.domain.board;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.piece.PieceFactory;

class BoardTest {

	@DisplayName("보드판 위에 A2 백폰을 A3으로 이동시킨다")
	@Test
	void movePiece() {
		// given
		Piece whitePawn = PieceFactory.getInstance().whitePawn("a2");
		Board board = Board.init(whitePawn);

		Location src = Location.from("a2");
		Location dst = Location.from("a3");
		// when
		Optional<Piece> actual = board.movePiece(src, dst);
		// then
		Piece expected = PieceFactory.getInstance().whitePawn("a3").withMoved();
		assertThat(actual).contains(expected);
		assertThat(board.findPiece(dst)).isEqualTo(expected);
		assertThat(board.size()).isEqualTo(1);
	}

	@DisplayName("보드판 위에 A2 백폰이 B3 흑폰을 잡는다")
	@Test
	void givenWhitePawn_whenA2WhitePawnMoveB3_thenDeleteB3BlackPawn() {
		// given
		Piece whitePawn = PieceFactory.getInstance().whitePawn("a2");
		Piece darkPawn = PieceFactory.getInstance().darkPawn("b3");
		Board board = Board.init(whitePawn, darkPawn);

		Location src = Location.from("a2");
		Location dst = Location.from("b3");
		// when
		Optional<Piece> actual = board.movePiece(src, dst);
		// then
		Piece expected = PieceFactory.getInstance().whitePawn("b3").withMoved();
		assertThat(actual).contains(expected);
		assertThat(board.findPiece(dst)).isEqualTo(expected);
		assertThat(board.size()).isEqualTo(1);
	}

	@Disabled
	@DisplayName("보드판 위에 A2 백폰이 B3 백폰을 잡을 수 없다")
	@Test
	void givenWhitePawn_whenA2WhitePawnMoveB3_thenNotDeleteB3WhitePawn() {
		// given
		Piece whitePawn = PieceFactory.getInstance().whitePawn("a2");
		Piece darkPawn = PieceFactory.getInstance().whitePawn("b3");
		Board board = Board.init(whitePawn, darkPawn);

		Location src = Location.from("a2");
		Location dst = Location.from("b3");
		// when
		Optional<Piece> actual = board.movePiece(src, dst);
		// then
		assertThat(actual).isEmpty();
		assertThat(board.size()).isEqualTo(2);
	}

	@DisplayName("보드판에 아무것도 없는 위치에서 기물을 움직이려고 하는 경우 예외 발생한다")
	@Test
	void givenBoard_whenMovePieceFromEmptyLocation_thenThrowsException() {
		// given
		Board board = Board.empty();
		Location src = Location.from("a2");
		Location dst = Location.from("a3");
		// when
		Optional<Piece> actual = board.movePiece(src, dst);
		// then
		assertThat(actual).isEmpty();
	}

	@DisplayName("보드판 위에 A2 백폰의 이동 가능 경로를 계산한다")
	@Test
	void findPossiblePaths() {
		// given
		Piece whitePawn = PieceFactory.getInstance().whitePawn("a2");
		PieceMovable board = Board.init(whitePawn);

		Location src = Location.from("a2");
		// when
		List<Location> actual = board.findPossiblePaths(src);
		// then
		List<Location> expected = Stream.of("a3", "a4")
			.map(Location::from)
			.toList();
		Assertions.assertThat(actual).containsExactlyElementsOf(expected);
	}
}
