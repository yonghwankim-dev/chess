package co.nemo.chess.domain.piece;

import static co.nemo.chess.domain.piece.Direction.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import co.nemo.chess.domain.board.PieceRepository;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Rook extends AbstractChessPiece {
	Rook(Location location, Color color, boolean isMoved, Deque<Location> locationHistory) {
		super(location, color, isMoved, locationHistory);
	}

	public static AbstractChessPiece notMovedWhiteRook(Location location) {
		return new Rook(location, Color.WHITE, false, new ArrayDeque<>());
	}

	public static AbstractChessPiece notMovedDarkRook(Location location) {
		return new Rook(location, Color.DARK, false, new ArrayDeque<>());
	}

	@Override
	AbstractChessPiece movedPiece(Location location, Color color, Deque<Location> moveHistory) {
		return new Rook(location, color, true, moveHistory);
	}

	@Override
	protected AbstractChessPiece withLocationHistory(Location location, Color color, boolean isMoved,
		Deque<Location> locationHistory) {
		return new Rook(location, color, isMoved, locationHistory);
	}

	@Override
	protected AttackType calAttackType(Location destination, PieceRepository repository) {
		if (canMoveStraight(destination)) {
			return AttackType.NORMAL;
		}
		return AttackType.NONE;
	}

	@Override
	public List<Location> findAllMoveLocations() {
		List<Location> result = new ArrayList<>();
		List<Direction> directions = List.of(UP, DOWN, LEFT, RIGHT);
		for (Direction direction : directions) {
			Optional<Location> optional;
			int distance = 1;
			// 해당 방향으로 더이상 움직일 수 없을때까지 반복한다
			do {
				optional = this.calLocation(direction, distance);
				optional.ifPresent(result::add);
				distance++;
			} while (optional.isPresent());
		}
		return result;
	}

	@Override
	public boolean canMove(Location destination, PieceRepository repository) {
		if (isCastling(destination, repository)) {
			return true;
		}
		// 중간에 기물이 없어야 한다
		if (existPieceBetween(destination, repository)) {
			return false;
		}
		return canMoveStraight(destination);
	}

	private boolean isCastling(Location destination, PieceRepository repository) {
		if (isMoved() || isExistPieceOnCastling(destination, repository)) {
			return false;
		}
		if (isWhite()) {
			return destination.equals(Location.from("f1")) || destination.equals(Location.from("d1"));
		} else if (isDark()) {
			return destination.equals(Location.from("f8")) || destination.equals(Location.from("d8"));
		}
		return false;
	}

	// 킹 기물을 제외한 다른 목적지까지의 다른 기물이 있는지 검사
	private boolean isExistPieceOnCastling(Location destination, PieceRepository repository) {
		return this.calBetweenLocations(destination).stream()
			.filter(loc -> !loc.equals(destination))
			.anyMatch(loc -> repository.find(loc)
				.filter(piece -> !(piece instanceof King))
				.filter(this::existPiece)
				.isPresent()
			);
	}

	private boolean canMoveStraight(Location destination) {
		return List.of(UP, DOWN, LEFT, RIGHT).contains(calDirection(destination));
	}
}
