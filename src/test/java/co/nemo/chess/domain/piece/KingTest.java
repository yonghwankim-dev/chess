package co.nemo.chess.domain.piece;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
			Arguments.of("e1", "c1", pieces),
			Arguments.of("e1", "g1", pieces),
			Arguments.of("e1", "f1", pieces),
			Arguments.of("e1", "d2", pieces),
			Arguments.of("e1", "f2", pieces),
			Arguments.of("e1", "e2", new AbstractChessPiece[] {e2DarkPawn})
		);
	}

	public static Stream<Arguments> invalidKingMoveSource() {
		AbstractChessPiece e2WhitePawn = PieceFactory.getInstance().whitePawn("e2");
		AbstractChessPiece f1WhitePawn = PieceFactory.getInstance().whitePawn("f1");
		AbstractChessPiece d1WhitePawn = PieceFactory.getInstance().whitePawn("d1");
		return Stream.of(
			Arguments.of("e1", "e2", new AbstractChessPiece[] {e2WhitePawn}),
			Arguments.of("e1", "e3", new AbstractChessPiece[] {}),
			Arguments.of("e1", "c3", new AbstractChessPiece[] {}),
			Arguments.of("e1", "g3", new AbstractChessPiece[] {}),
			Arguments.of("e1", "g1", new AbstractChessPiece[] {f1WhitePawn}),
			Arguments.of("e1", "c1", new AbstractChessPiece[] {d1WhitePawn})
		);
	}

	public static Stream<Arguments> validKingMoveInCheckedSource() {
		AbstractChessPiece a1DarkRook = PieceFactory.getInstance().darkRook("a1");
		return Stream.of(
			Arguments.of("e1", "d2", new AbstractChessPiece[] {a1DarkRook}),
			Arguments.of("e1", "e2", new AbstractChessPiece[] {a1DarkRook}),
			Arguments.of("e1", "f2", new AbstractChessPiece[] {a1DarkRook})
		);
	}

	public static Stream<Arguments> invalidKingMoveInCheckedSource() {
		AbstractChessPiece a1DarkRook = PieceFactory.getInstance().darkRook("a1");
		return Stream.of(
			Arguments.of("e1", "d1", new AbstractChessPiece[] {a1DarkRook}),
			Arguments.of("e1", "f1", new AbstractChessPiece[] {a1DarkRook})
		);
	}

	public static Stream<Arguments> validCheckmateSource() {
		King e1WhiteKing = (King)PieceFactory.getInstance().whiteKing("e1");
		AbstractChessPiece e1DarkQueen = PieceFactory.getInstance().darkQueen("e3");
		AbstractChessPiece d1DarkRook = PieceFactory.getInstance().darkRook("d1");
		AbstractChessPiece f1DarkRook = PieceFactory.getInstance().darkRook("f1");
		return Stream.of(
			Arguments.of(e1WhiteKing, new AbstractChessPiece[] {e1DarkQueen, d1DarkRook, f1DarkRook})
		);
	}

	public static Stream<Arguments> noCheckmateSource() {
		King e1WhiteKing = (King)PieceFactory.getInstance().whiteKing("e1");
		AbstractChessPiece e1DarkQueen = PieceFactory.getInstance().darkQueen("g2");
		AbstractChessPiece d1DarkRook = PieceFactory.getInstance().darkRook("a1");
		AbstractChessPiece d3WhiteRook = PieceFactory.getInstance().whiteRook("d3");
		AbstractChessPiece a3WhiteRook = PieceFactory.getInstance().whiteRook("a3");
		return Stream.of(
			Arguments.of(e1WhiteKing, new AbstractChessPiece[] {e1DarkQueen, d1DarkRook, d3WhiteRook}),
			Arguments.of(e1WhiteKing, new AbstractChessPiece[] {e1DarkQueen, d1DarkRook, a3WhiteRook})
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
	void givenWhiteKing_whenMoveInvalidMovePiece_thenNotMovePiece(String src, String dst, Piece... pieces) {
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

	@DisplayName("체크 상태인 백킹은 이동해서 체크 상태를 벗어난다")
	@ParameterizedTest
	@MethodSource(value = "validKingMoveInCheckedSource")
	void givenWhiteKingInCheckedStatus_whenMovePiece_thenReturnMovedPiece(String src, String dst, Piece... pieces) {
		// given
		Piece whiteKing = PieceFactory.getInstance().whiteKing(src);
		repository.add(whiteKing);
		Arrays.stream(pieces).forEach(repository::add);
		Location dstLocation = Location.from(dst);
		// when
		AbstractChessPiece actual = whiteKing.move(dstLocation, repository).orElseThrow();
		// then
		AbstractChessPiece expected = PieceFactory.getInstance().whiteKing(dst).withMoved();
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("체크 상태인 백킹은 특정 목적지로 이동할 수 없다")
	@ParameterizedTest
	@MethodSource(value = "invalidKingMoveInCheckedSource")
	void givenWhiteKingInCheckedStatus_whenInvalidMovePiece_thenNotMovedPiece(String src, String dst, Piece... pieces) {
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

	@DisplayName("백킹이 주어지고 백킹은 현재 체크 상태이다")
	@Test
	void givenWhiteKing_whenIsCheckedStatus_thenReturnTrue() {
		// given
		King whiteKing = (King)PieceFactory.getInstance().whiteKing("e1");
		Piece a1DarkRook = PieceFactory.getInstance().darkRook("a1");
		repository.add(whiteKing);
		repository.add(a1DarkRook);
		// when
		boolean actual = whiteKing.isCheckedStatus(repository);
		// then
		Assertions.assertThat(actual).isTrue();
	}

	@DisplayName("백킹과 다른 기물이 주어지고 백킹은 체크메이트 상태가 된다")
	@ParameterizedTest
	@MethodSource(value = "validCheckmateSource")
	void checkmate(Piece king, Piece[] other) {
		// given
		repository.add(king);
		Arrays.stream(other).forEach(repository::add);
		// when
		boolean actual = ((King)king).isCheckmate(repository);
		// then
		Assertions.assertThat(actual).isTrue();
	}

	@DisplayName("백킹과 다른 기물이 주어지고 백색 다른 기물로 막을 수 있어서 체크메이트 상태가 아니다")
	@ParameterizedTest
	@MethodSource(value = "noCheckmateSource")
	void givenPieces_whenBlockingPiece_thenNotCheckmate(Piece king, Piece[] other) {
		// given
		repository.add(king);
		Arrays.stream(other).forEach(repository::add);
		// when
		boolean actual = ((King)king).isCheckmate(repository);
		// then
		Assertions.assertThat(actual).isFalse();
	}
}
