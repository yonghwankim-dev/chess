package co.nemo.chess.domain.piece;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AbstractChessPieceTest {

	@DisplayName("두 공간의 중간 경로를 계산한다")
	@Test
	void calBetweenLocations() {
		// given
		AbstractChessPiece piece = PieceFactory.getInstance().whiteRook("a2");
		Location dst = Location.from("a4");
		// when
		List<Location> actual = piece.calBetweenLocations(dst);
		// then
		List<Location> expected = List.of(Location.from("a3"), Location.from("a4"));
		Assertions.assertThat(actual)
			.hasSize(2)
			.containsExactlyElementsOf(expected);
	}

}
