package co.nemo.chess.domain.board;

import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;

public class Board {
	private final PieceRepository repository;

	private Board(PieceRepository repository) {
		this.repository = repository;
	}

	public static Board empty() {
		return new Board(PieceRepository.empty());
	}

	public Piece movePiece(Location src, Location dst) {
		Piece movePiece = pollPiece(src).move(dst);
		addPiece(movePiece);
		return movePiece;
	}

	public void addPiece(Piece piece) {
		repository.add(piece);
	}

	public Piece finePiece(Location location) {
		return repository.find(location);
	}

	public Piece pollPiece(Location location) {
		return repository.poll(location);
	}

	public int size() {
		return repository.size();
	}
}
