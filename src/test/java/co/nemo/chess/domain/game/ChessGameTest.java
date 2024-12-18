package co.nemo.chess.domain.game;

import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

	@DisplayName("백 플레이어는 백룩과 백킹을 이용하여 캐슬링한다")
	@Test
	void castling() {
		// given
		String input = Stream.of(
				"move g2 g3",
				"move g7 g6",
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
}
