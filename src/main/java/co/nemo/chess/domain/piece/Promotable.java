package co.nemo.chess.domain.piece;

public interface Promotable {
	AbstractChessPiece promoTo(PieceType type);
}
