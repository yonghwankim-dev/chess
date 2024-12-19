package co.nemo.chess.domain.piece;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BishopTest {

	@DisplayName("백비숍의 이동 가능한 경로를 계산한다")
	@Test
	void findAllMoveLocations() {
		// given
		Piece bishop = PieceFactory.getInstance().whiteBishop("c1");
		// when
		List<Location> actual = bishop.findAllMoveLocations();
		// then
		List<Location> expected = Stream.of("b2", "a3", "d2", "e3", "f4", "g5", "h6")
			.map(Location::from)
			.toList();
		Assertions.assertThat(actual).containsExactlyElementsOf(expected);
	}

}
