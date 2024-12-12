package co.nemo.chess.domain.piece;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import co.nemo.chess.domain.board.PieceRepository;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class NullPiece extends AbstractChessPiece {

	public NullPiece(Location location, Color color, boolean isMoved, Deque<Location> locationHistory) {
		super(location, color, isMoved, locationHistory);
	}

	public static NullPiece from(Location location) {
		return new NullPiece(location, Color.NONE, false, new ArrayDeque<>());
	}

	@Override
	public AbstractChessPiece move(Location destination, PieceRepository repository) {
		throw new UnsupportedOperationException("NullPiece cannot be moved");
	}

	@Override
	public List<Location> findAllMoveLocations() {
		return Collections.emptyList();
	}

	@Override
	public boolean canAttack(Piece target, PieceRepository repository) {
		return false;
	}

	@Override
	public boolean canMove(Location location, PieceRepository repository) {
		return false;
	}

	@Override
	AbstractChessPiece movedPiece(Location location, Color color, Deque<Location> moveHistory) {
		throw new UnsupportedOperationException("NullPiece cannot be moved");
	}

	@Override
	protected AttackType calAttackType(Location destination, PieceRepository repository) {
		return AttackType.NONE;
	}

	@Override
	AbstractChessPiece withLocationHistory(Location location, Color color, boolean isMoved,
		Deque<Location> locationHistory) {
		return new NullPiece(location, color, isMoved, locationHistory);
	}
}
