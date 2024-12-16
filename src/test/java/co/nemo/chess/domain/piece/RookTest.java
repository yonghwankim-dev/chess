package co.nemo.chess.domain.piece;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import co.nemo.chess.TestSupportUtils;
import co.nemo.chess.domain.board.PieceRepository;

class RookTest {

	private final PieceRepository repository = PieceRepository.empty();

	public static Stream<Arguments> validMoveLocations() {
		String source = "a1";
		String[] destinations = {"a2", "a3", "a4", "a5", "a6", "a7", "a8", "b1", "c1", "d1", "e1", "f1", "g1", "h1"};
		return Stream.of(TestSupportUtils.createArgumentsArray(source, destinations));
	}

	public static Stream<Arguments> invalidMoveLocations() {
		String source = "a1";
		String[] destinations = {"a1", "b2", "b3", "c2", "c3"};
		return Stream.of(TestSupportUtils.createArgumentsArray(source, destinations));
	}

	public static Stream<Arguments> validWhiteRookMoveLocationSource() {
		return Stream.of(
			Arguments.of("d5", "d8"),
			Arguments.of("d5", "a5"),
			Arguments.of("d5", "d1"),
			Arguments.of("d5", "h5")
		);
	}

	@DisplayName("룩은 직선상의 상,하,좌,우 방향으로만 이동이 가능하다")
	@ParameterizedTest
	@MethodSource(value = "validMoveLocations")
	void givenRook_whenMove_thenReturnOfPiece(String src, String dst) {
		// given
		Piece rook = PieceFactory.getInstance().whiteRook(src);
		Location dstLocation = Location.from(dst);
		// when
		Piece actual = rook.move(dstLocation, repository).orElseThrow();
		// then
		Piece expected = PieceFactory.getInstance().whiteRook(dst).withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("룩은 직선 방향외에 다른 방향으로 이동이 불가능하다")
	@ParameterizedTest
	@MethodSource(value = "invalidMoveLocations")
	void givenRook_whenInvalidMoveLocation_thenThrowsException(String src, String dst) {
		// given
		Piece rook = PieceFactory.getInstance().whiteRook(src);
		Location dstLocation = Location.from(dst);
		// when
		Optional<AbstractChessPiece> actual = rook.move(dstLocation, repository);
		// then
		Assertions.assertThat(actual).isEmpty();
	}

	@DisplayName("백룩과 흑백폰이 주어진 상태에서 직선 이동하여 흑백폰을 잡는다")
	@ParameterizedTest
	@MethodSource(value = "validWhiteRookMoveLocationSource")
	void givenWhiteRook_whenMoveRook_thenDeleteBackPawn(String rookSrc, String pawnSrc) {
		// given
		Piece whiteRook = PieceFactory.getInstance().whiteRook(rookSrc);
		Piece darkPawn = PieceFactory.getInstance().darkPawn(pawnSrc);
		repository.add(whiteRook);
		repository.add(darkPawn);
		Location destination = Location.from(pawnSrc);
		// when
		Piece actual = whiteRook.move(destination, repository).orElseThrow();
		// then
		Piece expected = PieceFactory.getInstance().whiteRook(pawnSrc)
			.withMoved()
			.withLocationHistory(new ArrayDeque<>(List.of(Location.from(rookSrc))));
		Assertions.assertThat(actual).isEqualTo(expected);
		Assertions.assertThat(repository.findAll()).doesNotContain(darkPawn);
	}

	@DisplayName("백룩이 주어진 상태에서 목적지에 같은 백색 기물이 존재하면 이동할 수 없다")
	@Test
	void givenRookAndSameColorPieceAtDestination_whenMovePiece_thenNotMovePiece() {
		// given
		Piece d5WhiteRook = PieceFactory.getInstance().whiteRook("d5");
		Piece d8WhitePawn = PieceFactory.getInstance().whitePawn("d8");
		repository.add(d5WhiteRook);
		repository.add(d8WhitePawn);
		Location destination = Location.from("d8");
		// when
		Optional<AbstractChessPiece> actual = d5WhiteRook.move(destination, repository);
		// then
		Assertions.assertThat(actual).isEmpty();
	}

	@DisplayName("백룩이 주어진 상태에서 목적지 경로 중간에 기물이 존재하면 이동할 수 없다")
	@Test
	void givenWhiteRook_whenExistPieceBetweenMoveLocations_thenNotMoveWhiteRook() {
		// given
		Piece d5WhitePawn = PieceFactory.getInstance().whiteRook("d5");
		Piece d8DarkPawn = PieceFactory.getInstance().darkPawn("d8");
		Piece d7DarkPawn = PieceFactory.getInstance().darkPawn("d7");
		repository.add(d5WhitePawn);
		repository.add(d8DarkPawn);
		repository.add(d7DarkPawn);
		Location destination = Location.from("d8");
		// when
		Optional<AbstractChessPiece> actual = d5WhitePawn.move(destination, repository);
		// then
		Assertions.assertThat(actual).isEmpty();
	}
}
