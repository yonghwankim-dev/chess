package co.nemo.chess.domain.player;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.InputStrategy;
import co.nemo.chess.domain.game.OutputStrategy;
import co.nemo.chess.domain.piece.Location;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class AbstractCommand {
	private final CommandType type;

	AbstractCommand(CommandType type) {
		this.type = type;
	}

	public static AbstractCommand moveCommand(Location src, Location dst) {
		return MoveCommand.of(src, dst);
	}

	public static AbstractCommand locationsCommand(Location src) {
		return LocationsCommand.from(src);
	}

	public static AbstractCommand exitCommand() {
		return ExitCommand.create();
	}

	public static AbstractCommand invalidCommand() {
		return InvalidCommand.create();
	}

	public static AbstractCommand helpCommand() {
		return HelpCommand.create();
	}

	public static AbstractCommand castlingCommand(Location kingSrc, Location rookSrc) {
		return CastlingCommand.create(kingSrc, rookSrc);
	}

	public boolean isTypeOf(CommandType type) {
		return this.type == type;
	}

	public abstract void process(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy,
		Player player) throws
		IllegalArgumentException;

	@Override
	public String toString() {
		return String.format("%s 명령어", type);
	}
}
