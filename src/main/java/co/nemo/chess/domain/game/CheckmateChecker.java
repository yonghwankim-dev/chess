package co.nemo.chess.domain.game;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.piece.Color;
import co.nemo.chess.domain.piece.NullPiece;
import co.nemo.chess.domain.piece.Piece;

public class CheckmateChecker {
	private final ChessGameWriter printer;

	public CheckmateChecker(ChessGameWriter printer) {
		this.printer = printer;
	}

	public boolean isCheckmate(Board board) {
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
