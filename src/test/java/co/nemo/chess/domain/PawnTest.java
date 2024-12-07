package co.nemo.chess.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PawnTest {
	@DisplayName("백폰을 1칸 전진한다")
	@Test
	void move(){
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
	void test(){
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
	void givenWhitePawn_whenA9_thenNotMove(){
	    // given
		Pawn pawn = Pawn.whitePawn("a8");
		// when
		Throwable throwable = Assertions.catchThrowable(pawn::move);
		// then
		Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("백폰은 처음 이동시 2칸 이동할 수 있다")
	@Test
	void moveTwoSquares(){
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
	void givenWhitePawn_whenAlreadyMoved_thenNotTwoMoveSquare(){
	    // given
		Pawn pawn = Pawn.whitePawn("a3")
						.withMoved();
		// when
		Throwable throwable = Assertions.catchThrowable(pawn::moveTwoSquares);
		// then
		Assertions.assertThat(throwable).isInstanceOf(IllegalStateException.class);
	}
}
