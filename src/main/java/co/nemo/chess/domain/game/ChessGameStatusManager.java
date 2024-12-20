package co.nemo.chess.domain.game;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.piece.Color;
import co.nemo.chess.domain.piece.NullPiece;
import co.nemo.chess.domain.piece.Piece;

public class ChessGameStatusManager {
	private final Board board;
	private final ChessGameStatusPrinter printer;

	public ChessGameStatusManager(Board board, ChessGameStatusPrinter printer) {
		this.board = board;
		this.printer = printer;
	}

	public boolean isCheckmate() {
		Piece checkmatedPiece = board.getCheckmatePiece().orElseGet(NullPiece::empty);
		if (checkmatedPiece.isColorOf(Color.WHITE)) {
			printer.printDarkPlayerWin();
			printer.printBoard(board);
			return true;
		} else if (checkmatedPiece.isColorOf(Color.DARK)) {
			printer.printWhitePlayerWin();
			printer.printBoard(board);
			return true;
		} else {
			return false;
		}
	}
}
