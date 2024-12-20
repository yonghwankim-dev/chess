package co.nemo.chess.domain.game;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.player.Player;

public class ChessGameStatusPrinter {
	private final OutputStrategy outputStrategy;

	public ChessGameStatusPrinter(OutputStrategy outputStrategy) {
		this.outputStrategy = outputStrategy;
	}

	public void printErrorMessage(Exception e) {
		outputStrategy.print(e.getMessage());
	}

	public void printPrompt() {
		outputStrategy.print("> ");
	}

	public void printGameStart() {
		outputStrategy.println("Game Start");
	}

	public void printGameStatus(Player currentPlayer, Board board) {
		printCurrentPlayer(currentPlayer);
		printBoard(board);
	}

	private void printCurrentPlayer(Player currentPlayer) {
		outputStrategy.println("현재 차례: " + currentPlayer);
	}

	private void printBoard(Board board) {
		outputStrategy.printBoard(board);
	}
}
