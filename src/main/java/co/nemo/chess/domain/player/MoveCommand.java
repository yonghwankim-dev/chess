package co.nemo.chess.domain.player;

import org.apache.logging.log4j.util.Strings;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.InputStrategy;
import co.nemo.chess.domain.game.OutputStrategy;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Pawn;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.piece.PieceType;
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

	public static MoveCommand of(Location src, Location dst) {
		return new MoveCommand(CommandType.MOVE, src, dst);
	}

	@Override
	public void process(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy, Player player) throws
		IllegalArgumentException {
		Piece findPiece = board.findPiece(src).orElse(null);
		validatePieceOwnership(findPiece, player);
		board.movePiece(src, dst).ifPresent(piece -> {
			log.info("{}, move {}->{}", piece, src, dst);
			// 각 기물별 이벤트 체크
			if (piece instanceof Pawn pawn && pawn.canPromote()) {
				outputStrategy.println(
					"Your phone has reached the end of your opponent's camp! Promote with any craft you want.");
				outputStrategy.println("Possible options: Queen, Rook, Bishop, Knight (Case free)");
				outputStrategy.println(
					"Type: Please enter the first letter of the object in capital letters (e.g., Queen, Rook, Bishop, Knight).");
				PieceType type;
				while (true) {
					try {
						String promotionText = inputStrategy.readLine().orElse(Strings.EMPTY);
						type = PieceType.valueOfText(promotionText);
						break;
					} catch (IllegalArgumentException e) {
						outputStrategy.println("Invalid input, possible options: Queen, Rook, Bishop, Knight");
					}
				}
				board.removePiece(pawn);
				Piece promoPiece = pawn.promoTo(type);
				board.addPiece(promoPiece);
			}
		});
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
