package co.nemo.chess.domain.board;

import java.util.ArrayList;
import java.util.List;

import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;

public class PieceRepository {
	private final List<Piece> pieces;

	private PieceRepository(List<Piece> pieces) {
		this.pieces = pieces;
	}

	public static PieceRepository empty() {
		return new PieceRepository(new ArrayList<>());
	}

	public boolean add(Piece piece) {
		if (pieces.contains(piece)) {
			return false;
		}
		return pieces.add(piece);
	}

	public Piece find(Location location) {
		return pieces.stream()
			.filter(p -> p.match(location))
			.findAny()
			.orElse(null);
	}

	public Piece poll(Location location) {
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
