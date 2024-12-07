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

	@DisplayName("백폰을 1칸 전진한다")
	@Test
	void move() {
		// given
		Pawn pawn = Pawn.whitePawn("a2");
		// when
		Pawn actual = pawn.move();
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
		Pawn actual = pawn.move();
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
		Throwable throwable = Assertions.catchThrowable(pawn::move);
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
	void givenWhitePawn_whenMoveDiagonal_thenCatchB3BlackPawn(Direction direction, String srcPosition,
		String dstPosition) {
		// given
		Pawn whitePawn = Pawn.whitePawn(srcPosition);
		// when
		Pawn actual = whitePawn.moveDiagonally(direction);
		// then
		Pawn expected = Pawn.whitePawn(dstPosition).withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
	}
}
