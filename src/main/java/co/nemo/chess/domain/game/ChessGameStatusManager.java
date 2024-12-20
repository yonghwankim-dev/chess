package co.nemo.chess.domain.game;

import java.util.Optional;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.piece.Color;
import co.nemo.chess.domain.piece.Piece;

public class ChessGameStatusManager {
	private final Board board;
	private final OutputStrategy outputStrategy;

	public ChessGameStatusManager(Board board, OutputStrategy outputStrategy) {
		this.board = board;
		this.outputStrategy = outputStrategy;
	}

	public boolean isCheckmate() {
		Optional<Piece> piece = board.getCheckmatePiece();
		if (piece.isEmpty()) {
			return false;
		}

		Piece checkmatedPiece = piece.orElseThrow();
		if (checkmatedPiece.isColorOf(Color.WHITE)) {
			outputStrategy.println("흑 플레이어 승리!");
			outputStrategy.printBoard(board);
			return true;
		} else if (checkmatedPiece.isColorOf(Color.DARK)) {
			outputStrategy.println("백 플레이어 승리!");
			outputStrategy.printBoard(board);
			return true;
		}
		return false;
	}
}
