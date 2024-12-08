package co.nemo.chess.domain;

public class LocationDifference {
	private final int fileDiff;
	private final int rankDiff;

	private LocationDifference(int fileDiff, int rankDiff) {
		this.fileDiff = fileDiff;
		this.rankDiff = rankDiff;
	}

	public static LocationDifference from(Location src, Location dst) {
		int fileDiff = src.diffFile(dst);
		int rankDiff = src.diffRank(dst);
		return new LocationDifference(fileDiff, rankDiff);
	}

	public boolean isEqualDistance(int fileDiff, int rankDiff) {
		return Math.abs(this.fileDiff) == Math.abs(fileDiff) && Math.abs(this.rankDiff) == Math.abs(rankDiff);
	}

	public Direction calDirection() {
		if (isSame()) {
			return Direction.SAME;
		}
		if (isVertical()) {
			return rankDiff > 0 ? Direction.UP : Direction.DOWN;
		}
		if (isHorizontal()) {
			return fileDiff > 0 ? Direction.RIGHT : Direction.LEFT;
		}
		if (isDiagonal()) {
			return calculateDiagonalDirection();
		}
		return Direction.NO_DIRECTION;
	}

	private boolean isSame() {
		return fileDiff == 0 && rankDiff == 0;
	}

	private boolean isVertical() {
		return fileDiff == 0;
	}

	private boolean isHorizontal() {
		return rankDiff == 0;
	}

	private boolean isDiagonal() {
		return Math.abs(fileDiff) == Math.abs(rankDiff);
	}

	private Direction calculateDiagonalDirection() {
		if (fileDiff < 0 && rankDiff > 0) {
			return Direction.UP_LEFT;
		}
		if (fileDiff > 0 && rankDiff > 0) {
			return Direction.UP_RIGHT;
		}
		if (fileDiff < 0 && rankDiff < 0) {
			return Direction.DOWN_LEFT;
		}
		return Direction.DOWN_RIGHT; // fileDiff > 0 && rankDiff < 0
	}

	@Override
	public String toString() {
		return String.format("fileDiff=%d, rankDiff=%d", fileDiff, rankDiff);
	}
}
