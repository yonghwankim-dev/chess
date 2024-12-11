package co.nemo.chess.domain.piece;

import co.nemo.chess.domain.board.Board;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode
@Slf4j
public abstract class AbstractChessPiece implements Piece {
	private final Location location;
	private final Color color;
	private final boolean isMoved;

	AbstractChessPiece(Location location, Color color, boolean isMoved) {
		this.location = location;
		this.color = color;
		this.isMoved = isMoved;
	}

	public AbstractChessPiece withMoved() {
		return movedPiece(location, color);
	}

	boolean isSameColor(Color color) {
		return this.color == color;
	}

	@Override
	public AbstractChessPiece move(Location destination) throws IllegalArgumentException {
		if (!canMove(destination)) {
			throw new IllegalArgumentException("Invalid move for " + getClass().getSimpleName());
		}
		return movedPiece(destination, color);
	}

	@Override
	public AbstractChessPiece move(Location destination, Board board) {
		if (!canMove(destination)) {
			throw new IllegalArgumentException("Invalid move for " + getClass().getSimpleName());
		}
		// 출발지의 기물 제거
		board.pollPiece(location).ifPresent(piece -> log.info("delete Piece : {}", piece));
		// 목적지의 기물 제거
		board.pollPiece(destination).ifPresent(piece -> log.info("delete Piece : {}", piece));
		return movedPiece(destination, color);
	}

	@Override
	public boolean match(Location location) {
		return this.location.equals(location);
	}

	LocationDifference diffLocation(Location location) {
		return this.location.diff(location);
	}

	boolean isNotMoved() {
		return !this.isMoved;
	}

	Direction calDirection(Location location) {
		return this.location.calDirection(location);
	}

	Location calLocation(int fileValue, int rankValue) {
		return this.location.plus(fileValue, rankValue);
	}

	abstract boolean canMove(Location newLocation);

	abstract AbstractChessPiece movedPiece(Location location, Color color);

	@Override
	public String toString() {
		String moved = isMoved ? "MOVED" : "NOT MOVED";
		return String.format("%s %s %s", moved, color, location);
	}
}
