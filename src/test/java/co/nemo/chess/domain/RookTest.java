package co.nemo.chess.domain;

import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import co.nemo.chess.TestSupportUtils;

class RookTest {

	public static Stream<Arguments> validMoveLocations() {
		String source = "a1";
		String[] destinations = {"a2", "a3", "a4", "a5", "a6", "a7", "a8", "b1", "c1", "d1", "e1", "f1", "g1", "h1"};
		return Stream.of(TestSupportUtils.createArgumentsArray(source, destinations));
	}

	public static Stream<Arguments> invalidMoveLocations() {
		String source = "a1";
		String[] destinations = {"a1", "b2", "b3", "c2", "c3"};
		return Stream.of(TestSupportUtils.createArgumentsArray(source, destinations));
	}

	@DisplayName("룩은 직선상의 상,하,좌,우 방향으로만 이동이 가능하다")
	@ParameterizedTest
	@MethodSource(value = "validMoveLocations")
	void givenRook_whenMove_thenReturnOfPiece(String src, String dst) {
		// given
		Piece rook = PieceFactory.getInstance().whiteRook(src);
		Location dstLocation = Location.from(dst);
		// when
		Piece actual = rook.move(dstLocation);
		// then
		Piece expected = PieceFactory.getInstance().whiteRook(dst).withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("룩은 직선 방향외에 다른 방향으로 이동이 불가능하다")
	@ParameterizedTest
	@MethodSource(value = "invalidMoveLocations")
	void givenRook_whenInvalidMoveLocation_thenThrowsException(String src, String dst) {
		// given
		Piece rook = PieceFactory.getInstance().whiteRook(src);
		Location dstLocation = Location.from(dst);
		// when
		Throwable throwable = Assertions.catchThrowable(() -> rook.move(dstLocation));
		// then
		Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

}
