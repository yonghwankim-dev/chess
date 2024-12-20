package co.nemo.chess.domain.game;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.player.AbstractCommand;
import co.nemo.chess.domain.player.CommandType;
import co.nemo.chess.domain.player.Player;

public class CommandProcessor {

	private final Board board;
	private final InputStrategy inputStrategy;
	private final OutputStrategy outputStrategy;
	private Player currentPlayer;

	public CommandProcessor(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy,
		Player currentPlayer) {
		this.board = board;
		this.inputStrategy = inputStrategy;
		this.outputStrategy = outputStrategy;
		this.currentPlayer = currentPlayer;
	}

	public boolean processCommand(AbstractCommand command) {
		if (command.isTypeOf(CommandType.EXIT)) {
			return true;
		} else if (command.isTypeOf(CommandType.LOCATIONS) ||
			command.isTypeOf(CommandType.HELP)) {
			command.process(board, inputStrategy, outputStrategy, currentPlayer);
			return true;
		} else {
			return false;
		}
	}

	public boolean process(AbstractCommand command) {
		try {
			return command.process(board, inputStrategy, outputStrategy, currentPlayer);
		} catch (IllegalArgumentException e) {
			outputStrategy.println(e.getMessage());
			return false;
		}
	}

	public void switchPlayer(Player whitePlayer, Player blackPlayer) {
		currentPlayer = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
}
