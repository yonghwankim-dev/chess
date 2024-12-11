package co.nemo.chess.domain.piece;

import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode
@Slf4j
public abstract class AbstractChessPiece implements Piece {
	private final Location location;
	private final Color color;
	private final boolean isMoved;

	AbstractChessPiece(Location location, Color color, boolean isMoved) {
		this.location = location;
		this.color = color;
		this.isMoved = isMoved;
	}

	public AbstractChessPiece withMoved() {
		return movedPiece(location, color);
	}

	boolean isSameColor(Color color) {
		return this.color == color;
	}

	@Override
	public AbstractChessPiece move(Location destination) throws IllegalArgumentException {
		if (!canMove(destination)) {
			throw new IllegalArgumentException("Invalid move for " + getClass().getSimpleName());
		}
		return movedPiece(destination, color);
	}

	@Override
	public boolean match(Location location) {
		return this.location.equals(location);
	}

	LocationDifference diffLocation(Location location) {
		return this.location.diff(location);
	}

	boolean isNotMoved() {
		return !this.isMoved;
	}

	Direction calDirection(Location location) {
		return this.location.calDirection(location);
	}

	Optional<Location> calLocation(int fileValue, int rankValue) {
		return this.location.plus(fileValue, rankValue);
	}

	public abstract boolean canMove(Location newLocation);

	abstract AbstractChessPiece movedPiece(Location location, Color color);

	@Override
	public String toString() {
		String moved = isMoved ? "MOVED" : "NOT MOVED";
		return String.format("%s %s %s", moved, color, location);
	}
}
