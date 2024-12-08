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

	@Override
	public String toString() {
		return String.format("fileDiff=%d, rankDiff=%d", fileDiff, rankDiff);
	}
}
