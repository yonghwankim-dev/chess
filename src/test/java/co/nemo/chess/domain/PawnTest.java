package co.nemo.chess.domain;

import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PawnTest {
	public static Stream<Arguments> directions() {
		return Stream.of(
			Arguments.of(Direction.UP_LEFT, "b2", "a3"),
			Arguments.of(Direction.UP_RIGHT, "b2", "c3")
		);
	}

	public static Stream<Arguments> invalidWhitePawnDirections() {
		String src = "b2";
		return Stream.of(
			Arguments.of(Direction.UP, src, "b3"),
			Arguments.of(Direction.DOWN, src, "b1"),
			Arguments.of(Direction.LEFT, src, "a2"),
			Arguments.of(Direction.RIGHT, src, "c2"),
			Arguments.of(Direction.DOWN_LEFT, "a1"),
			Arguments.of(Direction.DOWN_RIGHT, "c1")
		);
	}

	public static Stream<Arguments> invalidDarkPawnDirections() {
		String src = "b2";
		return Stream.of(
			Arguments.of(Direction.UP, src, "b3"),
			Arguments.of(Direction.DOWN, src, "b1"),
			Arguments.of(Direction.LEFT, src, "a2"),
			Arguments.of(Direction.RIGHT, src, "c2"),
			Arguments.of(Direction.UP_LEFT, src, "a3"),
			Arguments.of(Direction.UP_RIGHT, src, "c3")
		);
	}

	@DisplayName("백폰을 1칸 전진한다")
	@Test
	void move() {
		// given
		AbstractChessPiece pawn = Pawn.whitePawn("a2");
		Location dst = Location.from("a3");
		// when
		AbstractChessPiece actual = pawn.move(dst);
		// then
		AbstractChessPiece expected = Pawn.whitePawn("a3").withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("흑폰을 1칸 전진한다")
	@Test
	void givenDarkPawn_whenMove_thenRankDecrease() {
		// given
		AbstractChessPiece pawn = Pawn.darkPawn("a7");
		Location dst = Location.from("a6");
		// when
		AbstractChessPiece actual = pawn.move(dst);
		// then
		AbstractChessPiece expected = Pawn.darkPawn("a6").withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("백폰은 처음 이동시 2칸 이동할 수 있다")
	@Test
	void moveTwoSquares() {
		// given
		AbstractChessPiece pawn = Pawn.whitePawn("a2");
		Location dst = Location.from("a4");
		// when
		AbstractChessPiece actual = pawn.move(dst);
		// then
		AbstractChessPiece expected = Pawn.whitePawn("a4")
			.withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("이미 이동한 백폰은 2칸 전진할 수 없다")
	@Test
	void givenWhitePawn_whenAlreadyMoved_thenNotTwoMoveSquare() {
		// given
		AbstractChessPiece pawn = Pawn.whitePawn("a3")
			.withMoved();
		Location dst = Location.from("a5");
		// when
		Throwable throwable = Assertions.catchThrowable(() -> pawn.move(dst));
		// then
		Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("백폰이 대각선으로 이동하여 흑폰을 잡는다")
	@ParameterizedTest
	@MethodSource(value = "directions")
	void givenWhitePawn_whenMoveDiagonal_thenCatchB3BlackPawn(Direction direction, String src, String dst) {
		// given
		AbstractChessPiece whitePawn = Pawn.whitePawn(src);
		Location dstLocation = Location.from(dst);
		// when
		AbstractChessPiece actual = whitePawn.move(dstLocation);
		// then
		AbstractChessPiece expected = Pawn.whitePawn(dst).withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("백폰은 상좌, 상우 대각선을 제외한 다른 방향의 대각선은 이동 불가능하다")
	@ParameterizedTest
	@MethodSource(value = "invalidWhitePawnDirections")
	void givenWhitePawn_whenInvalidDirection_thenThrowsException(Direction direction, String src, String dst) {
		// given
		AbstractChessPiece whitePawn = Pawn.whitePawn(src);
		Location dstLocation = Location.from(dst);
		// when
		Throwable throwable = Assertions.catchThrowable(() -> whitePawn.move(dstLocation));
		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("흑폰은 하좌, 화우 대각선을 제외한 다른 방향의 대각선은 이동 불가능하다")
	@ParameterizedTest
	@MethodSource(value = "invalidDarkPawnDirections")
	void givenDarkPawn_whenInvalidDirection_thenThrowsException(Direction direction, String src, String dst) {
		// given
		AbstractChessPiece darkPawn = Pawn.darkPawn(src);
		Location dstLocation = Location.from(dst);
		// when
		Throwable throwable = Assertions.catchThrowable(() -> darkPawn.move(dstLocation));
		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class);
	}
}
