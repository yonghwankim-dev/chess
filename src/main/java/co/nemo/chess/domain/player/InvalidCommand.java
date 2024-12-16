package co.nemo.chess.domain.player;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.InputStrategy;
import co.nemo.chess.domain.game.OutputStrategy;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class InvalidCommand extends AbstractCommand {
	private InvalidCommand(CommandType type) {
		super(type);
	}

	public static InvalidCommand create() {
		return new InvalidCommand(CommandType.NONE);
	}

	@Override
	public void process(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy, Player player) {
		outputStrategy.print("invalid command.");
	}
}
