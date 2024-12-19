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

class BishopTest {

	public static Stream<Arguments> validWhiteBishopMoveSource() {
		AbstractChessPiece h6DarkPawn = PieceFactory.getInstance().darkPawn("h6");
		AbstractChessPiece a3DarkPawn = PieceFactory.getInstance().darkPawn("a3");
		return Stream.of(
			Arguments.of(new AbstractChessPiece[] {h6DarkPawn}, "c1", "h6"),
			Arguments.of(new AbstractChessPiece[] {a3DarkPawn}, "c1", "a3")
		);
	}

	public static Stream<Arguments> invalidWhiteBishopMoveSource() {
		AbstractChessPiece g5DarkPawn = PieceFactory.getInstance().darkPawn("g5");
		return Stream.of(
			Arguments.of(new AbstractChessPiece[] {g5DarkPawn}, "c1", "h6")
		);
	}

	@DisplayName("백비숍의 이동 가능한 경로를 계산한다")
	@Test
	void findAllMoveLocations() {
		// given
		Piece bishop = PieceFactory.getInstance().whiteBishop("c1");
		// when
		List<Location> actual = bishop.findAllMoveLocations();
		// then
		List<Location> expected = Stream.of("b2", "a3", "d2", "e3", "f4", "g5", "h6")
			.map(Location::from)
			.toList();
		Assertions.assertThat(actual).containsExactlyElementsOf(expected);
	}

	@DisplayName("백비숍을 이동시킨다")
	@ParameterizedTest
	@MethodSource(value = "validWhiteBishopMoveSource")
	void move(Piece[] pieces, String src, String dst) {
		// given
		Piece bishop = PieceFactory.getInstance().whiteBishop(src);
		Location destination = Location.from(dst);
		PieceRepository repository = PieceRepository.empty();
		Arrays.stream(pieces).forEach(repository::add);
		// when
		Piece actual = bishop.move(destination, repository).orElseThrow();
		// then
		Piece expected = PieceFactory.getInstance().whiteBishop(dst).withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
		Assertions.assertThat(repository.findAll()).hasSize(1);
	}

	@DisplayName("백비숍이 주어지고 경로 중간에 기물이 존재하면 이동할 수 없다")
	@ParameterizedTest
	@MethodSource(value = "invalidWhiteBishopMoveSource")
	void givenWhiteBishop_whenExistPieceBetweenLocations_thenNotMoved(Piece[] pieces, String src, String dst) {
		// given
		Piece bishop = PieceFactory.getInstance().whiteBishop(src);
		Location destination = Location.from(dst);
		PieceRepository repository = PieceRepository.empty();
		Arrays.stream(pieces).forEach(repository::add);
		// when
		Optional<AbstractChessPiece> actual = bishop.move(destination, repository);
		// then
		Assertions.assertThat(actual).isEmpty();
	}
}
