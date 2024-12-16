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

	private boolean isInCheckAfterMove(Location destination, PieceRepository repository) {
		// TODO: 12/16/24 destination으로 이동하는 경우에 체크 상태이면 true 반환, 아니면 false 반환 
		AbstractChessPiece king = movedPiece(destination);
		return repository.findAll().stream()
			.filter(piece -> !piece.equals(this))
			.anyMatch(piece -> piece.canAttack(king, repository));
	}

	private boolean isValidLocationDifference(Location location, int fileDiff, int rankDiff) {
		LocationDifference locationDifference = super.diffLocation(location);
		return locationDifference.isEqualDistance(fileDiff, rankDiff);
	}

	public boolean isCheckedStatus(PieceRepository repository) {
		return true;
	}
}
