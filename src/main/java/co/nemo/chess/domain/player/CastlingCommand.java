package co.nemo.chess.domain.player;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.InputStrategy;
import co.nemo.chess.domain.game.OutputStrategy;
import co.nemo.chess.domain.piece.King;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.piece.Rook;
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

	public static CastlingCommand create(Location kingSrc, Location rookSrc) {
		return new CastlingCommand(CommandType.CASTLING, kingSrc, rookSrc);
	}

	@Override
	public void process(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy, Player player) throws
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

		if (kingSrc.equals(Location.from("e1")) && rookSrc.equals(Location.from("h1"))) {
			// 캐슬링 타입이 킹사이드 캐슬링인 경우
			board.movePiece(Location.from("e1"), Location.from("g1"))
				.ifPresent(piece -> log.info("{}, move {}->{}", piece, "e1", "g1"));
			board.movePiece(Location.from("h1"), Location.from("f1"))
				.ifPresent(piece -> log.info("{}, move {}->{}", piece, "h1", "f1"));
		} else if (kingSrc.equals(Location.from("e1")) && rookSrc.equals(Location.from("a1"))) {
			// 캐슬링 타입이 퀸사이드 캐슬링인 경우
			board.movePiece(Location.from("e1"), Location.from("c1"))
				.ifPresent(piece -> log.info("{}, move {}->{}", piece, "e1", "c1"));
			board.movePiece(Location.from("a1"), Location.from("d1"))
				.ifPresent(piece -> log.info("{}, move {}->{}", piece, "a1", "d1"));
		} else if (kingSrc.equals(Location.from("e8")) && rookSrc.equals(Location.from("h8"))) {
			// 캐슬링 타입이 흑색의 킹 사이드 캐슬링인 경우
			board.movePiece(Location.from("e8"), Location.from("g8"))
				.ifPresent(piece -> log.info("{}, move {}->{}", piece, "e8", "g8"));
			board.movePiece(Location.from("h8"), Location.from("f8"))
				.ifPresent(piece -> log.info("{}, move {}->{}", piece, "h8", "f8"));
		} else if (kingSrc.equals(Location.from("e8")) && rookSrc.equals(Location.from("a8"))) {
			// 캐슬링 타입이 흑색의 퀸 사이드 캐슬링인 경우
			board.movePiece(Location.from("e8"), Location.from("c8"))
				.ifPresent(piece -> log.info("{}, move {}->{}", piece, "e8", "c8"));
			board.movePiece(Location.from("a8"), Location.from("d8"))
				.ifPresent(piece -> log.info("{}, move {}->{}", piece, "a8", "d8"));
		} else {
			throw new IllegalArgumentException("invalid locations, kingSrc=" + kingSrc + " rookSrc=" + rookSrc);
		}
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
