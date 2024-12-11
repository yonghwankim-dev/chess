package co.nemo.chess.domain.piece;

public interface Piece {
	AbstractChessPiece move(Location destination);

	boolean match(Location location);
}
