package co.nemo.chess.domain.piece;

import static co.nemo.chess.domain.piece.Direction.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.nemo.chess.domain.board.PieceRepository;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Pawn extends AbstractChessPiece {

	private Pawn(Location location, Color color, boolean isMoved) {
		super(location, color, isMoved);
	}

	public static AbstractChessPiece notMovedWhitePawn(Location location) {
		return new Pawn(location, Color.WHITE, false);
	}

	public static AbstractChessPiece notMovedDarkPawn(Location location) {
		return new Pawn(location, Color.DARK, false);
	}

	@Override
	public boolean canMove(Location newLocation, PieceRepository repository) {
		return isOneForward(newLocation) || isTwoForward(newLocation) || isDiagonalMove(newLocation, repository)
			|| isEnPassant(newLocation, repository);
	}

	private boolean isOneForward(Location newLocation) {
		int fileDifference = 0;
		int rankDifference = 1;
		LocationDifference locationDifference = diffLocation(newLocation);
		Direction direction = calDirection(newLocation);
		if (isSameColor(Color.WHITE) && direction == UP) {
			return locationDifference.isEqualDistance(fileDifference, rankDifference);
		} else if (isSameColor(Color.DARK) && direction == DOWN) {
			return locationDifference.isEqualDistance(fileDifference, rankDifference);
		} else {
			return false;
		}
	}

	private boolean isTwoForward(Location newLocation) {
		int fileDifference = 0;
		int rankDifference = 2;
		Direction direction = calDirection(newLocation);
		LocationDifference locationDifference = diffLocation(newLocation);
		if (isMoved()) {
			return false;
		}
		if (isSameColor(Color.WHITE) && isNotMoved() && direction == UP) {
			return locationDifference.isEqualDistance(fileDifference, rankDifference);
		} else if (isSameColor(Color.DARK) && isNotMoved() && direction == DOWN) {
			return locationDifference.isEqualDistance(fileDifference, rankDifference);
		} else {
			return false;
		}
	}

	private boolean isDiagonalMove(Location location, PieceRepository repository) {
		Direction direction = this.calDirection(location);
		if (!List.of(UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT).contains(direction)) {
			return false;
		}

		int fileDiff = 1;
		int rankDiff = 1;
		LocationDifference locationDifference = diffLocation(location);
		if (isSameColor(Color.WHITE) && List.of(UP_LEFT, UP_RIGHT).contains(direction)) {
			// 거리가 일치해야 하고, 흑색 기물이 존재해야 한다
			Piece piece = repository.find(location);
			if (!piece.isColorOf(Color.DARK)) {
				return false;
			}
			return locationDifference.isEqualDistance(fileDiff, rankDiff);
		} else if (isSameColor(Color.DARK) && List.of(DOWN_LEFT, DOWN_RIGHT).contains(direction)) {
			// 거리가 일치해야 하고, 백색 기물이 존재해야 한다
			Piece piece = repository.find(location);
			if (!piece.isColorOf(Color.WHITE)) {
				return false;
			}
			return locationDifference.isEqualDistance(fileDiff, rankDiff);
		} else {
			return false;
		}
	}

	private boolean isEnPassant(Location location, PieceRepository repository) {
		Direction direction = this.calDirection(location);
		int fileDiff = 1;
		int rankDiff = 1;
		LocationDifference locationDifference = diffLocation(location);
		if (isSameColor(Color.WHITE) && direction == UP_LEFT) {
			// 폰의 현재 위치를 기준으로 좌측에 적 기물이 있는지 확인한다
			Optional<Location> optional = calLocation(LEFT, 1);
			if (optional.isEmpty()) {
				return false;
			}
			Location leftLocation = optional.get();
			// 좌측에 적 기물이 존재하고 좌상단으로 이동이 가능하면 true 반환, 그 외는 false
			Piece leftPiece = repository.find(leftLocation);
			return leftPiece.isColorOf(Color.DARK) && locationDifference.isEqualDistance(fileDiff, rankDiff);
		}
		return false;
	}

	@Override
	AbstractChessPiece movedPiece(Location location, Color color) {
		return new Pawn(location, color, true);
	}

	@Override
	public List<Location> findAllMoveLocations() {
		List<Location> result = new ArrayList<>();
		if (isSameColor(Color.WHITE)) {
			super.calLocation(UP, 1).ifPresent(result::add);
			super.calLocation(UP, 2).ifPresent(result::add);
			super.calLocation(UP_LEFT, 1).ifPresent(result::add);
			super.calLocation(UP_RIGHT, 1).ifPresent(result::add);
		} else {
			super.calLocation(DOWN, 1).ifPresent(result::add);
			super.calLocation(DOWN, 2).ifPresent(result::add);
			super.calLocation(DOWN_LEFT, 1).ifPresent(result::add);
			super.calLocation(DOWN_RIGHT, 1).ifPresent(result::add);
		}
		return result;
	}

	@Override
	AbstractChessPiece relocatePieces(AbstractChessPiece piece, Location destination, PieceRepository repository) {
		AbstractChessPiece result = null;
		// 앙파상인 경우 좌측 또는 우측의 기물을 제거한다
		// 공격 종류를 계산한다
		AttackType type = piece.calAttackType(destination, repository);
		// 일반 공격인 경우 destination에 위치한 적 기물을 제거한다
		if (type == AttackType.NORMAL) {
			// 현재 기물 삭제
			repository.poll(piece);
			// 목적지의 기물 삭제
			repository.poll(destination);
			// 현재 기물을 기반으로 목적지 위치의 기물 생성
			AbstractChessPiece newPiece = piece.movedPiece(destination);
			repository.add(newPiece);
			return newPiece;
		}
		return result;
	}

	@Override
	protected AttackType calAttackType(Location destination, PieceRepository repository) {
		if (isOneForward(destination) || isTwoForward(destination) || isDiagonalMove(destination, repository)) {
			return AttackType.NORMAL;
		} else if (isEnPassant(destination, repository)) {
			return AttackType.EN_PASSANT;
		}
		return AttackType.NONE;
	}
}
