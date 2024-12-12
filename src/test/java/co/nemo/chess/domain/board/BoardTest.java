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

import co.nemo.chess.domain.piece.AbstractChessPiece;
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
		AbstractChessPiece a2WhitePawn = PieceFactory.getInstance().pawn("a2", Color.WHITE);
		AbstractChessPiece b2WhitePawn = PieceFactory.getInstance().pawn("b2", Color.WHITE);
		AbstractChessPiece a2DarkPawn = PieceFactory.getInstance().pawn("a2", Color.DARK);
		AbstractChessPiece a7DarkPawn = PieceFactory.getInstance().pawn("a7", Color.DARK);

		AbstractChessPiece a3WhitePawn = PieceFactory.getInstance().pawn("a3", Color.WHITE).withMoved();
		AbstractChessPiece a4WhitePawn = PieceFactory.getInstance().pawn("a4", Color.WHITE).withMoved();
		AbstractChessPiece a5DarkPawn = PieceFactory.getInstance().pawn("a5", Color.DARK).withMoved();
		AbstractChessPiece a6DarkPawn = PieceFactory.getInstance().pawn("a6", Color.DARK).withMoved();
		Location a2Location = Location.from("a2");
		Location a3Location = Location.from("a3");
		Location a4Location = Location.from("a4");
		Location a5Location = Location.from("a5");
		Location a6Location = Location.from("a6");
		Location a7Location = Location.from("a7");
		Location b2Location = Location.from("b2");
		return Stream.of(
			Arguments.of(new AbstractChessPiece[] {a2WhitePawn}, a2Location, a3Location, a3WhitePawn),
			Arguments.of(new AbstractChessPiece[] {a2WhitePawn}, a2Location, a4Location, a4WhitePawn),
			Arguments.of(new AbstractChessPiece[] {a7DarkPawn}, a7Location, a5Location, a5DarkPawn),
			Arguments.of(new AbstractChessPiece[] {a7DarkPawn}, a7Location, a6Location, a6DarkPawn),
			Arguments.of(new AbstractChessPiece[] {b2WhitePawn, a2DarkPawn}, b2Location, a3Location, a3WhitePawn)
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
	void givenPawn_whenMovePiece_thenReturnMovedPiece(Piece[] initPieces, Location src, Location dst, Piece expected) {
		// given
		PieceMovable board = Board.init(repository, initPieces);
		// when
		Piece actual = board.movePiece(src, dst).orElseThrow();
		// then
		assertThat(actual).isEqualTo(expected);
		assertThat(repository.contains(actual)).isTrue();
		assertThat(repository.size()).isEqualTo(1);
	}
}
