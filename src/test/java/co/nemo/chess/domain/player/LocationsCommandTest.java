package co.nemo.chess.domain.player;

import org.apache.logging.log4j.util.Strings;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.ChessGameReader;
import co.nemo.chess.domain.game.ChessGameWriter;
import co.nemo.chess.domain.game.ConsoleOutputStrategy;
import co.nemo.chess.domain.game.InputStrategy;
import co.nemo.chess.domain.game.OutputStrategy;
import co.nemo.chess.domain.game.StringInputStrategy;
import co.nemo.chess.domain.piece.Location;

class LocationsCommandTest {

	private Board board;
	private InputStrategy inputStrategy;
	private ChessGameWriter gameWriter;
	private ChessGameReader gameReader;
	private OutputStrategy outputStrategy;
	private Player whitePlayer;

	@BeforeEach
	void setUp() {
		this.board = Board.empty();
		board.setupPieces();
		this.inputStrategy = new StringInputStrategy(Strings.EMPTY);
		this.outputStrategy = ConsoleOutputStrategy.getInstance();
		this.gameWriter = new ChessGameWriter(outputStrategy);
		this.gameReader = new ChessGameReader(inputStrategy, gameWriter);
		this.whitePlayer = Player.white();
	}

	@DisplayName("백색 플레이어가 흑색 기물에 대한 이동 경로를 조회할 수 없다")
	@Test
	void givenWhitePlayer_whenAttemptingToLocationsBlackPiece_thenLocationsIsInvalid() {
		// given
		AbstractCommand command = LocationsCommand.from(Location.from("a7"));

		// when
		Throwable throwable = Assertions.catchThrowable(
			() -> command.process(board, gameReader, gameWriter, whitePlayer));
		// then
		Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}
}
