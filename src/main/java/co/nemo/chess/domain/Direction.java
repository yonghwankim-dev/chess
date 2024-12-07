package co.nemo.chess.domain;

import lombok.Getter;

@Getter
public enum Direction {
	UP(0, 1),
	DOWN(0, -1),
	LEFT(-1, 0),
	RIGHT(1, 0),
	UP_LEFT(-1, 1),
	UP_RIGHT(1, 1),
	DOWN_LEFT(-1, -1),
	DOWN_RIGHT(1, -1);

	private final int fileDirection;
	private final int rankDirection;

	Direction(int fileDirection, int rankDirection) {
		this.fileDirection = fileDirection;
		this.rankDirection = rankDirection;
	}

	public int calFileDistance(int value) {
		return this.fileDirection * value;
	}

	public int calRankDistance(int value) {
		return this.rankDirection * value;
	}
}