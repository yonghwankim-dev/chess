package co.nemo.chess.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Pawn {

	private final File file;
	private final int rank;
	private final Color color;
	private final boolean isMoved;

	private Pawn(File file, int rank, Color color, boolean isMoved) {
		this.file = file;
		this.rank = rank;
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
		return new Pawn(file, rank, Color.WHITE, false);
	}

	public static Pawn valueOf(File file, int rank, Color color){
		return new Pawn(file, rank, color, false);
	}

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
		return new Pawn(file, rank, color, true);
	}

	@Override
	public String toString() {
		return String.format("%s %s%s", color, file, rank);
	}
}
