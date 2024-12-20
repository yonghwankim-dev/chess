package co.nemo.chess.domain.piece;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PieceFactory {

	private PieceFactory() {

	}

	private static class PieceFactoryHelper {

		private static final PieceFactory INSTANCE = new PieceFactory();

	}

	public static PieceFactory getInstance() {
		return PieceFactoryHelper.INSTANCE;
	}

	public AbstractChessPiece pawn(String position, Color color) {
		return color == Color.WHITE ? whitePawn(position) : darkPawn(position);
	}

	public AbstractChessPiece whitePawn(String position) {
		return Pawn.notMovedWhitePawn(Location.from(position));
	}

	public AbstractChessPiece darkPawn(String position) {
		return Pawn.notMovedDarkPawn(Location.from(position));
	}

	public AbstractChessPiece rook(String position, Color color) {
		return color == Color.WHITE ? whiteRook(position) : darkRook(position);
	}

	public AbstractChessPiece whiteRook(String position) {
		return Rook.notMovedWhiteRook(Location.from(position));
	}

	public AbstractChessPiece darkRook(String position) {
		return Rook.notMovedDarkRook(Location.from(position));
	}

	public AbstractChessPiece king(String position, Color color) {
		return color == Color.WHITE ? whiteKing(position) : darkKing(position);
	}

	public AbstractChessPiece whiteKing(String position) {
		return King.notMovedWhiteKing(Location.from(position));
	}

	public AbstractChessPiece darkKing(String position) {
		return King.notMOvedDarkKing(Location.from(position));
	}

	public AbstractChessPiece bishop(String position, Color color) {
		return color == Color.WHITE ? whiteBishop(position) : darkBishop(position);
	}

	public AbstractChessPiece whiteBishop(String position) {
		return Bishop.notMovedWhiteBishop(Location.from(position));
	}

	public AbstractChessPiece darkBishop(String position) {
		return Bishop.notMovedDarkBishop(Location.from(position));
	}

	public AbstractChessPiece queen(String position, Color color) {
		return color == Color.WHITE ? whiteQueen(position) : darkQueen(position);
	}

	public AbstractChessPiece whiteQueen(String position) {
		return Queen.notMovedWhiteQueen(Location.from(position));
	}

	public AbstractChessPiece darkQueen(String position) {
		return Queen.notMovedDarkQueen(Location.from(position));
	}

	public AbstractChessPiece knight(String position, Color color) {
		return color == Color.WHITE ? whiteKnight(position) : darkKnight(position);
	}

	public AbstractChessPiece whiteKnight(String position) {
		return Knight.notMovedWhiteKnight(Location.from(position));
	}

	public AbstractChessPiece darkKnight(String position) {
		return Knight.notMovedDarkKnight(Location.from(position));
	}

	/**
	 * 체스판의 처음 배치된 기물들을 생성하고 반환
	 * @return 기물 리스트
	 */
	public List<Piece> initializedPieces() {
		// 폰 생성
		List<Piece> whitePawns = Stream.of("a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2")
			.map(this::whitePawn)
			.map(Piece.class::cast)
			.toList();
		List<Piece> darkPawns = Stream.of("a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7")
			.map(this::darkPawn)
			.map(Piece.class::cast)
			.toList();

		// 룩 생성
		List<Piece> whiteRooks = Stream.of("a1", "h1")
			.map(this::whiteRook)
			.map(Piece.class::cast)
			.toList();
		List<Piece> darkRooks = Stream.of("a8", "h8")
			.map(this::darkRook)
			.map(Piece.class::cast)
			.toList();

		// 나이트 생성
		List<Piece> whiteKnights = Stream.of("b1", "g1")
			.map(this::whiteKnight)
			.map(Piece.class::cast)
			.toList();
		List<Piece> darkKnights = Stream.of("b8", "g8")
			.map(this::darkKnight)
			.map(Piece.class::cast)
			.toList();

		// 비숍
		List<Piece> whiteBishops = Stream.of("c1", "f1")
			.map(this::whiteBishop)
			.map(Piece.class::cast)
			.toList();
		List<Piece> darkBishops = Stream.of("c8", "f8")
			.map(this::darkBishop)
			.map(Piece.class::cast)
			.toList();

		// 퀸 생성
		Piece whiteQueen = this.whiteQueen("d1");
		Piece darkQueen = this.darkQueen("d8");

		// 킹 생성
		Piece whiteKing = this.whiteKing("e1");
		Piece darkKing = this.darkKing("e8");

		List<Piece> result = new ArrayList<>();
		result.addAll(whitePawns);
		result.addAll(darkPawns);
		result.addAll(whiteRooks);
		result.addAll(darkRooks);
		result.addAll(whiteKnights);
		result.addAll(darkKnights);
		result.addAll(whiteBishops);
		result.addAll(darkBishops);
		result.add(whiteQueen);
		result.add(darkQueen);
		result.add(whiteKing);
		result.add(darkKing);
		return result;
	}
}
