package co.nemo.chess.domain.piece;

import java.util.Deque;
import java.util.Optional;

import co.nemo.chess.domain.board.PieceRepository;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(exclude = "locationHistory")
@Slf4j
public abstract class AbstractChessPiece implements Piece {
	private final Location location;
	private final Color color;
	private final boolean isMoved;
	private final Deque<Location> locationHistory;

	AbstractChessPiece(Location location, Color color, boolean isMoved, Deque<Location> locationHistory) {
		this.location = location;
		this.color = color;
		this.isMoved = isMoved;
		this.locationHistory = locationHistory;
	}

	@Override
	public AbstractChessPiece move(Location destination, PieceRepository repository) throws IllegalArgumentException {
		Piece target = repository.find(destination);
		if (!canAttack(target, repository)) {
			throw new IllegalArgumentException("Invalid move for " + getClass().getSimpleName());
		}
		locationHistory.push(this.location);
		return this.relocatePieces(destination, repository);
	}

	@Override
	public boolean match(Location location) {
		return this.location.equals(location);
	}

	@Override
	public boolean canAttack(Piece target, PieceRepository repository) {
		if (target instanceof AbstractChessPiece piece) {
			return this.canMove(piece.location, repository) && this.color != piece.color;
		}
		return false;
	}

	@Override
	public boolean isColorOf(Color color) {
		return this.color == color;
	}

	private AbstractChessPiece relocatePieces(Location destination, PieceRepository repository) {
		AttackType type = this.calAttackType(destination, repository);
		return switch (type) {
			case NORMAL -> this.relocateNormalPieces(destination, repository);
			case EN_PASSANT -> this.relocateEnPassant(destination, repository);
			default -> NullPiece.from(destination);
		};
	}

	private AbstractChessPiece relocateEnPassant(Location destination,
		PieceRepository repository) {
		repository.poll(this);
		Direction direction = this.calDirection(destination);
		int distance = 1;
		if ((isSameColor(Color.WHITE) && direction == Direction.UP_LEFT) ||
			(isSameColor(Color.DARK) && direction == Direction.DOWN_LEFT)) {
			Location leftLocation = this.calLocation(Direction.LEFT, distance).orElseThrow();
			repository.poll(leftLocation);
		} else if ((isSameColor(Color.WHITE) && direction == Direction.UP_RIGHT) ||
			(isSameColor(Color.DARK) && direction == Direction.DOWN_RIGHT)) {
			Location rightLocation = this.calLocation(Direction.RIGHT, distance).orElseThrow();
			repository.poll(rightLocation);
		}
		AbstractChessPiece result = this.movedPiece(destination);
		repository.add(result);
		return result;
	}

	private AbstractChessPiece relocateNormalPieces(Location destination, PieceRepository repository) {
		repository.poll(this);
		repository.poll(destination);
		AbstractChessPiece newPiece = this.movedPiece(destination);
		repository.add(newPiece);
		return newPiece;
	}

	AbstractChessPiece movedPiece(Location location) {
		return movedPiece(location, color, locationHistory);
	}

	public AbstractChessPiece withMoved() {
		return movedPiece(location, color, locationHistory);
	}

	abstract AbstractChessPiece movedPiece(Location location, Color color, Deque<Location> moveHistory);

	public AbstractChessPiece withLocationHistory(Deque<Location> locationHistory) {
		return withLocationHistory(location, color, isMoved, locationHistory);
	}

	abstract AbstractChessPiece withLocationHistory(Location location, Color color, boolean isMoved,
		Deque<Location> locationHistory);

	boolean isSameColor(Color color) {
		return this.color == color;
	}

	LocationDifference diffLocation(Location location) {
		return this.location.diff(location);
	}

	boolean isMoved() {
		return this.isMoved;
	}

	boolean isNotMoved() {
		return !this.isMoved;
	}

	Direction calDirection(Location location) {
		return this.location.calDirection(location);
	}

	Optional<Location> calLocation(Direction direction, int distance) {
		return this.location.calLocation(direction, distance);
	}

	abstract AttackType calAttackType(Location destination, PieceRepository repository);

	Optional<Location> getLastMovedLocation() {
		return Optional.ofNullable(locationHistory.peekFirst());
	}

	@Override
	public String toString() {
		String moved = isMoved ? "MOVED" : "NOT MOVED";
		return String.format("%s %s %s", moved, color, location);
	}
}
