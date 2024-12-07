package co.nemo.chess.domain;

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

	public static Location from(String position) {
		String[] split = position.split("");
		final int FILE_INDEX = 0;
		final int RANK_INDEX = 1;
		File file = File.from(split[FILE_INDEX]);
		Rank rank = new Rank(Integer.parseInt(split[RANK_INDEX]));
		return new Location(file, rank);
	}

	public Location adjustRank(int rank) {
		Rank newRank = this.rank.plus(rank);
		return new Location(this.file, newRank);
	}

	public Location withFile(File newFile) {
		return new Location(newFile, this.rank);
	}

	// 방향에 따른 열의 대각선 위치 계산
	public Location adjustDiagonal(int fileDirection, int fileDistance, int rowDirection, int rowDistance) {
		// 상좌, 상우, 하좌, 하우
		File newFile = this.file.adjustColumn(fileDirection, fileDistance);
		Rank newRank = this.rank.plus(rowDirection * rowDistance);
		return new Location(newFile, newRank);
	}

	@Override
	public String toString() {
		return String.format("%s%s", file, rank);
	}
}
