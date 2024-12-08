package co.nemo.chess.domain.piece;

import static co.nemo.chess.domain.piece.Direction.*;

import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LocationTest {

	public static Stream<Arguments> invalidLocation() {
		return Stream.of(
			Arguments.of("a0"),
			Arguments.of("a9"),
			Arguments.of("i1"),
			Arguments.of("i0"),
			Arguments.of("i9")
		);
	}

	public static Stream<Arguments> validLocationSources() {
		int start = 1;
		int end = 8;
		Arguments[] argumentsArray = new Arguments[File.values().length * 8];
		int idx = 0;
		for (File file : File.values()) {
			for (int i = start; i <= end; i++) {
				Rank rank = Rank.from(i);
				Arguments arguments = Arguments.of(file.name() + rank);
				argumentsArray[idx++] = arguments;
			}
		}
		return Stream.of(argumentsArray);
	}

	public static Stream<Arguments> validLocationWithDirection() {
		String source = "d5";
		String[] destinations = {"d6", "d4", "c5", "e5", "c6", "e6", "c4", "e4", "d5"};
		Direction[] expectedDirections = {UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT, SAME};
		int n = destinations.length;
		Arguments[] argumentsArray = new Arguments[n];
		for (int i = 0; i < n; i++) {
			argumentsArray[i] = Arguments.of(source, destinations[i], expectedDirections[i]);
		}
		return Stream.of(argumentsArray);
	}

	public static Stream<Arguments> notCalLocations() {
		String source = "d5";
		String[] destinations = {"c7", "e7", "f6", "f4", "e3", "c3", "b4", "b6"};
		int n = destinations.length;
		Arguments[] argumentsArray = new Arguments[n];
		for (int i = 0; i < n; i++) {
			argumentsArray[i] = Arguments.of(source, destinations[i]);
		}
		return Stream.of(argumentsArray);
	}

	@DisplayName("위치의 File(열)은 a~h 사이어야 하고, Rank(행)은 1~8 사이어야 한다")
	@ParameterizedTest
	@MethodSource(value = "validLocationSources")
	void givenLocation_whenCreateLocation_thenReturnOfInstance(String dst) {
		// given

		// when
		Location actual = Location.from(dst);
		// then
		Assertions.assertThat(actual).isNotNull();
	}

	@DisplayName("위치의 File(열)은 a~h 사이어야 하고, Rank(행)은 1~8 사이가 아닌 위치는 생성할 수 없다")
	@ParameterizedTest
	@MethodSource(value = "invalidLocation")
	void givenInvalidLocation_whenCreateLocation_thenThrowsException(String dst) {
		// given
		// when
		Throwable throwable = Assertions.catchThrowable(() -> Location.from(dst));
		// then
		Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("두 위치간에 방향성을 계산한다")
	@ParameterizedTest
	@MethodSource(value = "validLocationWithDirection")
	void calDirection(String src, String dst, Direction expected) {
		// given
		Location srcLocation = Location.from(src);
		// when
		Direction actual = srcLocation.calDirection(Location.from(dst));
		// then
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("두 위치간에 방향성을 계산할 수 없으면 NO_DIRECTION을 반환한다")
	@ParameterizedTest
	@MethodSource(value = "notCalLocations")
	void givenLocations_whenCalDirection_thenReturnOfNoDirection(String src, String dst) {
		// given
		Location srcLocation = Location.from(src);
		// when
		Direction actual = srcLocation.calDirection(Location.from(dst));
		// then
		Assertions.assertThat(actual).isEqualTo(NO_DIRECTION);
	}
}
