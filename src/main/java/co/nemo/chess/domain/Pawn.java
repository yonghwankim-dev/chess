package co.nemo.chess.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Pawn implements Movable{

	private final Location location;
	private final Color color;
	private final boolean isMoved;

	private Pawn(Location location, Color color, boolean isMoved) {
		this.location = location;
		this.color = color;
		this.isMoved = isMoved;
	}

	public static Pawn whitePawn(String position){
		Location location = Location.from(position);
		return new Pawn(location, Color.WHITE, false);
	}

	public static Pawn darkPawn(String position) {
		Location location = Location.from(position);
		return new Pawn(location, Color.DARK, false);
	}

	private static Pawn valueOf(Location location, Color color){
		return new Pawn(location, color, false);
	}

	private static Pawn moved(Location location, Color color){
		return new Pawn(location, color, true);
	}

	@Override
	public Pawn move() {
		int distance = 1;
		Location newLocation = calMoveLocation(distance);
		return Pawn.valueOf(newLocation, color).withMoved();
	}

	private Location calMoveLocation(int distance) {
		int direction = getMoveDirection();
		int newRank = direction * distance;
		return this.location.adjustRank(newRank);
	}

	private int getMoveDirection(){
		final int UP = 1;
		final int DOWN = -1;
		return this.color == Color.WHITE ? UP : DOWN;
	}

	public Pawn moveTwoSquares() {
		if (isMoved){
			throw new IllegalStateException("pawn cannot move the two squares");
		}
		Location newLocation = calMoveLocation(2);
		return valueOf(newLocation, color).withMoved();
	}

	public Pawn withMoved(){
		return new Pawn(location, color, true);
	}

	public Pawn captureDiagonally() {
		int direction = getMoveDirection();
		int distance = 1;
		int newRank = direction * distance;

		File newFile = File.B;
		Location newLocation = this.location.adjustRank(newRank)
											.withFile(newFile);
		return moved(newLocation, color);
	}

	@Override
	public String toString() {
		return String.format("%s %s", color, location);
	}
}
