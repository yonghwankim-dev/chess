package co.nemo.chess.domain.piece;

public interface PawnStrategy {
	Direction getForwardDirection();

	AbstractChessPiece promoTo(PieceType type) throws IllegalStateException;

	boolean canPromote();
}
