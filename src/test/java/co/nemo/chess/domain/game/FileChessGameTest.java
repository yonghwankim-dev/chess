package co.nemo.chess.domain.game;

import java.io.IOException;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import co.nemo.chess.domain.player.Player;

class FileChessGameTest {

	@DisplayName("입력 파일이 주어지고 체스 게임을 시작할 때 백 플레이어가 이긴다")
	@Test
	void givenFile_whenStartGame_thenWinWhitePlayer() throws IOException {
		// given
		ClassPathResource resource = new ClassPathResource("test/test1.txt");
		InputStrategy inputStrategy = FileInputStrategy.from(resource.getFile());
		OutputStrategy outputStrategy = ConsoleOutputStrategy.getInstance();
		ChessGame chessGame = ChessGame.init(inputStrategy, outputStrategy);
		// when
		Optional<Player> actual = chessGame.startGame();
		// then
		Player expected = Player.white();
		Assertions.assertThat(actual).contains(expected);
	}
}
