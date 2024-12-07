package co.nemo.chess.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class Location {
	private final File file;
	private final int rank;

	public Location(File file, int rank) {
		this.file = file;
		this.rank = rank;

		if (this.rank <= 0 || this.rank >= 9){
			throw new IllegalArgumentException("Invalid rank value: " + this.rank);
		}
	}

	public Location increaseRank(int rank) {
		return new Location(this.file, this.rank + rank);
	}

	public Location decreaseRank(int rank){
		return new Location(this.file, this.rank - rank);
	}

	@Override
	public String toString() {
		return String.format("%s%s", file, rank);
	}
}
