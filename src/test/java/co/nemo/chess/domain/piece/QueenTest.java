package co.nemo.chess.domain.piece;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import co.nemo.chess.domain.board.PieceRepository;

class QueenTest {

	public static Stream<Arguments> validWhiteQueenMoveSource() {
		AbstractChessPiece d8DarkPawn = PieceFactory.getInstance().darkPawn("d8");
		return Stream.of(
			Arguments.of(new AbstractChessPiece[] {d8DarkPawn}, "d1", "d8")
		);
	}

	public static Stream<Arguments> invalidWhiteQueenMoveSource() {
		AbstractChessPiece d7DarkPawn = PieceFactory.getInstance().darkPawn("d7");
		return Stream.of(
			Arguments.of(new AbstractChessPiece[] {d7DarkPawn}, "d1", "d8")
		);
	}

	@DisplayName("백퀸의 전체 이동가능 명령어를 계산한다")
	@Test
	void findAllMoveLocations() {
		// given
		AbstractChessPiece whiteQueen = PieceFactory.getInstance().whiteQueen("d1");
		// when
		List<Location> actual = whiteQueen.findAllMoveLocations();
		// then
		List<Location> expected = Stream.of("d2", "d3", "d4", "d5", "d6", "d7", "d8", "c1", "b1", "a1", "e1", "f1",
				"g1", "h1", "c2", "b3", "a4", "e2", "f3", "g4", "h5")
			.map(Location::from)
			.toList();
		Assertions.assertThat(actual).containsExactlyElementsOf(expected);
	}

	@DisplayName("백퀸을 이동시킨다")
	@ParameterizedTest
	@MethodSource(value = "validWhiteQueenMoveSource")
	void move(Piece[] pieces, String src, String dst) {
		// given
		AbstractChessPiece whiteQueen = PieceFactory.getInstance().whiteQueen(src);
		PieceRepository repository = PieceRepository.empty();
		Arrays.stream(pieces).forEach(repository::add);
		Location destination = Location.from(dst);
		// when
		Optional<AbstractChessPiece> actual = whiteQueen.move(destination, repository);
		// then
		AbstractChessPiece expected = PieceFactory.getInstance().whiteQueen(dst).withMoved();
		Assertions.assertThat(actual).contains(expected);
	}

	@DisplayName("백퀸이 이동시 중간 경로에 기물이 있으면 이동하지 못한다")
	@ParameterizedTest
	@MethodSource(value = "invalidWhiteQueenMoveSource")
	void givenWhiteQueen_whenExistPieceBetweenLocations_thenNotMoved(Piece[] pieces, String src, String dst) {
		// given
		AbstractChessPiece whiteQueen = PieceFactory.getInstance().whiteQueen(src);
		PieceRepository repository = PieceRepository.empty();
		Arrays.stream(pieces).forEach(repository::add);
		Location destination = Location.from(dst);
		// when
		Optional<AbstractChessPiece> actual = whiteQueen.move(destination, repository);
		// then
		Assertions.assertThat(actual).isEmpty();
	}
}
