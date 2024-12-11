package co.nemo.chess.domain.piece;

import static co.nemo.chess.domain.piece.Direction.*;

import java.util.List;

import co.nemo.chess.domain.board.PieceRepository;

public class Rook extends AbstractChessPiece {
	Rook(Location location, Color color, boolean isMoved) {
		super(location, color, isMoved);
	}

	public static AbstractChessPiece notMovedWhiteRook(Location location) {
		return new Rook(location, Color.WHITE, false);
	}

	public static AbstractChessPiece notMovedDarkRook(Location location) {
		return new Rook(location, Color.DARK, false);
	}

	@Override
	public boolean canMove(Location destination, PieceRepository repository) {
		return canMoveStraight(destination);
	}

	private boolean canMoveStraight(Location destination) {
		return List.of(UP, DOWN, LEFT, RIGHT).contains(calDirection(destination));
	}

	@Override
	AbstractChessPiece movedPiece(Location location, Color color) {
		return new Rook(location, color, true);
	}

	@Override
	public List<Location> findPossibleLocations() {
		// TODO: 12/11/24
		return null;
	}
}
