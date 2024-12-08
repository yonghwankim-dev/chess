package co.nemo.chess.domain;

import org.apache.logging.log4j.util.Strings;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class Location {
	private final File file;
	private final Rank rank;

	private Location(File file, Rank rank) {
		this.file = file;
		this.rank = rank;
	}

	public static Location from(String location) {
		String[] split = location.split(Strings.EMPTY);
		final int FILE_INDEX = 0;
		final int RANK_INDEX = 1;
		File file = File.from(split[FILE_INDEX]);
		Rank rank = Rank.from(Integer.parseInt(split[RANK_INDEX]));
		return new Location(file, rank);
	}

	public Location adjustRank(Direction direction, int distance) {
		Rank newRank = this.rank.adjust(direction, distance);
		return new Location(this.file, newRank);
	}

	public Location adjustFile(Direction direction, int distance) {
		File newFile = this.file.adjust(direction, distance);
		return new Location(newFile, this.rank);
	}

	public Location adjustDiagonal(Direction direction, int fileDistance, int rankDistance) {
		File newFile = this.file.adjust(direction, fileDistance);
		Rank newRank = this.rank.adjust(direction, rankDistance);
		return new Location(newFile, newRank);
	}

	public LocationDifference diff(Location location) {
		return LocationDifference.from(this, location);
	}

	public int diffFile(Location location) {
		return this.file.diff(location.file);
	}

	public int diffRank(Location location) {
		return this.rank.diff(location.rank);
	}

	public Direction calDirection(Location location) {
		int fileDiff = location.diffFile(this);
		int rankDiff = location.diffRank(this);

		if (fileDiff == 0 && rankDiff == 0) {
			return Direction.SAME;
		}

		if (fileDiff == 0) {
			return rankDiff > 0 ? Direction.UP : Direction.DOWN;
		}
		if (rankDiff == 0) {
			return fileDiff > 0 ? Direction.RIGHT : Direction.LEFT;
		}

		if (Math.abs(fileDiff) == Math.abs(rankDiff)) {
			if (fileDiff < 0 && rankDiff > 0) {
				return Direction.UP_LEFT;
			} else if (fileDiff > 0 && rankDiff > 0) {
				return Direction.UP_RIGHT;
			} else if (fileDiff < 0 && rankDiff < 0) {
				return Direction.DOWN_LEFT;
			} else {
				return Direction.DOWN_RIGHT;
			}
		}
		return Direction.NO_DIRECTION;
	}

	@Override
	public String toString() {
		return String.format("%s%s", file, rank);
	}
}
