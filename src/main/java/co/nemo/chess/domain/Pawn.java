package co.nemo.chess.domain;

import org.springframework.cglib.core.Local;

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
		String[] split = position.split("");
		File file = File.from(split[0]);
		int rank = Integer.parseInt(split[1]);
		Location location = new Location(file, rank);
		return new Pawn(location, Color.WHITE, false);
	}

	public static Pawn valueOf(File file, int rank, Color color){
		Location location = new Location(file, rank);
		return new Pawn(location, color, false);
	}

	public static Pawn darkPawn(String position) {
		String[] split = position.split("");
		File file = File.from(split[0]);
		int rank = Integer.parseInt(split[1]);
		Location location = new Location(file, rank);
		return new Pawn(location, Color.DARK, true);
	}

	@Override
	public Pawn move() {
		int newRank;
		if (color == Color.WHITE){
			newRank = this.location.getRank() + 1;
		}else{
			newRank = this.location.getRank() - 1;
		}
		return Pawn.valueOf(this.location.getFile(), newRank, color).withMoved();
	}

	public Pawn moveTwoSquares() {
		if (isMoved){
			throw new IllegalStateException("pawn cannot move the two squares");
		}
		int newRank;
		if (color == Color.WHITE){
			newRank = this.location.getRank() + 2;
		}else{
			newRank = this.location.getRank() - 2;
		}
		return valueOf(this.location.getFile(), newRank, color).withMoved();
	}

	public Pawn withMoved(){
		return new Pawn(location, color, true);
	}

	@Override
	public String toString() {
		return String.format("%s %s", color, location);
	}
}
