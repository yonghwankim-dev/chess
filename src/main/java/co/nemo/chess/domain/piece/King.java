package co.nemo.chess.domain.piece;

import static co.nemo.chess.domain.piece.Direction.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import co.nemo.chess.domain.board.PieceRepository;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class King extends AbstractChessPiece {

	King(Location location, Color color, boolean isMoved, Deque<Location> locationHistory) {
		super(location, color, isMoved, locationHistory);
	}

	public static AbstractChessPiece notMovedWhiteKing(Location location) {
		return new King(location, Color.WHITE, false, new ArrayDeque<>());
	}

	public static AbstractChessPiece notMOvedDarkKing(Location location) {
		return new King(location, Color.DARK, false, new ArrayDeque<>());
	}

	@Override
	AbstractChessPiece movedPiece(Location location, Color color, Deque<Location> moveHistory) {
		return new King(location, color, true, moveHistory);
	}

	@Override
	protected AbstractChessPiece withLocationHistory(Location location, Color color, boolean isMoved,
		Deque<Location> locationHistory) {
		return new King(location, color, isMoved, locationHistory);
	}

	@Override
	AttackType calAttackType(Location destination, PieceRepository repository) {
		return AttackType.NORMAL;
	}

	@Override
	public List<Location> findAllMoveLocations() {
		int distance = 1;
		return Stream.of(UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT)
			.map(direction -> this.calLocation(direction, distance))
			.flatMap(Optional::stream)
			.toList();
	}

	@Override
	public boolean canMove(Location location, PieceRepository repository) {
		int fileDiff;
		int rankDiff;
		Direction direction = this.calDirection(location);

		// 이동 후 체크 상태에 빠지는지 여부 검사
		if (isInCheckAfterMove(location, repository)) {
			return false;
		}

		// 이동이 캐슬링 이동인 경우를 검사한다
		if (isCastling(location, repository)) {
			return true;
		}

		// 일반적인 이동인 경우 검사
		return switch (direction) {
			case UP, DOWN -> {
				fileDiff = 0;
				rankDiff = 1;
				yield this.isValidLocationDifference(location, fileDiff, rankDiff);
			}
			case LEFT, RIGHT -> {
				fileDiff = 1;
				rankDiff = 0;
				yield this.isValidLocationDifference(location, fileDiff, rankDiff);
			}
			case UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT -> {
				fileDiff = 1;
				rankDiff = 1;
				yield this.isValidLocationDifference(location, fileDiff, rankDiff);
			}
			default -> false;
		};
	}

	private boolean isCastling(Location location, PieceRepository repository) {
		if (isMoved() || isExistPieceOnCastling(location, repository)) {
			return false;
		}

		// 현재 킹이 체크 상태라면 캐슬링 불가능
		if (isCheckedStatus(repository)) {
			return false;
		}

		if (isWhite()) {
			return location.equals(Location.from("g1")) || location.equals(Location.from("c1"));
		} else if (isDark()) {
			return location.equals(Location.from("g8")) || location.equals(Location.from("c8"));
		}
		return false;
	}

	// 룩 기물을 제외한 다른 목적지까지의 다른 기물이 있는지 검사
	private boolean isExistPieceOnCastling(Location destination, PieceRepository repository) {
		return this.calBetweenLocations(destination).stream()
			.filter(loc -> !loc.equals(destination))
			.anyMatch(loc -> repository.find(loc)
				.filter(piece -> !(piece instanceof Rook))
				.filter(this::existPiece)
				.isPresent()
			);
	}

	private boolean isInCheckAfterMove(Location destination, PieceRepository repository) {
		AbstractChessPiece king = movedPiece(destination);
		List<Piece> pieces = repository.findAll();
		pieces.remove(this);
		pieces.add(king);
		try {
			Color kingColor = isWhite() ? Color.WHITE : Color.DARK;
			return pieces.stream()
				.filter(piece -> !(piece instanceof King))
				.filter(piece -> !piece.isColorOf(kingColor))
				.anyMatch(piece -> piece.canAttack(king, pieces));
		} catch (Exception e) {
			throw new IllegalArgumentException("invalid destination, destination=" + destination);
		}
	}

	public boolean isCheckedStatus(PieceRepository repository) {
		// 킹을 제외한 다른 기물들이 공격이 가능하면 체크 상태
		Color color = this.isWhite() ? Color.WHITE : Color.DARK;
		return repository.findAll().stream()
			.filter(piece -> !piece.equals(this)) // 자신 제외
			.filter(piece -> !piece.isColorOf(color)) // 반대 색상
			.anyMatch(piece -> piece.canAttack(this, repository));
	}
}
