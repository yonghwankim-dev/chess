package co.nemo.chess.domain.player;

import co.nemo.chess.domain.piece.Color;
import co.nemo.chess.domain.piece.Piece;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Player {
	private final Color color;

	private Player(Color color) {
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
