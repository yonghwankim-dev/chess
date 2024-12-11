package co.nemo.chess.domain.board;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.piece.PieceFactory;

class BoardTest {

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
