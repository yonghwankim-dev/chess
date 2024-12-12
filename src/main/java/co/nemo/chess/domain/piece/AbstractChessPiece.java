package co.nemo.chess.domain.piece;

import java.util.Optional;

import co.nemo.chess.domain.board.PieceRepository;
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

	@Override
	public AbstractChessPiece move(Location destination, PieceRepository repository) throws IllegalArgumentException {
		Piece target = repository.find(destination);
		if (!canAttack(target, repository)) {
			throw new IllegalArgumentException("Invalid move for " + getClass().getSimpleName());
		}
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
		if (type == AttackType.NORMAL) {
			return this.relocateNormalPieces(destination, repository);
		} else if (type == AttackType.EN_PASSANT) {
			return this.relocateEnPassant(destination, repository);
		}
		return NullPiece.from(destination);
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
		return movedPiece(location, color);
	}

	abstract AbstractChessPiece movedPiece(Location location, Color color);

	public AbstractChessPiece withMoved() {
		return movedPiece(location, color);
	}

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

	@Override
	public String toString() {
		String moved = isMoved ? "MOVED" : "NOT MOVED";
		return String.format("%s %s %s", moved, color, location);
	}

	protected abstract AttackType calAttackType(Location destination, PieceRepository repository);

}
