package co.nemo.chess.domain.piece;

import java.util.List;
import java.util.Optional;

public enum Direction {
	UP(0, 1),
	DOWN(0, -1),
	LEFT(-1, 0),
	RIGHT(1, 0),
	UP_LEFT(-1, 1),
	UP_RIGHT(1, 1),
	DOWN_LEFT(-1, -1),
	DOWN_RIGHT(1, -1),

	UP_UP_LEFT(-1, 2),
	UP_UP_RIGHT(1, 2),
	DOWN_DOWN_LEFT(-1, -2),
	DOWN_DOWN_RIGHT(1, -2),
	LEFT_LEFT_DOWN(-2, -1),
	LEFT_LEFT_UP(-2, 1),
	RIGHT_RIGHT_UP(2, 1),
	RIGHT_RIGHT_DOWN(2, -1),

	SAME(0, 0),
	NO_DIRECTION(0, 0);

	private final int fileDirection;
	private final int rankDirection;

	Direction(int fileDirection, int rankDirection) {
		this.fileDirection = fileDirection;
		this.rankDirection = rankDirection;
	}

	public Optional<File> calFile(File curFile, int distance) {
		int value = this.fileDirection * distance;
		return curFile.plus(value);
	}

	public Optional<Rank> calRank(Rank curRank, int distance) {
		int value = this.rankDirection * distance;
		return curRank.plus(value);
	}

	public static List<Direction> knightDirections() {
		return List.of(
			UP_UP_LEFT,
			UP_UP_RIGHT,
			DOWN_DOWN_LEFT,
			DOWN_DOWN_RIGHT,
			LEFT_LEFT_UP,
			LEFT_LEFT_DOWN,
			RIGHT_RIGHT_UP,
			RIGHT_RIGHT_DOWN
		);
	}

	public boolean isEqualDistance(int fileDiff, int rankDiff) {
		return this.fileDirection == fileDiff && this.rankDirection == rankDiff;
	}
}
