package co.nemo.chess.domain;

public class PieceFactory {

	private PieceFactory() {

	}

	private static class PieceFactoryHelper {
		private static final PieceFactory INSTANCE = new PieceFactory();
	}

	public static PieceFactory getInstance() {
		return PieceFactoryHelper.INSTANCE;
	}

	public AbstractChessPiece whitePawn(String position) {
		Location location = Location.from(position);
		return Pawn.newInstance(location, Color.WHITE, false);
	}

	public AbstractChessPiece darkPawn(String position) {
		Location location = Location.from(position);
		return Pawn.newInstance(location, Color.DARK, false);
	}
}
