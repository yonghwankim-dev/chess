package co.nemo.chess.domain;

public interface Piece {
	AbstractChessPiece move(Location destination);
}
