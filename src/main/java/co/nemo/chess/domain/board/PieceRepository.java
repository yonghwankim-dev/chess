package co.nemo.chess.domain.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	public static PieceRepository init(List<Piece> pieces) {
		return new PieceRepository(new ArrayList<>(pieces));
	}

	/**
	 * Add boolean.
	 * 이미 같은 기물이 존재하는 경우 추가하지 않는다
	 * @param piece the piece
	 * @return the boolean
	 */
	public void add(Piece piece) {
		if (pieces.contains(piece) || piece instanceof NullPiece) {
			return;
		}
		pieces.add(piece);
	}

	public Optional<Piece> find(Location location) {
		return pieces.stream()
			.filter(p -> p.match(location))
			.findAny();
	}

	/**
	 * Poll piece.
	 * location에 따른 체스 기물이 없으면 NullPiece 객체를 반환
	 * @param location the location
	 */
	public void poll(Location location) {
		find(location).ifPresent(pieces::remove);
	}

	/**
	 * 매개변수로 전달한 Piece 객체를 제거한다.
	 * 만약 매개변수로 전달한 Piece가 구현체 타입이 맞지 않으면 그대로 반환한다
	 * @param delPiece 삭제할 기물
	 */
	public void poll(Piece delPiece) {
		pieces.remove(delPiece);
	}

	public boolean contains(Piece piece) {
		return pieces.contains(piece);
	}

	public boolean contains(Location location) {
		return pieces.stream().anyMatch(piece -> piece.match(location));
	}

	public int size() {
		return pieces.size();
	}

	public void clear() {
		pieces.clear();
	}

	public List<Piece> findAll() {
		return new ArrayList<>(pieces);
	}
}
