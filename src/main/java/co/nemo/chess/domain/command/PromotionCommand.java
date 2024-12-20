package co.nemo.chess.domain.command;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.ChessGameReader;
import co.nemo.chess.domain.game.ChessGameWriter;
import co.nemo.chess.domain.piece.Pawn;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.piece.PieceType;
import co.nemo.chess.domain.player.Player;

public class PromotionCommand extends AbstractCommand {
	private final Pawn pawn;
	private final PieceType promotionPieceType;

	private PromotionCommand(CommandType type, Pawn pawn, PieceType promotionPieceType) {
		super(type);
		this.pawn = pawn;
		this.promotionPieceType = promotionPieceType;
	}

	static AbstractCommand create(Piece piece, PieceType pieceType) {
		if (piece instanceof Pawn pawn) {
			return new PromotionCommand(CommandType.PROMOTION, pawn, pieceType);
		}
		return AbstractCommand.invalidCommand();
	}

	@Override
	public boolean process(Board board, ChessGameReader gameReader, ChessGameWriter gameWriter,
		Player player) throws
		IllegalArgumentException {
		Piece promoPiece;
		try {
			promoPiece = pawn.promoTo(promotionPieceType);
		} catch (IllegalStateException e) {
			return false;
		}
		board.removePiece(pawn);
		board.addPiece(promoPiece);
		return true;
	}
}
