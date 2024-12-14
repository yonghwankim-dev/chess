package co.nemo.chess.domain.game;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.piece.AbstractChessPiece;
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
}
