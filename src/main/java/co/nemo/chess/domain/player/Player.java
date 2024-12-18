package co.nemo.chess.domain.player;

import co.nemo.chess.domain.piece.Color;
import co.nemo.chess.domain.piece.Piece;

public class Player {
	private final Color color;

	public Player(Color color) {
		this.color = color;
	}

	public static Player white() {
		return new Player(Color.WHITE);
	}

	public static Player dark() {
		return new Player(Color.DARK);
	}

	public AbstractCommand inputCommand(String text) {
		return CommandParser.getInstance().parse(text);
	}

	public boolean isOwnPiece(Piece piece) {
		return piece.isColorOf(color);
	}

	@Override
	public String toString() {
		return color.name();
	}
}
