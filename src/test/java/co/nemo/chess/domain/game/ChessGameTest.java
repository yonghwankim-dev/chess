package co.nemo.chess.domain.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.piece.AbstractChessPiece;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.PieceFactory;

class ChessGameTest {

	@DisplayName("백색 플레이어가 흑색 기물을 움직일 수 없다")
	@Test
	void givenWhitePlayer_whenAttemptingToMoveBlackPiece_thenMoveIsInvalid() {
		// given
		String input = "move a7 a6";
		InputStrategy inputStrategy = new StringInputStrategy(input);
		Board board = Board.empty();
		ChessGame game = new ChessGame(board, inputStrategy, ConsoleOutputStrategy.getInstance());
		// when
		game.startGame();
		// then
		AbstractChessPiece expected = PieceFactory.getInstance().darkPawn("a7");
		Assertions.assertThat(board.getAllPieces())
			.contains(expected);
	}

	@DisplayName("백 플레이어가 폰을 룩으로 승급시킨다")
	@Test
	void givenWhitePlayer_whenPawnMovedEndOfRank_thenPawnPromoRook() {
		// given
		String input = Stream.of(
				"move a2 a4",
				"move b7 b5",
				"move a4 b5",
				"move a7 a6",
				"move b5 b6",
				"move a6 a5",
				"move b6 b7",
				"move a5 a4",
				"move b7 b8",
				"R", // a typing error
				"Rook"
			)
			.collect(Collectors.joining(System.lineSeparator()));
		InputStrategy inputStrategy = new StringInputStrategy(input);
		Board board = Board.empty();
		ChessGame game = new ChessGame(board, inputStrategy, ConsoleOutputStrategy.getInstance());
		// when
		game.startGame();
		// then
		AbstractChessPiece expected = PieceFactory.getInstance().whiteRook("b8");
		Assertions.assertThat(board.getAllPieces()).contains(expected);

		List<Location> moveHistory = Stream.of("b7", "b6", "b5", "a4", "a2")
			.map(Location::from)
			.toList();
		AbstractChessPiece expectedB8WhitePawn = PieceFactory.getInstance().whitePawn("b8").withMoved()
			.withLocationHistory(new ArrayDeque<>(moveHistory));
		Assertions.assertThat(board.getAllPieces()).doesNotContain(expectedB8WhitePawn);
	}

	@DisplayName("백 플레이어는 백룩과 백킹을 이용하여 킹사이드 캐슬링한다")
	@Test
	void givenWhiteKingAndRook_whenCastling_thenMovedKingAndRook() {
		// given
		String input = Stream.of(
				"move g2 g3",
				"move g7 g6",
				"move f1 g2",
				"move g6 g5",
				"castling e1 h1"
			)
			.collect(Collectors.joining(System.lineSeparator()));
		InputStrategy inputStrategy = new StringInputStrategy(input);
		Board board = Board.empty();
		ChessGame game = new ChessGame(board, inputStrategy, ConsoleOutputStrategy.getInstance());
		// when
		game.startGame();
		// then
		AbstractChessPiece expectedWhiteRook = PieceFactory.getInstance().whiteRook("f1").withMoved();
		AbstractChessPiece expectedWhiteKing = PieceFactory.getInstance().whiteKing("g1").withMoved();
		Assertions.assertThat(board.getAllPieces()).contains(expectedWhiteRook, expectedWhiteKing);
	}

	@DisplayName("백 플레이어는 백룩과 백킹을 이용하여 퀸사이드 캐슬링한다")
	@Test
	void givenWhiteKingAndRook_whenQueenSideCastling_thenMovedKingAndRook() {
		// given
		String input = Stream.of(
				"move g2 g3",
				"move g7 g6",
				"move b2 b3",
				"move g6 g5",
				"move c1 b2",
				"move g5 g4",
				"move c2 c3",
				"move c7 c6",
				"move d1 c2",
				"move c6 c5",
				"castling e1 a1"
			)
			.collect(Collectors.joining(System.lineSeparator()));
		InputStrategy inputStrategy = new StringInputStrategy(input);
		Board board = Board.empty();
		ChessGame game = new ChessGame(board, inputStrategy, ConsoleOutputStrategy.getInstance());
		// when
		game.startGame();
		// then
		AbstractChessPiece expectedWhiteRook = PieceFactory.getInstance().whiteRook("d1").withMoved();
		AbstractChessPiece expectedWhiteKing = PieceFactory.getInstance().whiteKing("c1").withMoved();
		Assertions.assertThat(board.getAllPieces()).contains(expectedWhiteRook, expectedWhiteKing);
	}

	@DisplayName("흑 플레이어는 흑킹과 흑룩을 이용하여 킹 사이드 캐슬링한다")
	@Test
	void givenBlackKingAndRook_whenKingSideCastling_thenMovedKingAndRook() {
		// given
		String input = Stream.of(
				"move g2 g3", // white
				"move g7 g6", // dark
				"move g3 g4", // white
				"move f8 g7", // dark
				"move g4 g5", // white
				"castling e8 h8"
			)
			.collect(Collectors.joining(System.lineSeparator()));
		InputStrategy inputStrategy = new StringInputStrategy(input);
		Board board = Board.empty();
		ChessGame game = new ChessGame(board, inputStrategy, ConsoleOutputStrategy.getInstance());
		// when
		game.startGame();
		// then
		AbstractChessPiece expectedDarkRook = PieceFactory.getInstance().darkRook("f8").withMoved();
		AbstractChessPiece expectedDarkKing = PieceFactory.getInstance().darkKing("g8").withMoved();
		Assertions.assertThat(board.getAllPieces()).contains(expectedDarkRook, expectedDarkKing);
	}

	@DisplayName("흑 플레이어는 흑킹과 흑룩을 이용하여 퀸 사이드 캐슬링한다")
	@Test
	void givenBlackKingAndRook_whenQueenSideCastling_thenMovedKingAndRook() {
		// given
		String input = Stream.of(
				"move g2 g3",
				"move b7 b6",
				"move g3 g4",
				"move c8 b7",
				"move g4 g5",
				"move c7 c6",
				"move g5 g6",
				"move d8 c7",
				"move c2 c3",
				"castling e8 a8"
			)
			.collect(Collectors.joining(System.lineSeparator()));
		InputStrategy inputStrategy = new StringInputStrategy(input);
		Board board = Board.empty();
		ChessGame game = new ChessGame(board, inputStrategy, ConsoleOutputStrategy.getInstance());
		// when
		game.startGame();
		// then
		AbstractChessPiece expectedDarkRook = PieceFactory.getInstance().darkRook("d8").withMoved();
		AbstractChessPiece expectedDarkKing = PieceFactory.getInstance().darkKing("c8").withMoved();
		Assertions.assertThat(board.getAllPieces()).contains(expectedDarkRook, expectedDarkKing);
	}

	@DisplayName("파일을 읽은 다음에 시나리오를 실행시킨다")
	@Test
	void senario1() throws IOException {
		// given
		InputStream inputStream = new ClassPathResource("./test/senario1.txt").getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String input = reader.lines()
			.collect(Collectors.joining(System.lineSeparator()));
		InputStrategy inputStrategy = new StringInputStrategy(input);
		Board board = Board.empty();
		ChessGame game = new ChessGame(board, inputStrategy, ConsoleOutputStrategy.getInstance());
		// when
		game.startGame();
		// then
	}
}
