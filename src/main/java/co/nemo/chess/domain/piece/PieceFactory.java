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

	public AbstractChessPiece rook(String position, Color color) {
		return color == Color.WHITE ? whiteRook(position) : darkRook(position);
	}

	public AbstractChessPiece whiteRook(String position) {
		return Rook.notMovedWhiteRook(Location.from(position));
	}

	public AbstractChessPiece darkRook(String position) {
		return Rook.notMovedDarkRook(Location.from(position));
	}

	public AbstractChessPiece king(String position, Color color) {
		return color == Color.WHITE ? whiteKing(position) : darkKing(position);
	}

	public AbstractChessPiece whiteKing(String position) {
		return King.notMovedWhiteKing(Location.from(position));
	}

	public AbstractChessPiece darkKing(String position) {
		return King.notMOvedDarkKing(Location.from(position));
	}

	public AbstractChessPiece bishop(String position, Color color) {
		return color == Color.WHITE ? whiteBishop(position) : darkBishop(position);
	}

	public AbstractChessPiece whiteBishop(String position) {
		return Bishop.notMovedWhiteBishop(Location.from(position));
	}

	public AbstractChessPiece darkBishop(String position) {
		return Bishop.notMovedDarkBishop(Location.from(position));
	}

	public AbstractChessPiece queen(String position, Color color) {
		return color == Color.WHITE ? whiteQueen(position) : darkQueen(position);
	}

	public AbstractChessPiece whiteQueen(String position) {
		return Queen.notMovedWhiteQueen(Location.from(position));
	}

	public AbstractChessPiece darkQueen(String position) {
		return Queen.notMovedDarkQueen(Location.from(position));
	}

	public AbstractChessPiece knight(String position, Color color) {
		return color == Color.WHITE ? whiteKnight(position) : darkKnight(position);
	}

	public AbstractChessPiece whiteKnight(String position) {
		return Knight.notMovedWhiteKnight(Location.from(position));
	}

	public AbstractChessPiece darkKnight(String position) {
		return Knight.notMovedDarkKnight(Location.from(position));
	}
}
