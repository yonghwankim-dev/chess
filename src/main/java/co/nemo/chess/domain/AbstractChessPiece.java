package co.nemo.chess.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class AbstractChessPiece {
	private final Location location;
	private final Color color;
	private final boolean isMoved;

	AbstractChessPiece(Location location, Color color, boolean isMoved) {
		this.location = location;
		this.color = color;
		this.isMoved = isMoved;
	}

	AbstractChessPiece newLocation(Location location) {
		return valueOf(location, color, false);
	}

	AbstractChessPiece withMoved() {
		return valueOf(location, color, true);
	}

	Location calMoveLocation(int distance) {
		Direction direction = getMoveDirection();
		return this.location.adjustRank(direction, distance);
	}

	boolean isSameColor(Color color) {
		return this.color == color;
	}

	AbstractChessPiece withLocation(Location location) {
		return valueOf(location, color, false);
	}

	AbstractChessPiece move(Location newLocation) {
		if (!canMove(newLocation)) {
			throw new IllegalArgumentException("Invalid move for " + getClass().getSimpleName());
		}
		return valueOf(newLocation, color, true);
	}

	Location adjustDiagonal(Direction direction, int fileDistance, int rankDistance) {
		return location.adjustDiagonal(direction, fileDistance, rankDistance);
	}

	int diffFile(Location location) {
		return this.location.diffFile(location);
	}

	int diffRank(Location location) {
		return this.location.diffRank(location);
	}

	boolean isMoved() {
		return this.isMoved;
	}

	Direction calDirection(Location location) {
		return this.location.calDirection(location);
	}

	abstract boolean canMove(Location newLocation);

	abstract AbstractChessPiece valueOf(Location location, Color color, boolean isMoved);

	abstract Direction getMoveDirection();

	@Override
	public String toString() {
		String moved = isMoved ? "MOVED" : "NOT MOVED";
		return String.format("%s %s %s", moved, color, location);
	}
}
