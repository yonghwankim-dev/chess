package co.nemo.chess.domain.game;

import co.nemo.chess.domain.player.AbstractCommand;
import co.nemo.chess.domain.player.CommandParser;

public class ChessGameReader {
	private final InputStrategy inputStrategy;
	private final ChessGameStatusPrinter statusPrinter;

	public ChessGameReader(InputStrategy inputStrategy, ChessGameStatusPrinter statusPrinter) {
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
}
