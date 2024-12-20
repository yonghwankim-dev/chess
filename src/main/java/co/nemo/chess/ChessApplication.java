package co.nemo.chess;

import co.nemo.chess.domain.game.ChessGame;

public class ChessApplication {

	public static void main(String[] args) {
		ChessGame game = ChessGame.consoleBased();
		game.startGame();
	}

}
