package co.nemo.chess.domain.piece;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import co.nemo.chess.domain.board.PieceRepository;

class KnightTest {

	public static Stream<Arguments> validWhiteKnightFindAllLocationMoveSource() {
		return Stream.of(
			Arguments.of("b1", new String[] {"a3", "c3", "d2"}),
			Arguments.of("g1", new String[] {"f3", "h3", "e2"})
		);
	}

	public static Stream<Arguments> validWhiteKnightMoveSource() {
		return Stream.of(
			Arguments.of("b1", "a3"),
			Arguments.of("b1", "c3"),
			Arguments.of("b1", "d2"),
			Arguments.of("g1", "f3"),
			Arguments.of("g1", "h3"),
			Arguments.of("g1", "e2")
		);
	}

	@DisplayName("백 나이트의 이동 가능한 경로를 계산한다")
	@ParameterizedTest
	@MethodSource(value = "validWhiteKnightFindAllLocationMoveSource")
	void findAllMoveLocations(String src, String[] dsts) {
		// given
		AbstractChessPiece whiteKnight = PieceFactory.getInstance().whiteKnight(src);
		// when
		List<Location> actual = whiteKnight.findAllMoveLocations();
		// then
		List<Location> expected = Stream.of(dsts)
			.map(Location::from)
			.toList();
		Assertions.assertThat(actual).containsExactlyElementsOf(expected);
	}

	@DisplayName("백 나이트가 목적지로 이동 가능한지 계산한다")
	@ParameterizedTest
	@MethodSource(value = {"validWhiteKnightMoveSource"})
	void move(String src, String dst) {
		// given
		AbstractChessPiece whiteKnight = PieceFactory.getInstance().whiteKnight(src);
		PieceRepository repository = PieceRepository.empty();
		Location destination = Location.from(dst);
		// when
		Optional<AbstractChessPiece> actual = whiteKnight.move(destination, repository);
		// then
		AbstractChessPiece expected = PieceFactory.getInstance().whiteKnight(dst).withMoved();
		Assertions.assertThat(actual).contains(expected);
	}
}
