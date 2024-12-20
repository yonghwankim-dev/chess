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

	public void printCurrentPlayer(Player currentPlayer) {
		outputStrategy.println("현재 차례: " + currentPlayer);
	}

	public void printBoard(Board board) {
		outputStrategy.printBoard(board);
	}

	public void printDarkPlayerWin() {
		outputStrategy.println("흑 플레이어 승리!");
	}

	public void printWhitePlayerWin() {
		outputStrategy.println("백 플레이어 승리!");
	}
}
