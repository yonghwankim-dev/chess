package co.nemo.chess.domain.game;

import java.util.Optional;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.command.AbstractCommand;
import co.nemo.chess.domain.command.ExitCommand;
import co.nemo.chess.domain.command.ResignCommand;
import co.nemo.chess.domain.player.Player;

public class ChessGame {

	private final ChessGameWriter gameWriter;
	private final ChessGameReader gameReader;
	private final CommandProcessor processor;

	public ChessGame(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy) {
		this.gameWriter = new ChessGameWriter(outputStrategy);
		this.gameReader = new ChessGameReader(inputStrategy, gameWriter);
		CheckmateChecker checkmateChecker = new CheckmateChecker(gameWriter);
		this.processor = new CommandProcessor(board, gameReader, gameWriter, checkmateChecker);
	}

	public static ChessGame consoleBased() {
		return new ChessGame(
			Board.empty(),
			ConsoleInputStrategy.getInstance(),
			ConsoleOutputStrategy.getInstance()
		);
	}

	public static ChessGame init(InputStrategy inputStrategy, OutputStrategy outputStrategy) {
		return new ChessGame(
			Board.empty(),
			inputStrategy,
			outputStrategy
		);
	}

	public Optional<Player> startGame() {
		processor.setupPieces();
		gameWriter.printGameStart();

		Optional<Player> winner = Optional.empty();
		while (!processor.isCheckmate() && !processor.isStalemate()) {
			processor.printGameStatus(gameWriter);
			AbstractCommand command = gameReader.readCommand();
			if (command instanceof ExitCommand exit) {
				processor.process(exit);
				break;
			} else if (command instanceof ResignCommand resign) {
				processor.process(resign);
				winner = Optional.of(processor.getEnemyPlayer());
				break;
			}
			processor.process(command);
		}
		gameReader.close();
		if (processor.isStalemate()) {
			gameWriter.printStalemateMessage();
			return Optional.empty();
		} else if (winner.isEmpty()) {
			return processor.getWinner();
		} else {
			return winner;
		}
	}
}
