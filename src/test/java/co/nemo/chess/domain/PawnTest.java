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
			Arguments.of(Direction.UP, src),
			Arguments.of(Direction.DOWN, src),
			Arguments.of(Direction.LEFT, src),
			Arguments.of(Direction.DOWN, src),
			Arguments.of(Direction.DOWN_LEFT, src),
			Arguments.of(Direction.DOWN_RIGHT, src)
		);
	}

	public static Stream<Arguments> invalidDarkPawnDirections() {
		String src = "b2";
		return Stream.of(
			Arguments.of(Direction.UP, src),
			Arguments.of(Direction.DOWN, src),
			Arguments.of(Direction.LEFT, src),
			Arguments.of(Direction.DOWN, src),
			Arguments.of(Direction.UP_LEFT, src),
			Arguments.of(Direction.UP_RIGHT, src)
		);
	}

	@DisplayName("백폰을 1칸 전진한다")
	@Test
	void move() {
		// given
		Pawn pawn = Pawn.whitePawn("a2");
		// when
		Pawn actual = pawn.moveForwardly();
		// then
		Pawn expected = Pawn.whitePawn("a3").withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("흑폰을 1칸 전진한다")
	@Test
	void givenDarkPawn_whenMove_thenRankDecrease() {
		// given
		Pawn pawn = Pawn.darkPawn("a7");
		// when
		Pawn actual = pawn.moveForwardly();
		// then
		Pawn expected = Pawn.darkPawn("a6").withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("백폰은 a9으로 이동할 수 없다")
	@Test
	void givenWhitePawn_whenA9_thenNotMove() {
		// given
		Pawn pawn = Pawn.whitePawn("a8");
		// when
		Throwable throwable = Assertions.catchThrowable(pawn::moveForwardly);
		// then
		Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("백폰은 처음 이동시 2칸 이동할 수 있다")
	@Test
	void moveTwoSquares() {
		// given
		Pawn pawn = Pawn.whitePawn("a2");
		// when
		Pawn actual = pawn.moveTwoSquares();
		// then
		Pawn expected = Pawn.whitePawn("a4")
			.withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("이미 이동한 백폰은 2칸 전진할 수 없다")
	@Test
	void givenWhitePawn_whenAlreadyMoved_thenNotTwoMoveSquare() {
		// given
		Pawn pawn = Pawn.whitePawn("a3")
			.withMoved();
		// when
		Throwable throwable = Assertions.catchThrowable(pawn::moveTwoSquares);
		// then
		Assertions.assertThat(throwable).isInstanceOf(IllegalStateException.class);
	}

	@DisplayName("백폰이 대각선으로 이동하여 흑폰을 잡는다")
	@ParameterizedTest
	@MethodSource(value = "directions")
	void givenWhitePawn_whenMoveDiagonal_thenCatchB3BlackPawn(Direction direction, String src, String dst) {
		// given
		Pawn whitePawn = Pawn.whitePawn(src);
		// when
		Pawn actual = whitePawn.moveDiagonally(direction);
		// then
		Pawn expected = Pawn.whitePawn(dst).withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("백폰은 상좌, 상우 대각선으로만 이동 가능하다")
	@ParameterizedTest
	@MethodSource(value = "invalidWhitePawnDirections")
	void givenWhitePawn_whenInvalidDirection_thenThrowsException(Direction direction, String src) {
		// given
		Pawn whitePawn = Pawn.whitePawn(src);
		// when
		Throwable throwable = Assertions.catchThrowable(() -> whitePawn.moveDiagonally(direction));
		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("흑폰은 하좌, 화우 대각선으로만 이동 가능하다")
	@ParameterizedTest
	@MethodSource(value = "invalidDarkPawnDirections")
	void givenDarkPawn_whenInvalidDirection_thenThrowsException(Direction direction, String src) {
		// given
		Pawn darkPawn = Pawn.darkPawn(src);
		// when
		Throwable throwable = Assertions.catchThrowable(() -> darkPawn.moveDiagonally(direction));
		// then
		Assertions.assertThat(throwable)
			.isInstanceOf(IllegalArgumentException.class);
	}
}
