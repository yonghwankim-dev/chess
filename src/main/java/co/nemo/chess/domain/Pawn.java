package co.nemo.chess.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Pawn implements Movable {

	private final Location location;
	private final Color color;
	private final boolean isMoved;

	private Pawn(Location location, Color color, boolean isMoved) {
		this.location = location;
		this.color = color;
		this.isMoved = isMoved;
	}

	public static Pawn whitePawn(String position) {
		Location location = Location.from(position);
		return new Pawn(location, Color.WHITE, false);
	}

	public static Pawn darkPawn(String position) {
		Location location = Location.from(position);
		return new Pawn(location, Color.DARK, false);
	}

	private static Pawn valueOf(Location location, Color color) {
		return new Pawn(location, color, false);
	}

	private static Pawn moved(Location location, Color color) {
		return new Pawn(location, color, true);
	}

	private Pawn newLocation(Location location) {
		return valueOf(location, color);
	}

	@Override
	public Pawn move() {
		int distance = 1;
		return newLocation(calMoveLocation(distance)).withMoved();
	}

	private Location calMoveLocation(int distance) {
		Direction direction = getMoveDirection();
		return this.location.adjustRank(direction, distance);
	}

	private Direction getMoveDirection() {
		return this.color == Color.WHITE ? Direction.UP : Direction.DOWN;
	}

	public Pawn moveTwoSquares() {
		if (isMoved) {
			throw new IllegalStateException("pawn cannot move the two squares");
		}
		return newLocation(calMoveLocation(2)).withMoved();
	}

	public Pawn withMoved() {
		return new Pawn(location, color, true);
	}

	public Pawn moveDiagonally(Direction direction) {
		int rankDistance = 1;
		int fileDistance = 1;
		Location newLocation = this.location.adjustRank(direction, rankDistance)
			.adjustFile(direction, fileDistance);
		return moved(newLocation, color);
	}

	@Override
	public String toString() {
		return String.format("%s %s", color, location);
	}
}
