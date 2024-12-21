package co.nemo.chess.domain.command;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.ChessGameReader;
import co.nemo.chess.domain.game.ChessGameWriter;
import co.nemo.chess.domain.player.Player;

public class ResignCommand extends AbstractCommand {
	private ResignCommand(CommandType type) {
		super(type);
	}

	public static AbstractCommand create() {
		return new ResignCommand(CommandType.RESIGN);
	}

	@Override
	public boolean process(Board board, ChessGameReader reader, ChessGameWriter gameWriter, Player player) throws
		IllegalArgumentException {
		gameWriter.printResignMessage(player);
		if (player.equals(Player.white())) {
			gameWriter.printWinner(Player.dark());
		} else {
			gameWriter.printWinner(Player.dark());
		}
		return false;
	}
}
