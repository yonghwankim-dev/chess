package co.nemo.chess.domain.piece;

import static co.nemo.chess.TestSupportUtils.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import co.nemo.chess.domain.board.PieceRepository;

class PawnTest {

	public static Stream<Arguments> validPromoToSource() {
		return Stream.of(
			Arguments.of("a8", Color.WHITE),
			Arguments.of("a1", Color.DARK)
		);
	}

	@Nested
	@DisplayName("Pawn move 테스트")
	class PawnMoveTest {

		private final PieceRepository repository = PieceRepository.empty();

		public static Stream<Arguments> validWhitePawnMoveLocations() {
			String source = "b2";
			String[] destinations = {"b3", "b4"};
			return Stream.of(createArgumentsArray(source, destinations));
		}

		public static Stream<Arguments> invalidWhitePawnMoveLocations() {
			String src = "b2";
			String[] destinations = {"a2", "a1", "b1", "c1", "c2", "b2"};
			return Stream.of(createArgumentsArray(src, destinations));
		}

		public static Stream<Arguments> validDarkPawnMoveLocations() {
			String src = "b7";
			String[] destinations = {"b6", "b5"};
			return Stream.of(createArgumentsArray(src, destinations));
		}

		public static Stream<Arguments> invalidDarkPawnMoveLocations() {
			String src = "b7";
			String[] destinations = {"a8", "a7", "c7", "c8", "b8", "b7"};
			return Stream.of(createArgumentsArray(src, destinations));
		}

		@DisplayName("백폰은 1칸 전진, 2칸 전진, 상좌, 상우 대각선 이동만 가능하다")
		@ParameterizedTest
		@MethodSource(value = "validWhitePawnMoveLocations")
		void givenWhitePawn_whenValidMoveLocations_thenReturnOfMovedPiece(String src, String dst) {
			// given
			Piece whitePawn = PieceFactory.getInstance().whitePawn(src);
			Location dstLocation = Location.from(dst);
			// when
			Piece actual = whitePawn.move(dstLocation, repository);
			// then
			Piece expected = PieceFactory.getInstance().whitePawn(dst).withMoved();
			assertThat(actual).isEqualTo(expected);
		}

		@DisplayName("백폰은 1칸 전진, 2칸 전진, 상좌, 상우 대각선 이동을 제외한 다른 이동은 불가능하다")
		@ParameterizedTest
		@MethodSource(value = "invalidWhitePawnMoveLocations")
		void givenWhitePawn_whenInvalidMoveLocations_thenThrowsException(String src, String dst) {
			// given
			Piece whitePawn = PieceFactory.getInstance().whitePawn(src);
			Location dstLocation = Location.from(dst);
			// when
			Throwable throwable = catchThrowable(() -> whitePawn.move(dstLocation, repository));
			// then
			assertThat(throwable)
				.isInstanceOf(IllegalArgumentException.class);
		}

		@DisplayName("흑폰은 1칸 전진, 2칸 전진, 하좌, 하우 대각선 이동만 가능하다")
		@ParameterizedTest
		@MethodSource(value = "validDarkPawnMoveLocations")
		void givenDarkPawn_whenValidMoveLocations_thenReturnOfMovedLocation(String src, String dst) {
			// given
			Piece darkPawn = PieceFactory.getInstance().darkPawn(src);
			Location dstLocation = Location.from(dst);
			// when
			Piece actual = darkPawn.move(dstLocation, repository);
			// then
			Piece expected = PieceFactory.getInstance().darkPawn(dst).withMoved();
			assertThat(actual).isEqualTo(expected);
		}

		@DisplayName("흑폰은 1칸 전진, 2칸 전진, 하좌, 화우 대각선 이동을 제외한 다른 이동은 불가능하다")
		@ParameterizedTest
		@MethodSource(value = "invalidDarkPawnMoveLocations")
		void givenDarkPawn_whenInvalidDirection_thenThrowsException(String src, String dst) {
			// given
			Piece darkPawn = PieceFactory.getInstance().darkPawn(src);
			Location dstLocation = Location.from(dst);
			// when
			Throwable throwable = catchThrowable(() -> darkPawn.move(dstLocation, repository));
			// then
			assertThat(throwable)
				.isInstanceOf(IllegalArgumentException.class);
		}

		@DisplayName("이미 이동한 백폰은 2칸 전진할 수 없다")
		@Test
		void givenWhitePawn_whenAlreadyMoved_thenNotTwoMoveSquare() {
			// given
			Piece pawn = PieceFactory.getInstance().whitePawn("a3").withMoved();
			Location dst = Location.from("a5");
			// when
			Throwable throwable = catchThrowable(() -> pawn.move(dst, repository));
			// then
			assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
		}

	}

	public static Stream<Arguments> validPawnPossibleLocations() {
		return Stream.of(
			Arguments.of("a2", Color.WHITE, new String[] {"a3", "a4", "b3"}),
			Arguments.of("a7", Color.DARK, new String[] {"a6", "a5", "b6"}),
			Arguments.of("b7", Color.DARK, new String[] {"b6", "b5", "a6", "c6"})
		);
	}

	@DisplayName("A2 백폰의 이동 가능한 경로를 계산한다")
	@ParameterizedTest
	@MethodSource(value = "validPawnPossibleLocations")
	void findPossibleLocations(String src, Color color, String[] expectedLocations) {
		// given
		Piece whitePawn = PieceFactory.getInstance().pawn(src, color);
		// when
		List<Location> locations = whitePawn.findAllMoveLocations();
		// then
		List<Location> expected = Arrays.stream(expectedLocations)
			.map(Location::from)
			.toList();
		assertThat(locations).containsExactlyElementsOf(expected);
	}

	@DisplayName("폰을 룩으로 프로모션한다")
	@ParameterizedTest
	@MethodSource(value = "validPromoToSource")
	void promoTo(String src, Color color) {
		// given
		Pawn whitePawn = (Pawn)PieceFactory.getInstance().pawn(src, color);
		// when
		Piece actual = whitePawn.promoTo(PieceType.ROOK);
		// then
		Piece expected = PieceFactory.getInstance().rook(src, color);
		Assertions.assertThat(actual).isEqualTo(expected);
	}
}
