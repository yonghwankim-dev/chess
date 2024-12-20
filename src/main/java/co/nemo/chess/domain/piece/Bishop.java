package co.nemo.chess.domain.piece;

import static co.nemo.chess.domain.piece.Direction.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import co.nemo.chess.domain.board.PieceRepository;

public class Bishop extends AbstractChessPiece {

	private Bishop(Location location, Color color, boolean isMoved, Deque<Location> locationHistory) {
		super(location, color, isMoved, locationHistory);
	}

	public static AbstractChessPiece notMovedWhiteBishop(Location location) {
		return new Bishop(location, Color.WHITE, false, new ArrayDeque<>());
	}

	public static AbstractChessPiece notMovedDarkBishop(Location location) {
		return new Bishop(location, Color.DARK, false, new ArrayDeque<>());
	}

	@Override
	AbstractChessPiece movedPiece(Location location, Color color, Deque<Location> moveHistory) {
		return new Bishop(location, color, true, moveHistory);
	}

	@Override
	protected AbstractChessPiece withLocationHistory(Location location, Color color, boolean isMoved,
		Deque<Location> locationHistory) {
		return new Bishop(location, color, isMoved, locationHistory);
	}

	@Override
	AttackType calAttackType(Location destination, PieceRepository repository) {
		return AttackType.NORMAL;
	}

	@Override
	public List<Location> findAllMoveLocations() {
		List<Location> result = new ArrayList<>();

		for (Direction direction : List.of(UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT)) {
			int distance = 1;
			Optional<Location> location;

			while ((location = this.calLocation(direction, distance)).isPresent()) {
				result.add(location.get());
				distance++;
			}
		}
		return result;
	}

	@Override
	public boolean canMove(Location location, PieceRepository repository) {
		return emptyPieceUntil(location, repository) &&
			Direction.bishopDirections().contains(calDirection(location));
	}

	@Override
	public String toSymbol() {
		return isWhite() ? "♗" : "♝";
	}
}
