package co.nemo.chess.domain.piece;

import static co.nemo.chess.domain.piece.Direction.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.nemo.chess.domain.board.PieceRepository;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Pawn extends AbstractChessPiece {

	private Pawn(Location location, Color color, boolean isMoved) {
		super(location, color, isMoved);
	}

	public static AbstractChessPiece notMovedWhitePawn(Location location) {
		return new Pawn(location, Color.WHITE, false);
	}

	public static AbstractChessPiece notMovedDarkPawn(Location location) {
		return new Pawn(location, Color.DARK, false);
	}

	@Override
	public boolean canMove(Location newLocation, PieceRepository repository) {
		return isOneForward(newLocation) || isTwoForward(newLocation) || isDiagonalMove(newLocation, repository);
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
		if (isMoved()) {
			return false;
		}
		if (isSameColor(Color.WHITE) && isNotMoved() && direction == UP) {
			return locationDifference.isEqualDistance(fileDifference, rankDifference);
		} else if (isSameColor(Color.DARK) && isNotMoved() && direction == DOWN) {
			return locationDifference.isEqualDistance(fileDifference, rankDifference);
		} else {
			return false;
		}
	}

	private boolean isDiagonalMove(Location location, PieceRepository repository) {
		Direction direction = this.calDirection(location);
		if (!List.of(UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT).contains(direction)) {
			return false;
		}

		int fileDiff = 1;
		int rankDiff = 1;
		LocationDifference locationDifference = diffLocation(location);
		if (isSameColor(Color.WHITE) && List.of(UP_LEFT, UP_RIGHT).contains(direction)) {
			// 거리가 일치해야 하고, 흑색 기물이 존재해야 한다
			Optional<Piece> piece = repository.find(location).filter(p -> p.isColorOf(Color.DARK));
			if (piece.isEmpty()) {
				return false;
			}
			return locationDifference.isEqualDistance(fileDiff, rankDiff);
		} else if (isSameColor(Color.DARK) && List.of(DOWN_LEFT, DOWN_RIGHT).contains(direction)) {
			// 거리가 일치해야 하고, 백색 기물이 존재해야 한다
			Optional<Piece> piece = repository.find(location).filter(p -> p.isColorOf(Color.WHITE));
			if (piece.isEmpty()) {
				return false;
			}
			return locationDifference.isEqualDistance(fileDiff, rankDiff);
		} else {
			return false;
		}
	}

	@Override
	Pawn movedPiece(Location location, Color color) {
		return new Pawn(location, color, true);
	}

	@Override
	public List<Location> findPossibleLocations() {
		List<Location> result = new ArrayList<>();
		if (isSameColor(Color.WHITE)) {
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
}
