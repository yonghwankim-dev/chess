package co.nemo.chess.domain.board;

import co.nemo.chess.domain.piece.Piece;

public class BoardLocation {
	private final Piece piece;

	public BoardLocation(Piece piece) {
		this.piece = piece;
	}

	public static BoardLocation empty() {
		return new BoardLocation(null);
	}

	public static BoardLocation from(Piece piece) {
		return new BoardLocation(piece);
	}
}
