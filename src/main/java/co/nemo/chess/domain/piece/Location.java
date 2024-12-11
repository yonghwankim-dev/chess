package co.nemo.chess.domain.piece;

import java.util.Optional;

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
		return location.diff(this).calDirection();
	}

	public Optional<Location> plus(int fileValue, int rankValue) {
		File newFile = this.file.plus(fileValue).orElse(null);
		Rank newRank = this.rank.plus(rankValue).orElse(null);
		if (newFile == null || newRank == null) {
			return Optional.empty();
		}
		return Optional.of(new Location(newFile, newRank));
	}

	@Override
	public String toString() {
		return String.format("%s%s", file, rank);
	}
}
