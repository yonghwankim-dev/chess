package co.nemo.chess.domain.player;

import java.util.List;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.ChessGameReader;
import co.nemo.chess.domain.game.ChessGameWriter;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;

public class LocationsCommand extends AbstractCommand {
	private final Location src;

	private LocationsCommand(CommandType type, Location src) {
		super(type);
		this.src = src;
	}

	public static LocationsCommand from(Location src) {
		return new LocationsCommand(CommandType.LOCATIONS, src);
	}

	@Override
	public boolean process(Board board, ChessGameReader gameReader, ChessGameWriter gameWriter, Player player) {
		Piece findPiece = board.findPiece(src).orElse(null);
		validatePieceOwnership(findPiece, player);
		List<Location> possibleLocations = board.findPossibleLocations(src);
		gameWriter.printLocations(possibleLocations);
		return false;
	}

	private void validatePieceOwnership(Piece piece, Player player) {
		if (piece == null) {
			throw new IllegalArgumentException("No piece at the specified location. src=" + src);
		}
		if (!player.isOwnPiece(piece)) {
			throw new IllegalArgumentException("It's not your piece. Please choice your own piece." + " src=" + src);
		}
	}
}
