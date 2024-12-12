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

	public static PieceMovable init(Piece... pieces) {
		return new Board(PieceRepository.init(pieces));
	}

	@Override
	public Optional<Piece> movePiece(Location src, Location dst) {
		return Optional.empty();
	}

	/**
	 * 매개변수 src 위치에 있는 기물의 이동 가능한 경로를 계산한다
	 * 조건
	 * - 위치가 비어있거나 적 기물이 존재해야 함
	 * - 해당 기물이 목적지(적기물, 빈공간)로 이동 가능해야함
	 * @param src 출발 지역
	 * @return src 위치에 존재한 기물이 이동가능한 Location 리스트
	 */
	@Override
	public List<Location> findPossiblePaths(Location src) {
		List<Location> result = new ArrayList<>();
		Piece piece = repository.find(src);
		List<Location> possibleLocations = piece.findAllMoveLocations();
		for (Location location : possibleLocations) {
			Piece target = repository.find(location);
			if (piece.canAttack(target, repository)) {
				result.add(location);
			}
		}
		return result;
	}
}
