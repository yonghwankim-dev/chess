package co.nemo.chess.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Pawn {

	private final File file;
	private final int rank;
	private final String color;

	public Pawn(File file, int rank, String color) {
		this.file = file;
		this.rank = rank;
		this.color = color;
	}

	public Pawn move() {
		if (color.equals("white")){
			return new Pawn(file, rank + 1, color);
		}
		return new Pawn(file, rank - 1, color);
	}
}
