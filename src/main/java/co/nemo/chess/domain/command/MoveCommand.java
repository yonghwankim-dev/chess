package co.nemo.chess.domain.command;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.ChessGameReader;
import co.nemo.chess.domain.game.ChessGameWriter;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.player.Player;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Slf4j
public class MoveCommand extends AbstractCommand {

	private final Location src;
	private final Location dst;

	private MoveCommand(CommandType type, Location src, Location dst) {
		super(type);
		this.src = src;
		this.dst = dst;
	}

	static MoveCommand of(Location src, Location dst) {
		return new MoveCommand(CommandType.MOVE, src, dst);
	}

	@Override
	public boolean process(Board board, ChessGameReader gameReader, ChessGameWriter gameWriter,
		Player player) throws
		IllegalArgumentException {
		Piece findPiece = board.findPiece(src).orElse(null);
		validatePieceOwnership(findPiece, player);
		board.movePiece(src, dst).ifPresent(piece -> {
			log.info("{}, move {}->{}", piece, src, dst);
			piece.handleMoveEvent(board, gameReader, gameWriter);
		});
		return true;
	}

	private void validatePieceOwnership(Piece piece, Player player) throws IllegalArgumentException {
		if (piece == null) {
			throw new IllegalArgumentException("No piece at the specified location. src=" + src + ", dst=" + dst);
		}
		if (!player.isOwnPiece(piece)) {
			throw new IllegalArgumentException(
				"It's not your piece. Please move your own piece." + " src=" + src + ", dst=" + dst);
		}
	}

	@Override
	public String toString() {
		return super.toString() + String.format("src=%s, dst=%s", src, dst);
	}
}
