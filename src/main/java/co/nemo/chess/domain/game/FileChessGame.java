package co.nemo.chess.domain.game;

import java.util.Optional;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.player.AbstractCommand;
import co.nemo.chess.domain.player.Player;

public class FileChessGame implements ChessGame {

	private final CommandProcessor processor;
	private final ChessGameStatusPrinter statusPrinter;
	private final CheckmateChecker checkmateChecker;
	private final ChessGameReader reader;

	public FileChessGame(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy) {
		this.processor = new CommandProcessor(board, inputStrategy, outputStrategy);
		this.statusPrinter = new ChessGameStatusPrinter(outputStrategy);
		this.checkmateChecker = new CheckmateChecker(statusPrinter);
		this.reader = new ChessGameReader(inputStrategy, statusPrinter);
	}

	public static ChessGame init(InputStrategy inputStrategy, OutputStrategy outputStrategy) {
		return new FileChessGame(
			Board.empty(),
			inputStrategy,
			outputStrategy
		);
	}

	@Override
	public Optional<Player> startGame() {
		processor.setupPieces();
		statusPrinter.printGameStart();

		while (!processor.isCheckmate(checkmateChecker)) {
			processor.printGameStatus(statusPrinter);
			AbstractCommand command = reader.readCommand();
			if (command.isExistCommand()) {
				break;
			}
			processor.process(command);
		}
		reader.close();
		return processor.getWinner();
	}
}
