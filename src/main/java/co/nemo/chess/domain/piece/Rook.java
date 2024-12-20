package co.nemo.chess.domain.piece;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import co.nemo.chess.domain.board.PieceRepository;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Rook extends AbstractChessPiece {
	private Rook(Location location, Color color, boolean isMoved, Deque<Location> locationHistory) {
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
		for (Direction direction : Direction.rookDirections()) {
			Optional<Location> optional;
			int distance = 1;
			// 해당 방향으로 더이상 움직일 수 없을때까지 반복한다
			do {
				optional = super.calLocation(direction, distance);
				optional.ifPresent(result::add);
				distance++;
			} while (optional.isPresent());
		}
		return result;
	}

	@Override
	public boolean canMove(Location destination, PieceRepository repository) {
		if (isCastlingMove(destination, repository)) {
			return true;
		}
		return emptyPieceUntil(destination, repository) &&
			canMoveStraight(destination);
	}

	private boolean isCastlingMove(Location destination, PieceRepository repository) {
		return super.isNotMoved() &&
			isEmptyPieceOnCastling(destination, repository) &&
			getCastlingLocations().contains(destination);
	}

	private List<Location> getCastlingLocations() {
		return super.isWhite() ?
			List.of(Location.from("f1"), Location.from("d1")) :
			List.of(Location.from("f8"), Location.from("d8"));
	}

	// 킹 기물을 제외한 다른 목적지까지의 다른 기물이 없는지 검사
	private boolean isEmptyPieceOnCastling(Location destination, PieceRepository repository) {
		return this.calBetweenLocations(destination).stream()
			.noneMatch(loc -> repository.find(loc)
				.filter(piece -> !(piece instanceof King))
				.filter(this::existPiece)
				.isPresent()
			);
	}

	private boolean canMoveStraight(Location destination) {
		return Direction.rookDirections().contains(super.calDirection(destination));
	}

	@Override
	public String toSymbol() {
		return isWhite() ? "♖" : "♜";
	}
}
