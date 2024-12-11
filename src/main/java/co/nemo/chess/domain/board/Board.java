package co.nemo.chess.domain.board;

import java.util.ArrayList;
import java.util.List;

import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;

public class Board {
	private final List<Piece> pieces;

	private Board(List<Piece> pieces) {
		this.pieces = pieces;
	}

	public static Board empty() {
		return new Board(new ArrayList<>());
	}

	public void addPiece(Piece piece) {
		if (!pieces.contains(piece)) {
			pieces.add(piece);
		}
	}

	public boolean movePiece(Location src, Location dst) {
		Piece piece = popPiece(src);
		addPiece(piece.move(dst));
		return true;
	}

	public Piece finePiece(Location location) {
		return pieces.stream()
			.filter(p -> p.match(location))
			.findAny()
			.orElse(null);
	}

	public Piece popPiece(Location location) {
		Piece delPiece = pieces.stream()
			.filter(p -> p.match(location))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("not found piece, location=" + location));
		pieces.remove(delPiece);
		return delPiece;
	}

	public int size() {
		return pieces.size();
	}
}
