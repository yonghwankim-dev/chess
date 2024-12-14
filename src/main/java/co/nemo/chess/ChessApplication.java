package co.nemo.chess;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.ChessGame;
import co.nemo.chess.domain.game.ConsoleOutputStrategy;

public class ChessApplication {

	public static void main(String[] args) {
		ChessGame game = new ChessGame(Board.empty(), ConsoleOutputStrategy.getInstance());
		game.startGame();
	}

}
