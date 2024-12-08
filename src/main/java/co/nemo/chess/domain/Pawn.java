package co.nemo.chess.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Pawn implements ForwardMovable, DiagonalMovable {

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

	private Pawn newLocation(Location location) {
		return valueOf(location, color);
	}

	@Override
	public Pawn moveForwardly() {
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

	@Override
	public Pawn moveDiagonally(Direction direction) {
		if (color == Color.WHITE && direction != Direction.UP_LEFT && direction != Direction.UP_RIGHT) {
			throw new IllegalArgumentException("The White Pawn can only move in the UP_LEFT or UP_RIGHT directions.");
		} else if (color == Color.DARK && direction != Direction.DOWN_LEFT && direction != Direction.DOWN_RIGHT) {
			throw new IllegalArgumentException(
				"The Dark Pawn can only move in the DOWN_LEFT or DOWN_RIGHT directions.");
		}
		int rankDistance = 1;
		int fileDistance = 1;
		Location newLocation = this.location.adjustDiagonal(direction, fileDistance, rankDistance);
		return this
			.withLocation(newLocation)
			.withMoved();
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

	public Pawn withLocation(Location location) {
		return new Pawn(location, color, true);
	}

	@Override
	public String toString() {
		return String.format("%s %s", color, location);
	}
}
