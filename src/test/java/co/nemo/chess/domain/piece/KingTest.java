package co.nemo.chess.domain.piece;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import co.nemo.chess.domain.board.PieceRepository;

class KingTest {

	private final PieceRepository repository = PieceRepository.empty();

	public static Stream<Arguments> validKingMoveSource() {
		Piece[] pieces = new AbstractChessPiece[] {};
		AbstractChessPiece e2DarkPawn = PieceFactory.getInstance().darkPawn("e2");
		return Stream.of(
			Arguments.of("e1", "e2", pieces),
			Arguments.of("e1", "d1", pieces),
			Arguments.of("e1", "f1", pieces),
			Arguments.of("e1", "d2", pieces),
			Arguments.of("e1", "f2", pieces),
			Arguments.of("e1", "e2", new AbstractChessPiece[] {e2DarkPawn})
		);
	}

	public static Stream<Arguments> invalidKingMoveSource() {
		AbstractChessPiece e2WhitePawn = PieceFactory.getInstance().whitePawn("e2");
		return Stream.of(
			Arguments.of("e1", "e2", new AbstractChessPiece[] {e2WhitePawn}),
			Arguments.of("e1", "e3", new AbstractChessPiece[] {}),
			Arguments.of("e1", "c1", new AbstractChessPiece[] {}),
			Arguments.of("e1", "g1", new AbstractChessPiece[] {}),
			Arguments.of("e1", "c3", new AbstractChessPiece[] {}),
			Arguments.of("e1", "g3", new AbstractChessPiece[] {})
		);
	}

	@DisplayName("백킹이 주어지고 특정 목적지로 이동을 수행한다")
	@ParameterizedTest
	@MethodSource(value = "validKingMoveSource")
	void givenWhiteKing_whenMovePiece_thenReturnMovedPiece(String src, String dst, Piece... pieces) {
		// given
		Piece whiteKing = PieceFactory.getInstance().whiteKing(src);
		repository.add(whiteKing);
		Arrays.stream(pieces).forEach(repository::add);
		Location dstLocation = Location.from(dst);
		// when
		Piece actual = whiteKing.move(dstLocation, repository).orElseThrow();
		// then
		Piece expected = PieceFactory.getInstance().whiteKing(dst).withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
		Assertions.assertThat(repository.findAll()).doesNotContain(whiteKing);
	}

	@DisplayName("백킹이 주어지고 특정 목적지로 이동할 수 없다")
	@ParameterizedTest
	@MethodSource(value = "invalidKingMoveSource")
	void givenWhiteKing_moveInvalidMovePiece_thenNotMovePiece(String src, String dst, Piece... pieces) {
		// given
		Piece whiteKing = PieceFactory.getInstance().whiteKing(src);
		repository.add(whiteKing);
		Arrays.stream(pieces).forEach(repository::add);
		Location dstLocation = Location.from(dst);
		// when
		Optional<AbstractChessPiece> actual = whiteKing.move(dstLocation, repository);
		// then
		Assertions.assertThat(actual).isEmpty();
	}
}
