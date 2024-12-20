package co.nemo.chess.domain.command;

import java.util.function.Consumer;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.ChessGameReader;
import co.nemo.chess.domain.game.ChessGameWriter;
import co.nemo.chess.domain.piece.King;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.piece.Rook;
import co.nemo.chess.domain.player.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CastlingCommand extends AbstractCommand {

	private final Location kingSrc;
	private final Location rookSrc;

	private CastlingCommand(CommandType type, Location kingSrc, Location rookSrc) {
		super(type);
		this.kingSrc = kingSrc;
		this.rookSrc = rookSrc;
	}

	static CastlingCommand create(Location kingSrc, Location rookSrc) {
		return new CastlingCommand(CommandType.CASTLING, kingSrc, rookSrc);
	}

	@Override
	public boolean process(Board board, ChessGameReader gameReader, ChessGameWriter gameWriter,
		Player player) throws
		IllegalArgumentException {
		// kingSrc와 rookSrc 위치에 있는 기물이 킹과 룩인지 검증
		Piece findKingPiece = board.findPiece(kingSrc)
			.filter(King.class::isInstance)
			.orElse(null);
		Piece findRookPiece = board.findPiece(rookSrc)
			.filter(Rook.class::isInstance)
			.orElse(null);
		validatePieceOwnership(findKingPiece, player);
		validatePieceOwnership(findRookPiece, player);

		if (isWhiteKingSideCastling(board)) {
			boolean canMoveKing = board.canMove(findKingPiece, Location.from("g1"));
			boolean canMoveRook = board.canMove(findRookPiece, Location.from("f1"));
			if (canMoveKing && canMoveRook) {
				board.movePiece(Location.from("e1"), Location.from("g1"))
					.ifPresent(logMovePiece("e1", "g1"));
				board.movePiece(Location.from("h1"), Location.from("f1"))
					.ifPresent(logMovePiece("h1", "f1"));
			}
		} else if (isWhiteQueenSideCastling(board)) {
			board.movePiece(Location.from("e1"), Location.from("c1"))
				.ifPresent(logMovePiece("e1", "c1"));
			board.movePiece(Location.from("a1"), Location.from("d1"))
				.ifPresent(logMovePiece("a1", "d1"));
		} else if (isDarkKingSideCastling(board)) {
			board.movePiece(Location.from("e8"), Location.from("g8"))
				.ifPresent(logMovePiece("e8", "g8"));
			board.movePiece(Location.from("h8"), Location.from("f8"))
				.ifPresent(logMovePiece("h8", "f8"));
		} else if (isDarkQueenSideCastling(board)) {
			board.movePiece(Location.from("e8"), Location.from("c8"))
				.ifPresent(logMovePiece("e8", "c8"));
			board.movePiece(Location.from("a8"), Location.from("d8"))
				.ifPresent(logMovePiece("a8", "d8"));
		} else {
			throw new IllegalArgumentException("invalid locations, kingSrc=" + kingSrc + " rookSrc=" + rookSrc);
		}
		return true;
	}

	private boolean isWhiteKingSideCastling(Board board) {
		Location e1 = Location.from("e1");
		Location h1 = Location.from("h1");
		// e1과 h1 기물 사이에 다른 기물들이 없어야 합니다.
		if (board.existPieceBetween(e1, h1)) {
			return false;
		}
		return kingSrc.equals(e1) && rookSrc.equals(h1);
	}

	private boolean isWhiteQueenSideCastling(Board board) {
		Location e1 = Location.from("e1");
		Location a1 = Location.from("a1");
		if (board.existPieceBetween(e1, a1)) {
			return false;
		}
		return kingSrc.equals(e1) && rookSrc.equals(a1);
	}

	private boolean isDarkKingSideCastling(Board board) {
		Location e8 = Location.from("e8");
		Location h8 = Location.from("h8");
		if (board.existPieceBetween(e8, h8)) {
			return false;
		}
		return kingSrc.equals(e8) && rookSrc.equals(h8);
	}

	private boolean isDarkQueenSideCastling(Board board) {
		Location e8 = Location.from("e8");
		Location a8 = Location.from("a8");
		if (board.existPieceBetween(e8, a8)) {
			return false;
		}
		return kingSrc.equals(e8) && rookSrc.equals(a8);
	}

	private Consumer<Piece> logMovePiece(String e1, String g1) {
		return piece -> log.info("{}, move {}->{}", piece, e1, g1);
	}

	private void validatePieceOwnership(Piece piece, Player player) throws IllegalArgumentException {
		if (piece == null) {
			throw new IllegalArgumentException(
				"No piece at the specified location. kingSrc=" + kingSrc + ", rookSrc=" + rookSrc);
		}
		if (!player.isOwnPiece(piece)) {
			throw new IllegalArgumentException(
				"It's not your piece. Please move your own piece." + " kingSrc=" + kingSrc + ", rookSrc=" + rookSrc);
		}
	}
}
