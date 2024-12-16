package co.nemo.chess.domain.player;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.InputStrategy;
import co.nemo.chess.domain.game.OutputStrategy;

public class ExitCommand extends AbstractCommand {

	private ExitCommand(CommandType type) {
		super(type);
	}

	public static ExitCommand create() {
		return new ExitCommand(CommandType.EXIT);
	}

	@Override
	public void process(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy, Player player) {
		outputStrategy.print("shutdown the game");
	}
}
