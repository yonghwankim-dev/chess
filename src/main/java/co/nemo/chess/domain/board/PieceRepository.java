package co.nemo.chess.domain.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
		if (pieces.contains(piece)) {
			return false;
		}
		return pieces.add(piece);
	}

	public Optional<Piece> find(Location location) {
		return pieces.stream()
			.filter(p -> p.match(location))
			.findAny();
	}

	/**
	 * Poll piece.
	 * location에 따른 체스 기물이 없으면 null을 반환한다
	 * @param location the location
	 * @return the piece
	 */
	public Piece poll(Location location) {
		Optional<Piece> optionalPiece = find(location);
		optionalPiece.ifPresent(pieces::remove);
		return optionalPiece.orElse(null);
	}

	public boolean contains(Location location) {
		return pieces.stream()
			.anyMatch(piece -> piece.match(location));
	}

	public int size() {
		return pieces.size();
	}
}
