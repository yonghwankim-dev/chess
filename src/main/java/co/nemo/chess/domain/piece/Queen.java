package co.nemo.chess.domain.piece;

import static co.nemo.chess.domain.piece.Direction.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import co.nemo.chess.domain.board.PieceRepository;

public class Queen extends AbstractChessPiece {

	private Queen(Location location, Color color, boolean isMoved, Deque<Location> locationHistory) {
		super(location, color, isMoved, locationHistory);
	}

	public static AbstractChessPiece notMovedWhiteQueen(Location location) {
		return new Queen(location, Color.WHITE, false, new ArrayDeque<>());
	}

	public static AbstractChessPiece notMovedDarkQueen(Location location) {
		return new Queen(location, Color.DARK, false, new ArrayDeque<>());
	}

	@Override
	AbstractChessPiece movedPiece(Location location, Color color, Deque<Location> moveHistory) {
		return new Queen(location, color, true, moveHistory);
	}

	@Override
	protected AbstractChessPiece withLocationHistory(Location location, Color color, boolean isMoved,
		Deque<Location> locationHistory) {
		return new Queen(location, color, isMoved, locationHistory);
	}

	@Override
	AttackType calAttackType(Location destination, PieceRepository repository) {
		return AttackType.NORMAL;
	}

	@Override
	public List<Location> findAllMoveLocations() {
		List<Location> result = new ArrayList<>();
		for (Direction direction : List.of(UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT)) {
			int distance = 1;
			Optional<Location> location;

			while ((location = this.calLocation(direction, distance)).isPresent()) {
				result.add(location.get());
				distance++;
			}
		}
		return result;
	}

	@Override
	public boolean canMove(Location location, PieceRepository repository) {
		if (this.existPieceUntil(location, repository)) {
			return false;
		}
		// 일반적인 이동인 경우 검사
		return List.of(UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT)
			.contains(calDirection(location));
	}
}
