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

	@Override
	public Pawn move() {
		int direction = getMoveDirection();
		int distance = 1;
		int newRank = direction * distance;
		Location newLocation = this.location.adjustRank(newRank);
		return Pawn.valueOf(newLocation, color).withMoved();
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
		int direction = getMoveDirection();
		int distance = 2;
		int newRank = direction * distance;
		Location newLocation = this.location.adjustRank(newRank);
		return valueOf(newLocation, color).withMoved();
	}

	public Pawn withMoved(){
		return new Pawn(location, color, true);
	}

	@Override
	public String toString() {
		return String.format("%s %s", color, location);
	}
}
