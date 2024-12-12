package co.nemo.chess.domain.board;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import co.nemo.chess.domain.piece.Color;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.piece.PieceFactory;

class BoardTest {

	public static Stream<Arguments> validNotMovedPawnPossiblePaths() {
		return Stream.of(
			Arguments.of("a2", Color.WHITE, new String[] {"a3", "a4"}),
			Arguments.of("a7", Color.DARK, new String[] {"a6", "a5"})
		);
	}

	public static Stream<Arguments> validMovedPawnPossiblePaths() {
		return Stream.of(
			Arguments.of("a3", Color.WHITE, new String[] {"a4"}),
			Arguments.of("a8", Color.WHITE, new String[] {}),
			Arguments.of("a6", Color.DARK, new String[] {"a5"})
		);
	}

	public static Stream<Arguments> validPawnMoveLocations() {
		Piece a2WhitePawn = PieceFactory.getInstance().pawn("a2", Color.WHITE);
		Piece a7DarkPawn = PieceFactory.getInstance().pawn("a7", Color.DARK);

		Piece a3WhitePawn = PieceFactory.getInstance().pawn("a3", Color.WHITE).withMoved();
		Piece a4WhitePawn = PieceFactory.getInstance().pawn("a4", Color.WHITE).withMoved();
		Piece a5DarkPawn = PieceFactory.getInstance().pawn("a5", Color.DARK).withMoved();
		Piece a6DarkPawn = PieceFactory.getInstance().pawn("a6", Color.DARK).withMoved();
		return Stream.of(
			Arguments.of(a2WhitePawn, "a2", "a3", a3WhitePawn),
			Arguments.of(a2WhitePawn, "a2", "a4", a4WhitePawn),
			Arguments.of(a7DarkPawn, "a7", "a5", a5DarkPawn),
			Arguments.of(a7DarkPawn, "a7", "a6", a6DarkPawn)
		);
	}

	private PieceRepository repository;

	@BeforeEach
	void setUp() {
		repository = PieceRepository.empty();
	}

	@DisplayName("보드판 위에 이동하지 않은 폰의 이동 가능 경로를 계산한다")
	@ParameterizedTest
	@MethodSource(value = "validNotMovedPawnPossiblePaths")
	void findPossiblePaths(String src, Color color, String[] expectedPositions) {
		// given
		Piece pawn = PieceFactory.getInstance().pawn(src, color);
		PieceMovable board = Board.init(repository, pawn);

		Location srcLocation = Location.from(src);
		// when
		List<Location> actual = board.findPossiblePaths(srcLocation);
		// then
		List<Location> expected = Arrays.stream(expectedPositions)
			.map(Location::from)
			.toList();
		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@DisplayName("보드판 위에 이미 이동한 폰의 이동 기능 경로를 계산한다")
	@ParameterizedTest
	@MethodSource(value = "validMovedPawnPossiblePaths")
	void givenPawn_whenPossiblePathsForAlreadyMovedPawn_thenReturnOfLocations(String src, Color color,
		String[] expectedPositions) {
		// given
		Piece pawn = PieceFactory.getInstance().pawn(src, color).withMoved();
		PieceMovable board = Board.init(repository, pawn);

		Location srcLocation = Location.from(src);
		// when
		List<Location> actual = board.findPossiblePaths(srcLocation);
		// then
		List<Location> expected = Arrays.stream(expectedPositions)
			.map(Location::from)
			.toList();
		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@DisplayName("보드판 위에 적 기물이 있는 상태에서 폰의 이동 가능 경로를 계산한다")
	@Test
	void givenEnemy_whenFindPossiblePaths_thenReturnOfPossibleLocations() {
		// given
		Piece whitePawn = PieceFactory.getInstance().pawn("a2", Color.WHITE);
		Piece darkPawn = PieceFactory.getInstance().pawn("b3", Color.DARK);
		PieceMovable board = Board.init(repository, whitePawn, darkPawn);

		Location srcLocation = Location.from("a2");
		// when
		List<Location> actual = board.findPossiblePaths(srcLocation);
		// then
		List<Location> expected = Arrays.stream(new String[] {"a3", "a4", "b3"})
			.map(Location::from)
			.toList();
		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@DisplayName("보드판 위에 폰이 주어졌을때 특정한 위치로 이동한다")
	@ParameterizedTest
	@MethodSource(value = "validPawnMoveLocations")
	void givenPawn_whenMovePiece_thenReturnMovedPiece(Piece piece, String src, String dst, Piece expected) {
		// given
		PieceMovable board = Board.init(repository, piece);

		Location srcLocation = Location.from(src);
		Location dstLocation = Location.from(dst);
		// when
		Piece actual = board.movePiece(srcLocation, dstLocation).orElseThrow();
		// then
		assertThat(actual).isEqualTo(expected);
		assertThat(repository.contains(actual)).isTrue();
	}
}
