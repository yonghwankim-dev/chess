package co.nemo.chess.domain.player;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.nemo.chess.domain.command.AbstractCommand;
import co.nemo.chess.domain.piece.Location;

class PlayerTest {

	@DisplayName("플레이어는 이동 명령어를 입력한다")
	@Test
	void inputCommand() {
		// given
		Player player = Player.white();
		// when
		AbstractCommand actual = player.inputCommand("move a2 a3");
		// then
		AbstractCommand expected = AbstractCommand.moveCommand(Location.from("a2"), Location.from("a3"));
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("플레이어는 이동 가능 경로 명령어를 입력한다")
	@Test
	void givenPlayer_whenInputLocationCommand_thenReturnCommand() {
		// given
		Player player = Player.white();
		// when
		AbstractCommand actual = player.inputCommand("locations a2");
		// then
		AbstractCommand expected = AbstractCommand.locationsCommand(Location.from("a2"));
		Assertions.assertThat(actual).isEqualTo(expected);
	}
}
