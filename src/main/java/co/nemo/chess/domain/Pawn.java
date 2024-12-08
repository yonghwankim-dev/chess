package co.nemo.chess.domain;

import static co.nemo.chess.domain.Direction.*;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Pawn extends AbstractChessPiece {

	private Pawn(Location location, Color color, boolean isMoved) {
		super(location, color, isMoved);
	}

	public static AbstractChessPiece newInstance(Location location, Color color, boolean isMoved) {
		return new Pawn(location, color, isMoved);
	}

	@Override
	boolean canMove(Location newLocation) {
		return isOneForward(newLocation) || isTwoForward(newLocation) || isDiagonalMove(newLocation);
	}

	private boolean isOneForward(Location newLocation) {
		int fileDifference = Math.abs(diffFile(newLocation));
		int rankDifference = Math.abs(diffRank(newLocation));
		Direction direction = calDirection(newLocation);
		if (isSameColor(Color.WHITE)) {
			return direction == UP && fileDifference == 0 && rankDifference == 1;
		} else if (isSameColor(Color.DARK)) {
			return direction == DOWN && fileDifference == 0 && rankDifference == 1;
		} else {
			return false;
		}
	}

	private boolean isTwoForward(Location newLocation) {
		int fileDifference = Math.abs(diffFile(newLocation));
		int rankDifference = Math.abs(diffRank(newLocation));
		Direction direction = calDirection(newLocation);
		if (isSameColor(Color.WHITE)) {
			return !isMoved() && direction == UP && fileDifference == 0 && rankDifference == 2;
		} else if (isSameColor(Color.DARK)) {
			return !isMoved() && direction == DOWN && fileDifference == 0 && rankDifference == 2;
		} else {
			return false;
		}
	}

	private boolean isDiagonalMove(Location location) {
		int fileDifference = Math.abs(diffFile(location));
		int rankDifference = Math.abs(diffRank(location));
		Direction direction = this.calDirection(location);
		if (direction != UP_LEFT && direction != UP_RIGHT && direction != DOWN_LEFT && direction != DOWN_RIGHT) {
			return false;
		}
		if (isSameColor(Color.WHITE)) {
			// 상좌
			if (direction == UP_LEFT && fileDifference == 1 && rankDifference == 1) {
				return true;
			}
			// 상우
			return direction == UP_RIGHT && fileDifference == 1 && rankDifference == 1;
		} else {
			// 하좌
			if (direction == DOWN_LEFT && fileDifference == 1 && rankDifference == 1) {
				return true;
			}
			// 하우
			return direction == DOWN_RIGHT && fileDifference == 1 && rankDifference == 1;
		}
	}

	@Override
	Pawn valueOf(Location location, Color color, boolean isMoved) {
		return new Pawn(location, color, isMoved);
	}

	Direction getMoveDirection() {
		return isSameColor(Color.WHITE) ? UP : DOWN;
	}
}
