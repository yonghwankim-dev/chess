package co.nemo.chess.domain.command;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.ChessGameReader;
import co.nemo.chess.domain.game.ChessGameWriter;
import co.nemo.chess.domain.player.Player;

public class ExitCommand extends AbstractCommand {

	private ExitCommand(CommandType type) {
		super(type);
	}

	static ExitCommand create() {
		return new ExitCommand(CommandType.EXIT);
	}

	@Override
	public boolean process(Board board, ChessGameReader gameReader, ChessGameWriter gameWriter, Player player) {
		gameWriter.printExitMessage();
		return false;
	}
}
