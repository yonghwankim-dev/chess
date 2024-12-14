package co.nemo.chess.domain.board;

import java.util.List;
import java.util.Optional;

import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;

public interface PieceMovable {
	Optional<Piece> movePiece(Location src, Location dst);

	List<Location> findPossiblePaths(Location src);

	Optional<Piece> findPiece(Location src);
}
