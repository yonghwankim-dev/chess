package co.nemo.chess.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Location {
	private final File file;
	private final int rank;

	public Location(File file, int rank) {
		this.file = file;
		this.rank = rank;
	}
}
