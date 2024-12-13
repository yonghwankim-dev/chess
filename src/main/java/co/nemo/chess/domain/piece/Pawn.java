package co.nemo.chess.domain.piece;

import static co.nemo.chess.domain.piece.Direction.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import co.nemo.chess.domain.board.PieceRepository;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Pawn extends AbstractChessPiece {

	private Pawn(Location location, Color color, boolean isMoved, Deque<Location> moveHistory) {
		super(location, color, isMoved, moveHistory);
	}

	public static AbstractChessPiece notMovedWhitePawn(Location location) {
		return new Pawn(location, Color.WHITE, false, new ArrayDeque<>());
	}

	public static AbstractChessPiece notMovedDarkPawn(Location location) {
		return new Pawn(location, Color.DARK, false, new ArrayDeque<>());
	}

	@Override
	AbstractChessPiece movedPiece(Location location, Color color, Deque<Location> moveHistory) {
		return new Pawn(location, color, true, moveHistory);
	}

	@Override
	AbstractChessPiece withLocationHistory(Location location, Color color, boolean isMoved,
		Deque<Location> locationHistory) {
		return new Pawn(location, color, isMoved, locationHistory);
	}

	@Override
	protected AttackType calAttackType(Location destination, PieceRepository repository) {
		if (isOneForward(destination) || isTwoForward(destination) || isDiagonalMove(destination, repository)) {
			return AttackType.NORMAL;
		} else if (isEnPassant(destination, repository)) {
			return AttackType.EN_PASSANT;
		} else {
			return AttackType.NONE;
		}
	}

	@Override
	public List<Location> findAllMoveLocations() {
		List<Location> result = new ArrayList<>();
		if (isColorOf(Color.WHITE)) {
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
	public boolean canMove(Location newLocation, PieceRepository repository) {
		return isOneForward(newLocation) ||
			isTwoForward(newLocation) ||
			isDiagonalMove(newLocation, repository) ||
			isEnPassant(newLocation, repository);
	}

	private boolean isOneForward(Location newLocation) {
		int fileDiff = 0;
		int rankDiff = 1;
		Direction direction = calDirection(newLocation);
		if (isColorOf(Color.WHITE) && direction == UP) {
			return this.isValidLocationDifference(newLocation, fileDiff, rankDiff);
		} else if (isColorOf(Color.DARK) && direction == DOWN) {
			return this.isValidLocationDifference(newLocation, fileDiff, rankDiff);
		} else {
			return false;
		}
	}

	private boolean isValidLocationDifference(Location location, int fileDiff, int rankDiff) {
		LocationDifference locationDifference = super.diffLocation(location);
		return locationDifference.isEqualDistance(fileDiff, rankDiff);
	}

	private boolean isTwoForward(Location newLocation) {
		int fileDifference = 0;
		int rankDifference = 2;
		Direction direction = calDirection(newLocation);
		if (isMoved()) {
			return false;
		} else if (isColorOf(Color.WHITE) && isNotMoved() && direction == UP) {
			return this.isValidLocationDifference(newLocation, fileDifference, rankDifference);
		} else if (isColorOf(Color.DARK) && isNotMoved() && direction == DOWN) {
			return this.isValidLocationDifference(newLocation, fileDifference, rankDifference);
		} else {
			return false;
		}
	}

	/**
	 * 폰 기물이 location 위치로 대각선 이동이 가능한지 여부
	 * 조건
	 * - 백색 기물인 경우 거리가 1칸으로 일치해야 하고, 대각선 위치에 흑색 기물이 존재해야 함
	 * - 흑색 기물인 경우 거리가 1칸으로 일치해야 하고, 대각선 위치에 백색 기물이 존재해야 함
	 * @param location 목적지
	 * @param repository 기물 저장소
	 * @return 이동 가능 여부
	 */
	private boolean isDiagonalMove(Location location, PieceRepository repository) {
		int fileDiff = 1;
		int rankDiff = 1;
		Direction direction = this.calDirection(location);
		if (isColorOf(Color.WHITE) && List.of(UP_LEFT, UP_RIGHT).contains(direction)) {
			boolean isValidColor = repository.find(location).isColorOf(Color.DARK);
			return isValidColor && this.isValidLocationDifference(location, fileDiff, rankDiff);
		} else if (isColorOf(Color.DARK) && List.of(DOWN_LEFT, DOWN_RIGHT).contains(direction)) {
			boolean isValidColor = repository.find(location).isColorOf(Color.WHITE);
			return isValidColor && this.isValidLocationDifference(location, fileDiff, rankDiff);
		} else {
			return false;
		}
	}

	/**
	 * 앙파상 이동이 가능한지 검사한다
	 * 조건
	 * - 잡는 기물과 잡히는 기물 모두가 폰이어야 한다
	 * - 적의 폰이 초기 배치에서 2칸 전진한 직후(다음 순서)에만 유효하다
	 * - 앙파상으로 잡은 폰은 반드시 옆의 행으로 이동된다. 예를 들어 e5 백폰이 d5 흑폰을 제거하고 d6 백폰이 된다
	 * @param location 목적지
	 * @param repository 기물 저장소
	 * @return 이동 가능 여부
	 */
	private boolean isEnPassant(Location location, PieceRepository repository) {
		int fileDiff = 1;
		int rankDiff = 1;
		if (!isValidLocationDifference(location, fileDiff, rankDiff)) {
			return false;
		}

		// 잡는 기물과 잡히는 기물 모두 폰이어야 한다
		Direction direction = this.calDirection(location);
		if (isColorOf(Color.WHITE) && direction == UP_LEFT) {
			return this.isEnemyPieceOnDirection(LEFT, Color.DARK, repository);
		} else if (isColorOf(Color.WHITE) && direction == UP_RIGHT) {
			return this.isEnemyPieceOnDirection(RIGHT, Color.DARK, repository);
		} else if (isColorOf(Color.DARK) && direction == DOWN_LEFT) {
			return this.isEnemyPieceOnDirection(LEFT, Color.WHITE, repository);
		} else if (isColorOf(Color.DARK) && direction == DOWN_RIGHT) {
			return this.isEnemyPieceOnDirection(RIGHT, Color.WHITE, repository);
		} else {
			return false;
		}
	}

	private Boolean isEnemyPieceOnDirection(Direction direction, Color color, PieceRepository repository) {
		// 적의 폰이 초기 배치에서 2칸 전진한 직후(다음 순서)가 아니라면 앙파상 불가능
		int distance = 1;
		return isValidEnPassantTargetOn(direction, repository, distance) &&
			isSameColorOnDirection(direction, color, repository, distance);
	}

	private boolean isValidEnPassantTargetOn(Direction direction, PieceRepository repository, int distance) {
		return this.calLocation(direction, distance)
			.map(repository::find)
			.filter(Pawn.class::isInstance)
			.map(Pawn.class::cast)
			.map(Pawn::isInitialTwoForward)
			.orElse(false);
	}

	private Boolean isSameColorOnDirection(Direction direction, Color color, PieceRepository repository, int distance) {
		return this.calLocation(direction, distance)
			.map(repository::find)
			.map(piece -> piece.isColorOf(color))
			.orElse(false);
	}

	/**
	 * 기물이 초기 배치에서 2칸 전진한 직후인지 여부 확인
	 * @return true: 바로 직전에 2칸 전진함, false: 바로 직전에 2칸 전진하지 않음
	 */
	private boolean isInitialTwoForward() {
		return super.getLastMovedLocation()
			.map(location -> {
				int fileDiff = 0;
				int rankDiff = 2;
				return this.isValidLocationDifference(location, fileDiff, rankDiff);
			})
			.orElse(false);
	}
}
