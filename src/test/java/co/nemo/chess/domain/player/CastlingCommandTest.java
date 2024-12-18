package co.nemo.chess.domain.player;

import static org.assertj.core.api.Assertions.*;

import org.apache.logging.log4j.util.Strings;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.ConsoleOutputStrategy;
import co.nemo.chess.domain.game.InputStrategy;
import co.nemo.chess.domain.game.OutputStrategy;
import co.nemo.chess.domain.game.StringInputStrategy;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.piece.PieceFactory;

class CastlingCommandTest {

	@DisplayName("백 플레이어가 백킹과 백룩을 가지고 킹 사이드 캐슬링한다")
	@Test
	void givenWhitePlayer_whenCastling_thenMovedKingAndRook() {
		// given
		Location kingSrc = Location.from("e1");
		Location rookSrc = Location.from("h1");
		AbstractCommand command = AbstractCommand.castlingCommand(kingSrc, rookSrc);
		Board board = Board.empty();
		Piece e1WhiteKing = PieceFactory.getInstance().whiteKing("e1");
		Piece h1WhiteRook = PieceFactory.getInstance().whiteRook("h1");
		board.addPiece(e1WhiteKing);
		board.addPiece(h1WhiteRook);

		InputStrategy inputStrategy = new StringInputStrategy(Strings.EMPTY);
		OutputStrategy outputStrategy = ConsoleOutputStrategy.getInstance();
		Player whitePlayer = Player.white();
		// when
		command.process(board, inputStrategy, outputStrategy, whitePlayer);
		// then
		Piece expectedWhiteKing = PieceFactory.getInstance().whiteKing("g1").withMoved();
		assertThat(board.findPiece(Location.from("g1"))).contains(expectedWhiteKing);

		Piece expectedWhiteRook = PieceFactory.getInstance().whiteRook("f1").withMoved();
		assertThat(board.findPiece(Location.from("f1"))).contains(expectedWhiteRook);
	}

	@DisplayName("백 플레이어가 백룩과 백킹을 가지고 퀸 사이드 캐슬링한다")
	@Test
	void givenWhiteKingAndRook_whenQueenSideCastling_thenMovedKingAndRook() {
		// given
		Location kingSrc = Location.from("e1");
		Location rookSrc = Location.from("a1");
		AbstractCommand command = AbstractCommand.castlingCommand(kingSrc, rookSrc);
		Board board = Board.empty();
		Piece e1WhiteKing = PieceFactory.getInstance().whiteKing("e1");
		Piece h1WhiteRook = PieceFactory.getInstance().whiteRook("a1");
		board.addPiece(e1WhiteKing);
		board.addPiece(h1WhiteRook);

		InputStrategy inputStrategy = new StringInputStrategy(Strings.EMPTY);
		OutputStrategy outputStrategy = ConsoleOutputStrategy.getInstance();
		Player whitePlayer = Player.white();
		// when
		command.process(board, inputStrategy, outputStrategy, whitePlayer);
		// then
		Piece expectedWhiteKing = PieceFactory.getInstance().whiteKing("c1").withMoved();
		assertThat(board.findPiece(Location.from("c1"))).contains(expectedWhiteKing);

		Piece expectedWhiteRook = PieceFactory.getInstance().whiteRook("d1").withMoved();
		assertThat(board.findPiece(Location.from("d1"))).contains(expectedWhiteRook);
	}

	@DisplayName("흑 플레이어가 흑킹과 흑룩을 가지고 킹 사이드 캐슬링한다")
	@Test
	void givenBlackKingAndRook_whenKingSideCastling_thenMovedKingAndRook() {
		// given
		Location kingSrc = Location.from("e8");
		Location rookSrc = Location.from("h8");
		AbstractCommand command = AbstractCommand.castlingCommand(kingSrc, rookSrc);
		Board board = Board.empty();
		Piece e8DarkKing = PieceFactory.getInstance().darkKing("e8");
		Piece h8DarkRook = PieceFactory.getInstance().darkRook("h8");
		board.addPiece(e8DarkKing);
		board.addPiece(h8DarkRook);

		InputStrategy inputStrategy = new StringInputStrategy(Strings.EMPTY);
		OutputStrategy outputStrategy = ConsoleOutputStrategy.getInstance();
		Player darkPlayer = Player.dark();
		// when
		command.process(board, inputStrategy, outputStrategy, darkPlayer);
		// then
		Piece expectedDarkKing = PieceFactory.getInstance().darkKing("g8").withMoved();
		assertThat(board.findPiece(Location.from("g8"))).contains(expectedDarkKing);

		Piece expectedDarkRook = PieceFactory.getInstance().darkRook("f8").withMoved();
		assertThat(board.findPiece(Location.from("f8"))).contains(expectedDarkRook);
	}

	@DisplayName("흑 플레이어가 흑킹과 흑룩을 가지고 퀸 사이드 캐슬링한다")
	@Test
	void givenBlackKingAndRook_whenQueenSideCastling_thenMovedKingAndRook() {
		// given
		Location kingSrc = Location.from("e8");
		Location rookSrc = Location.from("a8");
		AbstractCommand command = AbstractCommand.castlingCommand(kingSrc, rookSrc);
		Board board = Board.empty();
		Piece e8DarkKing = PieceFactory.getInstance().darkKing("e8");
		Piece a8DarkRook = PieceFactory.getInstance().darkRook("a8");
		board.addPiece(e8DarkKing);
		board.addPiece(a8DarkRook);

		InputStrategy inputStrategy = new StringInputStrategy(Strings.EMPTY);
		OutputStrategy outputStrategy = ConsoleOutputStrategy.getInstance();
		Player darkPlayer = Player.dark();
		// when
		command.process(board, inputStrategy, outputStrategy, darkPlayer);
		// then
		Piece expectedDarkKing = PieceFactory.getInstance().darkKing("c8").withMoved();
		assertThat(board.findPiece(Location.from("c8"))).contains(expectedDarkKing);

		Piece expectedDarkRook = PieceFactory.getInstance().darkRook("d8").withMoved();
		assertThat(board.findPiece(Location.from("d8"))).contains(expectedDarkRook);
	}

	@DisplayName("백색 플레이어가 흑킹과 흑룩을 가지고 킹 사이드 캐슬링 할수 없다")
	@Test
	void givenDarkKingAndRook_whenWhitePlayerDoKingSideCastling_thenNotMovedKingAndRook() {
		// given
		Location kingSrc = Location.from("e8");
		Location rookSrc = Location.from("h8");
		AbstractCommand command = AbstractCommand.castlingCommand(kingSrc, rookSrc);
		Board board = Board.empty();
		Piece e8DarkKing = PieceFactory.getInstance().darkKing("e8");
		Piece h8DarkRook = PieceFactory.getInstance().darkRook("h8");
		board.addPiece(e8DarkKing);
		board.addPiece(h8DarkRook);

		InputStrategy inputStrategy = new StringInputStrategy(Strings.EMPTY);
		OutputStrategy outputStrategy = ConsoleOutputStrategy.getInstance();
		Player whitePlayer = Player.white();
		// when
		Throwable throwable = catchThrowable(() -> command.process(board, inputStrategy, outputStrategy, whitePlayer));
		// then
		Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}
}
