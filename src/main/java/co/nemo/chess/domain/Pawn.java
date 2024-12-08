package co.nemo.chess.domain;

import static co.nemo.chess.domain.Direction.*;

import java.util.List;

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
		if (isSameColor(Color.WHITE) && !isMoved() && direction == UP) {
			return locationDifference.isEqualDistance(fileDifference, rankDifference);
		} else if (isSameColor(Color.DARK) && !isMoved() && direction == DOWN) {
			return locationDifference.isEqualDistance(fileDifference, rankDifference);
		} else {
			return false;
		}
	}

	private boolean isDiagonalMove(Location location) {
		Direction direction = this.calDirection(location);
		if (!List.of(UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT).contains(direction)) {
			return false;
		}

		int fileDiff = 1;
		int rankDiff = 1;
		LocationDifference locationDifference = diffLocation(location);
		if (isSameColor(Color.WHITE) && List.of(UP_LEFT, UP_RIGHT).contains(direction)) {
			return locationDifference.isEqualDistance(fileDiff, rankDiff);
		} else if (isSameColor(Color.DARK) && List.of(DOWN_LEFT, DOWN_RIGHT).contains(direction)) {
			return locationDifference.isEqualDistance(fileDiff, rankDiff);
		} else {
			return false;
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
