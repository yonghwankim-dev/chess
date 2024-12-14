package co.nemo.chess.domain.player;

import java.util.List;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.OutputStrategy;
import co.nemo.chess.domain.piece.Location;

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
	public void process(Board board) {
		List<Location> possibleLocations = board.findPossiblePaths(src);
	}

	@Override
	public void process(OutputStrategy outputStrategy) {

	}
}
