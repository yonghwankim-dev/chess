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
	
	/**
	 * 명령어를 실행한다
	 * 반환값으로 true를 반환하면 턴을 변경해야 함을 의미하고, false를 반환하면 턴을 변경하지 말아야 한다는 의미
	 *
	 * @param board the board
	 * @param inputStrategy the input strategy
	 * @param outputStrategy the output strategy
	 * @param player the player
	 * @return the boolean
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public abstract boolean process(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy,
		Player player) throws
		IllegalArgumentException;

	public boolean isExistCommand() {
		return this instanceof ExitCommand;
	}

	@Override
	public String toString() {
		return String.format("%s 명령어", type);
	}
}
