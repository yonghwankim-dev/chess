package co.nemo.chess.domain.piece;

public enum PieceType {
	PAWN, ROOK, BISHOP, KNIGHT, QUEEN, KING;

	public static PieceType valueOfText(String text) {
		return valueOf(text.toUpperCase());
	}
}
