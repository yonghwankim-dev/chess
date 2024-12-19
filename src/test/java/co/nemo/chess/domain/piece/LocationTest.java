package co.nemo.chess.domain.piece;

import static co.nemo.chess.domain.piece.Direction.*;

import java.util.Collections;
import java.util.List;
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

	public static Stream<Arguments> validKnightLocations() {
		return Stream.of(
			Arguments.of("d5", "c7", UP_UP_LEFT),
			Arguments.of("d5", "e7", UP_UP_RIGHT),
			Arguments.of("d5", "c3", DOWN_DOWN_LEFT),
			Arguments.of("d5", "e3", DOWN_DOWN_RIGHT),
			Arguments.of("d5", "b6", LEFT_LEFT_UP),
			Arguments.of("d5", "b4", LEFT_LEFT_DOWN),
			Arguments.of("d5", "f6", RIGHT_RIGHT_UP),
			Arguments.of("d5", "f4", RIGHT_RIGHT_DOWN)
		);
	}

	public static Stream<Arguments> calBetweenLocationSource() {
		Location a2 = Location.from("a2");
		Location a3 = Location.from("a3");
		Location a4 = Location.from("a4");
		Location a5 = Location.from("a5");
		Location a6 = Location.from("a6");
		Location a7 = Location.from("a7");
		Location a8 = Location.from("a8");
		Location b3 = Location.from("b3");
		Location c3 = Location.from("c3");
		Location c4 = Location.from("c4");
		return Stream.of(
			Arguments.of(a2, a3, List.of(a3)),
			Arguments.of(a2, a4, List.of(a3, a4)),
			Arguments.of(a2, a8, List.of(a3, a4, a5, a6, a7, a8)),
			Arguments.of(a2, c4, List.of(b3, c4)),
			Arguments.of(a2, a2, Collections.emptyList()),
			Arguments.of(a2, c3, List.of(c3))
		);
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

	@DisplayName("출발지와 목적지를 주어진 상태에서 나이트의 방향성을 계산한다")
	@ParameterizedTest
	@MethodSource(value = "validKnightLocations")
	void givenLocations_whenCalDirection_thenReturnDirectionOfKnightDirection(String src, String dst,
		Direction expected) {
		// given
		Location srcLocation = Location.from(src);
		// when
		Direction actual = srcLocation.calDirection(Location.from(dst));
		// then
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("두 위치간에 중간 경로를 반환한다")
	@ParameterizedTest
	@MethodSource(value = "calBetweenLocationSource")
	void calBetweenLocations(Location src, Location dst, List<Location> expected) {
		// given

		// when
		List<Location> actual = src.calBetweenLocations(dst);
		// then
		Assertions.assertThat(actual)
			.containsExactlyElementsOf(expected);
	}
}
