package co.nemo.chess.domain.board;

import java.util.List;
import java.util.Optional;

import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;

public class Board implements PieceMovable {
	private final PieceRepository repository;

	private Board(PieceRepository repository) {
		this.repository = repository;
	}

	public static Board empty() {
		return new Board(PieceRepository.empty());
	}

	public static Board init(Piece... pieces) {
		return new Board(PieceRepository.init(pieces));
	}

	@Override
	public Optional<Piece> movePiece(Location src, Location dst) {
		Piece movePiece;
		try {
			movePiece = findPiece(src).move(dst, this);
		} catch (IllegalArgumentException e) {
			return Optional.empty();
		}
		addPiece(movePiece);
		return Optional.of(movePiece);
	}

	@Override
	public List<Location> findPossiblePaths(Location src) {
		Piece piece = findPiece(src);
		// 기물의 이동 가능한 전체 경로 계산
		List<Location> result = piece.findPossiblePaths();
		// 전체 경로 중에서 조건을 만족하는 이동 가능한 경로 계산
		// 조건1: 위치에 다른 기물이 없어야 함
		result = result.stream()
			.filter(location -> !repository.contains(location))
			.toList();
		// 조건2: 위치에 다른 기물이 있는 경우 piece가 다른 기물을 잡을 수 있어야 함
		// 조건3: 위치에 다른 기물이 없지만 특수한 조건상 이동이 가능해야함 (ex: 폰 대각선 이동)
		return result;
	}

	public void addPiece(Piece piece) {
		repository.add(piece);
	}

	public Piece findPiece(Location location) {
		return repository.find(location)
			.orElseThrow(() -> new IllegalArgumentException("not found piece, location=" + location));
	}

	public Optional<Piece> pollPiece(Location location) throws IllegalArgumentException {
		return Optional.ofNullable(repository.poll(location));

	}

	public int size() {
		return repository.size();
	}
}
