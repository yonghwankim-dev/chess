package co.nemo.chess.domain.game;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.player.AbstractCommand;
import co.nemo.chess.domain.player.Player;

public class CommandProcessor {

	private final Board board;
	private final InputStrategy inputStrategy;
	private final OutputStrategy outputStrategy;
	private final Player whitePlayer;
	private final Player darkPlayer;
	private Player currentPlayer;

	public CommandProcessor(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy) {
		this.board = board;
		this.inputStrategy = inputStrategy;
		this.outputStrategy = outputStrategy;
		this.whitePlayer = Player.white();
		this.darkPlayer = Player.dark();
		this.currentPlayer = this.whitePlayer;
	}

	public void setupPieces() {
		board.setupPieces();
	}

	public void process(AbstractCommand command) {
		boolean isChangedTurn = false;
		try {
			isChangedTurn = command.process(board, inputStrategy, outputStrategy, currentPlayer);
		} catch (IllegalArgumentException e) {
			outputStrategy.println(e.getMessage());
		}
		if (isChangedTurn) {
			switchPlayer();
		}
	}

	private void switchPlayer() {
		currentPlayer = (currentPlayer == whitePlayer) ? darkPlayer : whitePlayer;
	}

	public void printCurrentPlayer(ChessGameStatusPrinter statusPrinter) {
		statusPrinter.printCurrentPlayer(currentPlayer);
	}

	public void printBoard(ChessGameStatusPrinter statusPrinter) {
		statusPrinter.printBoard(board);
	}
}
