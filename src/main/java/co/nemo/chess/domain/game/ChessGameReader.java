package co.nemo.chess.domain.game;

import co.nemo.chess.domain.piece.PieceType;
import co.nemo.chess.domain.command.AbstractCommand;
import co.nemo.chess.domain.command.CommandParser;

public class ChessGameReader {
	private final InputStrategy inputStrategy;
	private final ChessGameWriter statusPrinter;

	public ChessGameReader(InputStrategy inputStrategy, ChessGameWriter statusPrinter) {
		this.inputStrategy = inputStrategy;
		this.statusPrinter = statusPrinter;
	}

	public AbstractCommand readCommand() {
		statusPrinter.printPrompt();
		try {
			return inputStrategy.readLine()
				.map(text -> CommandParser.getInstance().parse(text))
				.orElseThrow(() -> new IllegalArgumentException("No input received. Exiting game."));
		} catch (IllegalArgumentException e) {
			statusPrinter.printErrorMessage(e);
			return AbstractCommand.exitCommand();
		}
	}

	public PieceType readPieceType() throws IllegalArgumentException {
		return inputStrategy.readLine()
			.map(PieceType::valueOfText)
			.orElseThrow(() ->
				new IllegalArgumentException("Invalid input, possible options: Queen, Rook, Bishop, Knight"));
	}

	public void close() {
		inputStrategy.close();
	}
}
