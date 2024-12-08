package co.nemo.chess.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Pawn extends AbstractChessPiece implements ForwardMovable, DiagonalMovable {

	private Pawn(Location location, Color color, boolean isMoved) {
		super(location, color, isMoved);
	}

	public static AbstractChessPiece whitePawn(String position) {
		Location location = Location.from(position);
		return new Pawn(location, Color.WHITE, false);
	}

	public static AbstractChessPiece darkPawn(String position) {
		Location location = Location.from(position);
		return new Pawn(location, Color.DARK, false);
	}

	@Override
	boolean canMove(Location newLocation, Direction direction) {
		if (isOneForward(newLocation, direction)) {
			return true;
		} else if (isTwoForward(newLocation)) {
			return true;
		}
		return isDiagonalMove(newLocation);
	}

	private boolean isOneForward(Location newLocation, Direction direction) {
		int fileDifference = diffFile(newLocation);
		int rankDifference = diffRank(newLocation);
		if (isSameColor(Color.WHITE)) {
			return direction == Direction.UP && fileDifference == 0 && rankDifference == 1;
		} else if (isSameColor(Color.DARK)) {
			return direction == Direction.DOWN && fileDifference == 0 && rankDifference == 1;
		} else {
			return false;
		}
	}

	private boolean isTwoForward(Location newLocation) {
		int fileDifference = diffFile(newLocation);
		int rankDifference = diffRank(newLocation);
		if (isSameColor(Color.WHITE)) {
			return !isMoved() && fileDifference == 0 && rankDifference == 2;
		} else if (isSameColor(Color.DARK)) {
			return !isMoved() && fileDifference == 0 && rankDifference == -2;
		} else {
			return false;
		}
	}

	private boolean isDiagonalMove(Location location) {
		return false;
	}

	@Override
	Pawn valueOf(Location location, Color color, boolean isMoved) {
		return new Pawn(location, color, isMoved);
	}

	@Override
	public AbstractChessPiece moveForwardly() {
		int distance = 1;
		return newLocation(calMoveLocation(distance)).withMoved();
	}

	Direction getMoveDirection() {
		return isSameColor(Color.WHITE) ? Direction.UP : Direction.DOWN;
	}

	@Override
	public AbstractChessPiece moveDiagonally(Direction direction) {
		if (isSameColor(Color.WHITE) && direction != Direction.UP_LEFT && direction != Direction.UP_RIGHT) {
			throw new IllegalArgumentException("The White Pawn can only move in the UP_LEFT or UP_RIGHT directions.");
		} else if (isSameColor(Color.DARK) && direction != Direction.DOWN_LEFT && direction != Direction.DOWN_RIGHT) {
			throw new IllegalArgumentException(
				"The Dark Pawn can only move in the DOWN_LEFT or DOWN_RIGHT directions.");
		}
		int rankDistance = 1;
		int fileDistance = 1;
		Location newLocation = adjustDiagonal(direction, fileDistance, rankDistance);
		return this
			.withLocation(newLocation)
			.withMoved();
	}
}
