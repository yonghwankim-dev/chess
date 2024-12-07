package co.nemo.chess.domain;

import org.springframework.cglib.core.Local;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Pawn implements Movable{

	private final File file;
	private final int rank;
	private final Location location;
	private final Color color;
	private final boolean isMoved;

	private Pawn(File file, int rank, Location location, Color color, boolean isMoved) {
		this.file = file;
		this.rank = rank;
		this.location = location;
		this.color = color;
		this.isMoved = isMoved;

		if (this.rank <= 0 || this.rank >= 9){
			throw new IllegalArgumentException("Invalid rank value: " + this.rank);
		}
	}

	public static Pawn whitePawn(String position){
		String[] split = position.split("");
		File file = File.from(split[0]);
		int rank = Integer.parseInt(split[1]);
		Location location = new Location(file, rank);
		return new Pawn(file, rank, location, Color.WHITE, false);
	}

	public static Pawn valueOf(File file, int rank, Color color){
		Location location = new Location(file, rank);
		return new Pawn(file, rank, location, color, false);
	}

	public static Pawn darkPawn(String position) {
		String[] split = position.split("");
		File file = File.from(split[0]);
		int rank = Integer.parseInt(split[1]);
		Location location = new Location(file, rank);
		return new Pawn(file, rank, location, Color.DARK, true);
	}

	@Override
	public Pawn move() {
		int newRank;
		if (color == Color.WHITE){
			newRank = this.rank + 1;
		}else{
			newRank = this.rank - 1;
		}
		return Pawn.valueOf(file, newRank, color).withMoved();
	}

	public Pawn moveTwoSquares() {
		if (isMoved){
			throw new IllegalStateException("pawn cannot move the two squares");
		}
		int newRank;
		if (color == Color.WHITE){
			newRank = this.rank + 2;
		}else{
			newRank = this.rank - 2;
		}
		return valueOf(file, newRank, color).withMoved();
	}

	public Pawn withMoved(){
		return new Pawn(file, rank, location, color, true);
	}

	@Override
	public String toString() {
		return String.format("%s %s%s", color, file, rank);
	}
}
