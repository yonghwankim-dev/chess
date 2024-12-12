package co.nemo.chess.domain.piece;

import java.util.Collections;
import java.util.List;

import co.nemo.chess.domain.board.PieceRepository;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class NullPiece implements Piece {

	private final Location location;

	private NullPiece(Location location) {
		this.location = location;
	}

	public static NullPiece from(Location location) {
		return new NullPiece(location);
	}

	@Override
	public AbstractChessPiece move(Location destination, PieceRepository repository) {
		throw new UnsupportedOperationException("NullPiece cannot be moved");
	}

	@Override
	public boolean match(Location location) {
		return this.location.equals(location);
	}

	@Override
	public List<Location> findPossibleLocations() {
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
	public boolean isColorOf(Color color) {
		return false;
	}
}
