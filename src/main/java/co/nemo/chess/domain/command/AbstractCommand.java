package co.nemo.chess.domain.command;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.ChessGameReader;
import co.nemo.chess.domain.game.ChessGameWriter;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.piece.PieceType;
import co.nemo.chess.domain.player.Player;
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

	public static AbstractCommand promotionCommand(Piece piece, PieceType pieceType) {
		return PromotionCommand.create(piece, pieceType);
	}

	/**
	 * 명령어를 실행한다
	 * 반환값으로 true를 반환하면 턴을 변경해야 함을 의미하고, false를 반환하면 턴을 변경하지 말아야 한다는 의미
	 *
	 * @param inputStrategy the input strategy
	 * @param board         the board
	 * @param gameWriter    the output strategy
	 * @param player        the player
	 * @return the boolean
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public abstract boolean process(Board board, ChessGameReader reader, ChessGameWriter gameWriter,
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
