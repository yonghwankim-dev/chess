package co.nemo.chess.domain;

import java.util.Arrays;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PawnTest {
	public static Stream<Arguments> validWhitePawnMoveLocations() {
		String source = "b2";
		String[] destinations = {"a3", "b3", "b4", "c3"};
		return Stream.of(createArgumentsArray(source, destinations));
	}

	private static Arguments[] createArgumentsArray(String source, String[] destinations) {
		return Arrays.stream(destinations)
			.map(destination -> Arguments.of(source, destination))
			.toArray(Arguments[]::new);
	}

	public static Stream<Arguments> invalidWhitePawnMoveLocations() {
		String src = "b2";
		String[] destinations = {"a2", "a1", "b1", "c1", "c2"};
		return Stream.of(createArgumentsArray(src, destinations));
	}

	public static Stream<Arguments> validDarkPawnMoveLocations() {
		String src = "b7";
		String[] destinations = {"a6", "b6", "b5", "c6"};
		return Stream.of(createArgumentsArray(src, destinations));
	}

	public static Stream<Arguments> invalidDarkPawnMoveLocations() {
		String src = "b7";
		String[] destinations = {"a8", "a7", "c7", "c8", "b8"};
		return Stream.of(createArgumentsArray(src, destinations));
	}

	@DisplayName("백폰은 1칸 전진, 2칸 전진, 상좌, 상우 대각선 이동만 가능하다")
	@ParameterizedTest
	@MethodSource(value = "validWhitePawnMoveLocations")
	void givenWhitePawn_whenValidMoveLocations_thenReturnOfMovedPiece(String src, String dst) {
		// given
		Piece whitePawn = PieceFactory.getInstance().whitePawn(src);
		Location dstLocation = Location.from(dst);
		// when
		Piece actual = whitePawn.move(dstLocation);
		// then
		Piece expected = PieceFactory.getInstance().whitePawn(dst).withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("백폰은 1칸 전진, 2칸 전진, 상좌, 상우 대각선 이동을 제외한 다른 이동은 불가능하다")
	@ParameterizedTest
	@MethodSource(value = "invalidWhitePawnMoveLocations")
	void givenWhitePawn_whenInvalidMoveLocations_thenThrowsException(String src, String dst) {
		// given
		Piece whitePawn = PieceFactory.getInstance().whitePawn(src);
		Location dstLocation = Location.from(dst);
		// when
		Throwable throwable = Assertions.catchThrowable(() -> whitePawn.move(dstLocation));
		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("흑폰은 1칸 전진, 2칸 전진, 하좌, 하우 대각선 이동만 가능하다")
	@ParameterizedTest
	@MethodSource(value = "validDarkPawnMoveLocations")
	void givenDarkPawn_whenValidMoveLocations_thenReturnOfMovedLocation(String src, String dst) {
		// given
		Piece darkPawn = PieceFactory.getInstance().darkPawn(src);
		Location dstLocation = Location.from(dst);
		// when
		Piece actual = darkPawn.move(dstLocation);
		// then
		Piece expected = PieceFactory.getInstance().darkPawn(dst).withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("흑폰은 1칸 전진, 2칸 전진, 하좌, 화우 대각선 이동을 제외한 다른 이동은 불가능하다")
	@ParameterizedTest
	@MethodSource(value = "invalidDarkPawnMoveLocations")
	void givenDarkPawn_whenInvalidDirection_thenThrowsException(String src, String dst) {
		// given
		Piece darkPawn = PieceFactory.getInstance().darkPawn(src);
		Location dstLocation = Location.from(dst);
		// when
		Throwable throwable = Assertions.catchThrowable(() -> darkPawn.move(dstLocation));
		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("이미 이동한 백폰은 2칸 전진할 수 없다")
	@Test
	void givenWhitePawn_whenAlreadyMoved_thenNotTwoMoveSquare() {
		// given
		Piece pawn = PieceFactory.getInstance().whitePawn("a3").withMoved();
		Location dst = Location.from("a5");
		// when
		Throwable throwable = Assertions.catchThrowable(() -> pawn.move(dst));
		// then
		Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}
}
