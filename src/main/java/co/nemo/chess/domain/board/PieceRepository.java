package co.nemo.chess.domain.board;

import java.util.ArrayList;
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
	 */
	public void poll(Location location) {
		Piece findPiece = find(location);
		if (findPiece instanceof AbstractChessPiece piece) {
			pieces.remove(piece);
		}
	}

	/**
	 * 매개변수로 전달한 Piece 객체를 제거한다.
	 * 만약 매개변수로 전달한 Piece가 구현체 타입이 맞지 않으면 그대로 반환한다
	 * @param delPiece 삭제할 기물
	 */
	public void poll(Piece delPiece) {
		if (delPiece instanceof AbstractChessPiece piece) {
			pieces.remove(piece);
		}
	}

	public boolean contains(Piece piece) {
		return pieces.contains(piece);
	}

	public int size() {
		return pieces.size();
	}
}
