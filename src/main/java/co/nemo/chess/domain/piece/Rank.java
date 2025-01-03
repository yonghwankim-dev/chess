package co.nemo.chess.domain.piece;

import java.util.Optional;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Rank {
	private final int value;

	private Rank(int value) {
		this.value = value;

		if (this.value <= 0 || this.value >= 9) {
			throw new IllegalArgumentException("Invalid rank value: " + this.value);
		}
	}

	public static Rank from(int value) {
		return new Rank(value);
	}

	public int diff(Rank rank) {
		return this.value - rank.value;
	}

	public Optional<Rank> plus(int distance) {
		try {
			return Optional.of(from(this.value + distance));
		} catch (IllegalArgumentException e) {
			return Optional.empty();
		}
	}

	public String toPositionText(File file) {
		return file.name() + this.value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
