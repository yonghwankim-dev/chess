package co.nemo.chess.domain.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.NullPiece;
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

	// 기물의 이동 가능한 전체 경로 계산
	@Override
	public List<Location> findPossiblePaths(Location src) {
		List<Location> result = new ArrayList<>();
		Piece piece = repository.find(src);
		// 조건1: 위치가 비어있거나 다른색의 기물이 존재해야함
		// 조건2: 위치에 다른 기물이 없지만 특수한 조건상 이동이 가능해야함 (ex: 폰 대각선 이동)
		List<Location> possibleLocations = piece.findPossibleLocations();
		for (Location location : possibleLocations) {
			// 이동 가능한 경로에 어떤 기물이 없는 경우
			Piece target = repository.find(location);

			// location 위치가 비어있지만 기물이 해당 장소로 이동이 가능한 경우
			if (target instanceof NullPiece && piece.canMove(location, repository)) {
				result.add(location);
				continue;
			}
			// 기물이 이동은 불가능하지만 위치가 비어있는 경우
			if (target instanceof NullPiece) {
				continue;
			}
			// 기물이 존재하는 경우 공격이 가능한 경우
			if (piece.canAttack(target, repository)) {
				result.add(location);
			}
		}
		return result;
	}
}
