package co.nemo.chess.domain.game;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.player.AbstractCommand;

public class ConsoleChessGame implements ChessGame {

	private final CommandProcessor processor;
	private final ChessGameStatusPrinter statusPrinter;
	private final ChessGameStatusManager statusManager;
	private final ChessGameReader reader;

	public ConsoleChessGame(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy) {
		this.processor = new CommandProcessor(board, inputStrategy, outputStrategy);
		this.statusPrinter = new ChessGameStatusPrinter(outputStrategy);
		this.statusManager = new ChessGameStatusManager(board, statusPrinter);
		this.reader = new ChessGameReader(inputStrategy, statusPrinter);
	}

	public static ConsoleChessGame init() {
		return new ConsoleChessGame(
			Board.empty(),
			ConsoleInputStrategy.getInstance(),
			ConsoleOutputStrategy.getInstance()
		);
	}

	@Override
	public void startGame() {
		processor.setupPieces();
		statusPrinter.printGameStart();

		while (!statusManager.isCheckmate()) {
			processor.printGameStatus(statusPrinter);
			AbstractCommand command = reader.readCommand();
			if (command.isExistCommand()) {
				break;
			}
			processor.process(command);
		}
		reader.close();
	}
}
