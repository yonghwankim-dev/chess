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
		Pawn pawn = new Pawn(File.A, 2, "white");
		// when
		Pawn actual = pawn.move();
		// then
		Pawn expected = new Pawn(File.A, 3, "white");
		Assertions.assertThat(actual).isEqualTo(expected);
	}
}
