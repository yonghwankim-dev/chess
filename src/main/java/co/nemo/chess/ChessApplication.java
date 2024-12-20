package co.nemo.chess;

import co.nemo.chess.domain.game.ChessGame;
import co.nemo.chess.domain.game.ConsoleChessGame;

public class ChessApplication {

	public static void main(String[] args) {
		ChessGame game = ConsoleChessGame.init();
		game.startGame();
	}

}
