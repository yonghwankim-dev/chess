package co.nemo.chess.domain.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.nemo.chess.domain.piece.AbstractChessPiece;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.NullPiece;
import co.nemo.chess.domain.piece.Piece;

public class PieceRepository {
	private final List<Piece> pieces;

	private PieceRepository(List<Piece> pieces) {
		this.pieces = pieces;
	}

	public static PieceRepository empty() {
		return new PieceRepository(new ArrayList<>());
	}

	public static PieceRepository init(Piece... pieces) {
		return new PieceRepository(new ArrayList<>(Arrays.asList(pieces)));
	}

	/**
	 * Add boolean.
	 * 이미 같은 기물이 존재하는 경우 추가하지 않는다
	 * @param piece the piece
	 * @return the boolean
	 */
	public boolean add(Piece piece) {
		if (pieces.contains(piece) || piece instanceof NullPiece) {
			return false;
		}
		return pieces.add(piece);
	}

	public Piece find(Location location) {
		return pieces.stream()
			.filter(p -> p.match(location))
			.findAny()
			.orElseGet(() -> NullPiece.from(location));
	}

	/**
	 * Poll piece.
	 * location에 따른 체스 기물이 없으면 NullPiece 객체를 반환
	 * @param location the location
	 * @return the piece
	 */
	public Piece poll(Location location) {
		Piece piece = find(location);
		if (piece instanceof AbstractChessPiece abstractChessPiece) {
			pieces.remove(abstractChessPiece);
			return abstractChessPiece;
		}
		return NullPiece.from(location);
	}

	public boolean contains(Location location) {
		return pieces.stream()
			.anyMatch(piece -> piece.match(location));
	}

	public boolean contains(Piece piece) {
		return pieces.contains(piece);
	}

	public int size() {
		return pieces.size();
	}
}
