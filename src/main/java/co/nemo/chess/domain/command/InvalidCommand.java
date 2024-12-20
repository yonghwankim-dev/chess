package co.nemo.chess.domain.command;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.ChessGameReader;
import co.nemo.chess.domain.game.ChessGameWriter;
import co.nemo.chess.domain.player.Player;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class InvalidCommand extends AbstractCommand {
	private InvalidCommand(CommandType type) {
		super(type);
	}

	static InvalidCommand create() {
		return new InvalidCommand(CommandType.NONE);
	}

	@Override
	public boolean process(Board board, ChessGameReader gameReader, ChessGameWriter gameWriter, Player player) {
		gameWriter.printInvalidCommandMessage();
		return false;
	}
}
