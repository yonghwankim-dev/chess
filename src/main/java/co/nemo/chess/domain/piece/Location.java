package co.nemo.chess.domain.piece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

	public static Location of(File file, Rank rank) {
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

	public Optional<Location> calLocation(Direction direction, int distance) {
		File newFile = direction.calFile(file, distance).orElse(null);
		Rank newRank = direction.calRank(rank, distance).orElse(null);
		if (newFile == null || newRank == null) {
			return Optional.empty();
		}
		return Optional.of(Location.of(newFile, newRank));
	}

	public boolean isOnRank(Rank rank) {
		return this.rank.equals(rank);
	}

	public String toPositionText() {
		return rank.toPositionText(file);
	}

	public List<Location> calBetweenLocations(Location destination) {
		Direction direction = this.calDirection(destination);
		if (direction == Direction.NO_DIRECTION) {
			return Collections.emptyList();
		}

		File curFile = file;
		Rank curRank = rank;
		int distance = 1;
		List<Location> result = new ArrayList<>();
		while (!curFile.equals(destination.file) ||
			!curRank.equals(destination.rank)) {
			curFile = direction.calFile(curFile, distance).orElseThrow();
			curRank = direction.calRank(curRank, distance).orElseThrow();
			result.add(Location.of(curFile, curRank));
		}
		return result;
	}

	@Override
	public String toString() {
		return String.format("%s%s", file, rank);
	}
}
