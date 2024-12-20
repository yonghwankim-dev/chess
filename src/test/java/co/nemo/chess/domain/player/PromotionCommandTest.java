package co.nemo.chess.domain.player;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.command.AbstractCommand;
import co.nemo.chess.domain.game.ChessGameReader;
import co.nemo.chess.domain.game.ChessGameWriter;
import co.nemo.chess.domain.game.ConsoleOutputStrategy;
import co.nemo.chess.domain.game.InputStrategy;
import co.nemo.chess.domain.game.OutputStrategy;
import co.nemo.chess.domain.game.StringInputStrategy;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.piece.PieceFactory;
import co.nemo.chess.domain.piece.PieceType;

class PromotionCommandTest {

	@DisplayName("폰을 퀸으로 승급시킨다")
	@Test
	void promo() {
		// given
		Board board = Board.empty();
		Piece pawn = PieceFactory.getInstance().whitePawn("a8");
		board.addPiece(pawn);
		InputStrategy inputStrategy = new StringInputStrategy("promotion queen");
		OutputStrategy outputStrategy = ConsoleOutputStrategy.getInstance();
		ChessGameWriter gameWriter = new ChessGameWriter(outputStrategy);
		ChessGameReader gameReader = new ChessGameReader(inputStrategy, gameWriter);
		Player player = Player.white();

		AbstractCommand command = AbstractCommand.promotionCommand(pawn, PieceType.QUEEN);
		// when
		boolean result = command.process(board, gameReader, gameWriter, player);
		Optional<Piece> actual = board.findPiece(Location.from("a8"));
		// then
		Assertions.assertThat(result).isTrue();
		Piece expected = PieceFactory.getInstance().whiteQueen("a8");
		Assertions.assertThat(actual).contains(expected);
	}

}
