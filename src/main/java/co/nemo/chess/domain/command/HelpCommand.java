package co.nemo.chess.domain.command;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.ChessGameReader;
import co.nemo.chess.domain.game.ChessGameWriter;
import co.nemo.chess.domain.player.Player;

public class HelpCommand extends AbstractCommand {

	private HelpCommand(CommandType type) {
		super(type);
	}

	static AbstractCommand create() {
		return new HelpCommand(CommandType.HELP);
	}

	@Override
	public boolean process(Board board, ChessGameReader gameReader, ChessGameWriter gameWriter, Player player) {
		gameWriter.printHelpMessage();
		return false;
	}
}
