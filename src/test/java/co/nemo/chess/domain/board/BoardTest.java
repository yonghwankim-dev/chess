package co.nemo.chess.domain.board;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
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
		return Stream.of(
			Arguments.of(new AbstractChessPiece[] {a2WhitePawn}, a2Location, a3Location, a3WhitePawn),
			Arguments.of(new AbstractChessPiece[] {a2WhitePawn}, a2Location, a4Location, a4WhitePawn),
			Arguments.of(new AbstractChessPiece[] {a7DarkPawn}, a7Location, a5Location, a5DarkPawn),
			Arguments.of(new AbstractChessPiece[] {a7DarkPawn}, a7Location, a6Location, a6DarkPawn)
		);
	}

	public static Stream<Arguments> validPawnEnPassantSource() {
		AbstractChessPiece f5WhitePawn = PieceFactory.getInstance().pawn("f5", Color.WHITE);
		AbstractChessPiece g6WhitePawn = PieceFactory.getInstance().pawn("g6", Color.WHITE);
		AbstractChessPiece g5DarkPawn = PieceFactory.getInstance().pawn("g5", Color.DARK)
			.withLocationHistory(new ArrayDeque<>(List.of(Location.from("g7"))));

		Location f5Location = Location.from("f5");
		Location g6Location = Location.from("g6");

		return Stream.of(
			Arguments.of(new AbstractChessPiece[] {f5WhitePawn.withMoved(), g5DarkPawn.withMoved()},
				f5Location,
				g6Location,
				g6WhitePawn.withMoved()
			)
		);
	}

	public static Stream<Arguments> invalidPawnEnPassantSource() {
		AbstractChessPiece a4WhitePawn = PieceFactory.getInstance().pawn("a4", Color.WHITE);
		AbstractChessPiece a5WhitePawn = PieceFactory.getInstance().pawn("a5", Color.WHITE);
		AbstractChessPiece b4DarkPawn = PieceFactory.getInstance().pawn("b4", Color.DARK);
		AbstractChessPiece b5DarkPawn = PieceFactory.getInstance().pawn("b5", Color.DARK);
		AbstractChessPiece b4DarkRook = PieceFactory.getInstance().darkRook("b4");

		Location a4Location = Location.from("a4");
		Location a5Location = Location.from("a5");
		Location b5Location = Location.from("b5");
		Location b6Location = Location.from("b6");
		return Stream.of(
			// A4 백폰이 B4 흑폰에 대하여 상우 대각선 이동해서 앙파상 시도
			Arguments.of(new AbstractChessPiece[] {a4WhitePawn.withMoved(), b4DarkPawn.withMoved()},
				a4Location,
				b5Location
			),
			// A4 백폰이 B4 흑룩에 대하여 상우 대각선 이동해서 앙파상 시도
			Arguments.of(new AbstractChessPiece[] {a4WhitePawn.withMoved(), b4DarkRook.withMoved()},
				a4Location,
				b5Location
			),
			// B5 흑폰이 총 2번 1칸씩 이동한 상태에서 A5 백폰이 B5 흑폰에 대하여 상우 대각선 이동해서 앙파상 시도
			Arguments.of(new AbstractChessPiece[] {a5WhitePawn.withMoved(), b5DarkPawn.withMoved()},
				a5Location,
				b6Location)
		);
	}

	public static Stream<Arguments> invalidPawnMoveLocations() {
		AbstractChessPiece a2WhitePawn = PieceFactory.getInstance().pawn("a2", Color.WHITE);
		AbstractChessPiece b2WhitePawn = PieceFactory.getInstance().pawn("b2", Color.WHITE);
		AbstractChessPiece a3DarkPawn = PieceFactory.getInstance().pawn("a3", Color.DARK).withMoved();
		Location a1Location = Location.from("a1");
		Location a2Location = Location.from("a2");
		Location a4Location = Location.from("a4");
		Location a3Location = Location.from("a3");
		Location b2Location = Location.from("b2");
		Location b3Location = Location.from("b3");
		AbstractChessPiece[] pieces = new AbstractChessPiece[] {a2WhitePawn, b2WhitePawn, a3DarkPawn};
		return Stream.of(
			Arguments.of(pieces, a2Location, a3Location),
			Arguments.of(pieces, a2Location, b3Location),
			Arguments.of(pieces, a2Location, b2Location),
			Arguments.of(pieces, a2Location, a1Location),
			Arguments.of(pieces, a2Location, a2Location),
			Arguments.of(pieces, a2Location, a4Location)
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
		List<Location> actual = board.findPossibleLocations(srcLocation);
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
		List<Location> actual = board.findPossibleLocations(srcLocation);
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
		List<Location> actual = board.findPossibleLocations(srcLocation);
		// then
		List<Location> expected = Arrays.stream(new String[] {"a3", "a4", "b3"})
			.map(Location::from)
			.toList();
		assertThat(actual).containsExactlyElementsOf(expected);
	}

	@DisplayName("보드판 위에 폰이 주어졌을때 특정한 위치로 이동한다")
	@ParameterizedTest
	@MethodSource(value = {"validPawnMoveLocations", "validPawnEnPassantSource"})
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

	@DisplayName("보드판 위에 폰이 주어졌을때 특정한 위치로 이동하지 못한다")
	@ParameterizedTest
	@MethodSource(value = {"invalidPawnMoveLocations", "invalidPawnEnPassantSource"})
	void givenPawn_whenInvalidMovePiece_thenReturnEmptyOptional(Piece[] initPieces, Location src, Location dst) {
		// given
		PieceMovable board = Board.init(repository, initPieces);
		// when
		Optional<Piece> actual = board.movePiece(src, dst);
		// then
		assertThat(actual).isEmpty();
	}

	@DisplayName("보드판의 기물들을 셋업한다")
	@Test
	void setupPieces() {
		// given
		Board board = Board.init(repository);
		// when
		List<Piece> actual = board.setupPieces();
		// then
		boolean allMatch = actual.stream()
			.allMatch(piece -> repository.contains(piece));
		Assertions.assertThat(allMatch).isTrue();
	}

	@DisplayName("백룩의 이동 가능한 경로를 탐색한다")
	@Test
	void givenWhiteRook_whenFindPossiblePaths_thenReturnLocations() {
		// given
		Board board = Board.init(repository);
		repository.add(PieceFactory.getInstance().whiteRook("a1"));
		Location src = Location.from("a1");
		// when
		List<Location> locations = board.findPossibleLocations(src);
		// then
		List<Location> expected = Stream.of("a2", "a3", "a4", "a5", "a6", "a7", "a8", "b1", "c1", "d1", "e1", "f1",
				"g1", "h1")
			.map(Location::from)
			.toList();
		assertThat(locations).containsExactlyElementsOf(expected);
	}

	@DisplayName("백킹과 백룩 사이에 다른 기물이 없다")
	@Test
	void givenWhiteKingAndRook_whenExistPieceBetween_thenReturnTrue() {
		// given
		Board board = Board.init(repository);
		Piece king = PieceFactory.getInstance().whiteKing("e1");
		Piece rook = PieceFactory.getInstance().whiteRook("h1");
		repository.add(king);
		repository.add(rook);

		Location e1 = Location.from("e1");
		Location h1 = Location.from("h1");
		// when
		boolean actual = board.existPieceBetween(e1, h1);
		// then
		Assertions.assertThat(actual).isFalse();
	}
}
