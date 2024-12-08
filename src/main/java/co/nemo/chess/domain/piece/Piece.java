package co.nemo.chess.domain.piece;

public interface Piece {
	AbstractChessPiece move(Location destination);
}
