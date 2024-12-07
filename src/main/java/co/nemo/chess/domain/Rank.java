package co.nemo.chess.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Rank {
	private final int value;

	public Rank(int value) {
		this.value = value;

		if (this.value <= 0 || this.value >= 9) {
			throw new IllegalArgumentException("Invalid rank value: " + this.value);
		}
	}

	public Rank plus(int value) {
		return new Rank(this.value + value);
	}
}
