package co.nemo.chess.domain.player;

import java.io.BufferedReader;
import java.io.StringReader;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.piece.Color;
import co.nemo.chess.domain.piece.Location;

class PlayerTest {

	private Board board;

	@BeforeEach
	void setUp() {
		board = Board.empty();
		board.setupPieces();
	}

	@DisplayName("플레이어는 이동 명령어를 입력한다")
	@Test
	void inputCommand() {
		// given
		Player player = new Player(Color.WHITE);
		BufferedReader reader = new BufferedReader(new StringReader("move a2 a3"));
		// when
		AbstractCommand actual = player.inputCommand(reader);
		// then
		AbstractCommand expected = AbstractCommand.moveCommand(Location.from("a2"), Location.from("a3"));
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("플레이어는 이동 가능 경로 명령어를 입력한다")
	@Test
	void givenPlayer_whenInputLocationCommand_thenReturnCommand() {
		// given
		Player player = new Player(Color.WHITE);
		BufferedReader reader = new BufferedReader(new StringReader("locations a2"));
		// when
		AbstractCommand actual = player.inputCommand(reader);
		// then
		AbstractCommand expected = AbstractCommand.locationsCommand(Location.from("a2"));
		Assertions.assertThat(actual).isEqualTo(expected);
	}
}
