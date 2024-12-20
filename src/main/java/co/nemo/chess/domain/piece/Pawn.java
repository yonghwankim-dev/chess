package co.nemo.chess.domain.piece;

import static co.nemo.chess.domain.piece.Direction.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.board.PieceRepository;
import co.nemo.chess.domain.game.ChessGameReader;
import co.nemo.chess.domain.game.ChessGameWriter;
import co.nemo.chess.domain.command.AbstractCommand;
import co.nemo.chess.domain.player.Player;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Pawn extends AbstractChessPiece implements PawnStrategy {

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
	protected AbstractChessPiece withLocationHistory(Location location, Color color, boolean isMoved,
		Deque<Location> locationHistory) {
		return new Pawn(location, color, isMoved, locationHistory);
	}

	@Override
	protected AttackType calAttackType(Location destination, PieceRepository repository) {
		if (isOneForward(destination, repository) ||
			isTwoForward(destination, repository) ||
			isDiagonalMove(destination, repository)) {
			return AttackType.NORMAL;
		} else if (isEnPassant(destination, repository)) {
			return AttackType.EN_PASSANT;
		} else {
			return AttackType.NONE;
		}
	}

	private boolean isOneForward(Location destination, PieceRepository repository) {
		Direction moveDirection = calDirection(destination);
		return emptyPieceUntil(destination, repository) &&
			moveDirection == getForwardDirection() &&
			moveDirection.isEqualDistance(this, destination);
	}

	@Override
	public Direction getForwardDirection() {
		return isWhite() ? UP : DOWN;
	}

	@Override
	public AbstractChessPiece promoTo(PieceType type) throws IllegalStateException {
		if (canPromote()) {
			return super.createPiece(type);
		}
		throw new IllegalStateException("Promotion is not allowed for this pawn.");
	}

	@Override
	public boolean canPromote() {
		final int WHITE_END_RANK = 8;
		Rank currentRank = Rank.from(WHITE_END_RANK);
		if (isWhite() && this.isOnRank(currentRank)) {
			return true;
		}
		final int DARK_END_RANK = 1;
		currentRank = Rank.from(DARK_END_RANK);
		return isDark() && this.isOnRank(currentRank);
	}

	/**
	 * 중간 경로에 기물이 있는지 여부에서 목적지를 포함하느냐의 여부
	 * @param curLocation 현재 위치
	 * @param destination 목적지
	 * @return true: 목적지 포함, false: 목적지 마포함
	 */
	@Override
	boolean shouldContainDestination(Location curLocation, Location destination) {
		return false;
	}

	private boolean isTwoForward(Location destination, PieceRepository repository) {
		if (existPieceUntil(destination, repository)) {
			return false;
		} else if (isWhiteTwoForward(destination) || isDarkTwoForward(destination)) {
			int fileDifference = 0;
			int rankDifference = 2;
			return this.isValidLocationDifference(destination, fileDifference, rankDifference);
		}
		return false;
	}

	private boolean isWhiteTwoForward(Location destination) {
		return isWhite() && super.calDirection(destination) == UP && isNotMoved();
	}

	private boolean isDarkTwoForward(Location destination) {
		return isDark() && super.calDirection(destination) == DOWN && isNotMoved();
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
		Direction direction = super.calDirection(location);
		List<Direction> directions = getDiagonalDirections();
		boolean isEnemyColor = isEnemyColorOn(location, repository);
		return directions.contains(direction) &&
			direction.isEqualDistance(this, location) &&
			isEnemyColor;
	}

	private boolean isEnemyColorOn(Location location, PieceRepository repository) {
		return repository.find(location)
			.filter(piece -> !piece.isSameColor(this))
			.isPresent();
	}

	private List<Direction> getDiagonalDirections() {
		return isWhite() ? List.of(UP_LEFT, UP_RIGHT) : List.of(DOWN_LEFT, DOWN_RIGHT);
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
		Direction direction = super.calDirection(location);
		if (!direction.isEqualDistance(this, location)) {
			return false;
		}

		// 잡는 기물과 잡히는 기물 모두 폰이어야 한다
		if (isWhite() && direction == UP_LEFT) {
			return this.isEnemyPieceOnDirection(LEFT, Color.DARK, repository);
		} else if (isWhite() && direction == UP_RIGHT) {
			return this.isEnemyPieceOnDirection(RIGHT, Color.DARK, repository);
		} else if (isDark() && direction == DOWN_LEFT) {
			return this.isEnemyPieceOnDirection(LEFT, Color.WHITE, repository);
		} else if (isDark() && direction == DOWN_RIGHT) {
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
			.flatMap(repository::find)
			.filter(Pawn.class::isInstance)
			.map(Pawn.class::cast)
			.map(Pawn::isInitialTwoForward)
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

	private Boolean isSameColorOnDirection(Direction direction, Color color, PieceRepository repository, int distance) {
		return this.calLocation(direction, distance)
			.flatMap(repository::find)
			.map(piece -> piece.isColorOf(color))
			.orElse(false);
	}

	@Override
	public List<Location> findAllMoveLocations() {
		List<Location> result = new ArrayList<>();
		if (isWhite()) {
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
		return isOneForward(newLocation, repository) ||
			isTwoForward(newLocation, repository) ||
			isDiagonalMove(newLocation, repository) ||
			isEnPassant(newLocation, repository);
	}

	// TODO: 12/20/24 프로모션 이벤트 리팩토링 
	@Override
	public void handleMoveEvent(Board board, ChessGameReader gameReader, ChessGameWriter gameWriter) {
		if (!this.canPromote()) {
			return;
		}
		gameWriter.printPromotionMessage();
		PieceType type;
		while (true) {
			try {
				type = gameReader.readPieceType();
				break;
			} catch (IllegalArgumentException e) {
				gameWriter.printErrorMessage(e);
			}
		}
		AbstractCommand command = AbstractCommand.promotionCommand(this, type);
		Player curPlayer = isWhite() ? Player.white() : Player.dark();
		boolean result = command.process(board, gameReader, gameWriter, curPlayer);
		log.info("promotion result is {}", result);
	}

	@Override
	public String toSymbol() {
		return isWhite() ? "♙" : "♟";
	}
}
