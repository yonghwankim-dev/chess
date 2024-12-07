package co.nemo.chess.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LocationTest {

	@DisplayName("특정 위치에 Rank 값을 더하여 위치를 조정한다")
	@Test
	void adjustRank() {
		// given
		Location location = Location.from("a2");
		// when
		Location actual = location.adjustRank(1);
		// then
		Location expected = Location.from("a3");
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("특정 위치의 File열을 File 값으로 설정한다")
	@Test
	void withFile() {
		// given
		Location location = Location.from("a2");
		// when
		Location actual = location.withFile(File.B);
		// then
		Location expected = Location.from("b2");
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("특정 위치에서 오른쪽 대각선 방향으로 1칸 설정한다")
	@Test
	void calDiagonalLocationBy() {
		// given
		Location location = Location.from("a2");
		int fileDirection = 1;
		int fileDistance = 1;
		int rankDirection = 1;
		int rankDistance = 1;
		// when
		Location actual = location.adjustDiagonal(fileDirection, fileDistance, rankDirection, rankDistance);
		// then
		Location expected = Location.from("b3");
		Assertions.assertThat(actual).isEqualTo(expected);
	}
}
