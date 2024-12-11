package co.nemo.chess.domain.board;

import java.util.ArrayList;
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
		return Optional.empty();
	}

	// 기물의 이동 가능한 전체 경로 계산
	@Override
	public List<Location> findPossiblePaths(Location src) {
		// 전체 경로 중에서 조건을 만족하는 이동 가능한 경로 계산
		// 조건1: 위치에 다른 기물이 없어야 함
		// 조건2: 위치에 다른 기물이 있는 경우 piece가 서로 다른 색상이어야 함
		// 조건3: 위치에 다른 기물이 없지만 특수한 조건상 이동이 가능해야함 (ex: 폰 대각선 이동)
		return new ArrayList<>();
	}
}
