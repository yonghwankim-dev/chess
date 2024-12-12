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
		return this.relocatePieces(this, destination, repository);
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

	AbstractChessPiece relocatePieces(AbstractChessPiece piece, Location destination,
		PieceRepository repository) {
		AttackType type = piece.calAttackType(destination, repository);
		if (type == AttackType.NORMAL) {
			return relocateNormalPieces(piece, destination, repository);
		} else if (type == AttackType.EN_PASSANT) {
			return relocateEnPassant(piece, destination, repository);
		}
		return NullPiece.from(destination);
	}

	private AbstractChessPiece relocateEnPassant(AbstractChessPiece piece, Location destination,
		PieceRepository repository) {
		repository.poll(piece);
		// 앙파상 위치 기물 제거
		// 기물이 백색이고 UP_LEFT라면 앙파상 위치는 LEFT, UP_RIGHT라면 RIGHT 반환
		Direction direction = piece.calDirection(destination);
		if (isSameColor(Color.WHITE) && direction == Direction.UP_LEFT) {
			Location leftLocation = piece.calLocation(Direction.LEFT, 1).orElseThrow();
			repository.poll(leftLocation);
		} else if (isSameColor(Color.WHITE) && direction == Direction.UP_RIGHT) {
			Location rightLocation = piece.calLocation(Direction.RIGHT, 1).orElseThrow();
			repository.poll(rightLocation);
		} else if (isSameColor(Color.DARK) && direction == Direction.DOWN_LEFT) {
			Location leftLocation = piece.calLocation(Direction.LEFT, 1).orElseThrow();
			repository.poll(leftLocation);
		} else if (isSameColor(Color.DARK) && direction == Direction.DOWN_RIGHT) {
			Location rightLocation = piece.calLocation(Direction.RIGHT, 1).orElseThrow();
			repository.poll(rightLocation);
		}
		AbstractChessPiece result = piece.movedPiece(destination);
		repository.add(result);
		return result;
	}

	abstract AbstractChessPiece relocateNormalPieces(AbstractChessPiece piece, Location destination,
		PieceRepository repository);

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
