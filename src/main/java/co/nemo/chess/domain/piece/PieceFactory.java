package co.nemo.chess.domain.piece;

public class PieceFactory {

	private PieceFactory() {

	}

	private static class PieceFactoryHelper {
		private static final PieceFactory INSTANCE = new PieceFactory();
	}

	public static PieceFactory getInstance() {
		return PieceFactoryHelper.INSTANCE;
	}

	public AbstractChessPiece pawn(String position, Color color) {
		return color == Color.WHITE ? whitePawn(position) : darkPawn(position);
	}

	public AbstractChessPiece whitePawn(String position) {
		return Pawn.notMovedWhitePawn(Location.from(position));
	}

	public AbstractChessPiece darkPawn(String position) {
		return Pawn.notMovedDarkPawn(Location.from(position));
	}

	public AbstractChessPiece whiteRook(String position) {
		return Rook.notMovedWhiteRook(Location.from(position));
	}

	public AbstractChessPiece darkRook(String position) {
		return Rook.notMovedDarkRook(Location.from(position));
	}
}
