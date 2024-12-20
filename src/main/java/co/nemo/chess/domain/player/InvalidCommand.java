package co.nemo.chess.domain.player;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.ChessGameReader;
import co.nemo.chess.domain.game.ChessGameWriter;
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
	public boolean process(Board board, ChessGameReader gameReader, ChessGameWriter gameWriter, Player player) {
		gameWriter.printInvalidCommandMessage();
		return false;
	}
}
