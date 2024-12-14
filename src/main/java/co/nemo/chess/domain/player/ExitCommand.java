package co.nemo.chess.domain.player;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.OutputStrategy;

public class ExitCommand extends AbstractCommand {

	private ExitCommand(CommandType type) {
		super(type);
	}

	public static ExitCommand create() {
		return new ExitCommand(CommandType.EXIT);
	}

	@Override
	public void process(Board board) {

	}

	@Override
	public void process(OutputStrategy outputStrategy) {
		outputStrategy.print("shutdown the game");
	}
}
