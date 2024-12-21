package co.nemo.chess.domain.piece;

import java.util.List;
import java.util.Optional;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.board.PieceRepository;
import co.nemo.chess.domain.game.ChessGameReader;
import co.nemo.chess.domain.game.ChessGameWriter;

public interface Piece {
	/**
	 * 체스 기물을 목적지로 이동시킨 다음에 이동시킨 기물을 반환한다
	 *
	 * @param destination 목적지
	 * @return 이동을 완료한 체스 기물
	 * @throws IllegalArgumentException 체스 기물을 목적지로 이동시키지 못하면 예외 발생
	 */
	Optional<AbstractChessPiece> move(Location destination, PieceRepository repository);

	Optional<AbstractChessPiece> move(AbstractChessPiece piece, PieceRepository repository);

	boolean match(Location location);

	/**
	 * 체스 기물이 조건 없이 이동 가능한 모든 경로를 반환한다
	 * @return 이동 가능한 모든 경로 리스트
	 */
	List<Location> findAllMoveLocations();

	List<Location> findPossibleLocations(Board board);

	boolean canAttack(Piece target, List<Piece> pieces);

	boolean canAttack(Piece target, PieceRepository repository);

	boolean canMove(Location location, PieceRepository repository);

	boolean isColorOf(Color color);

	boolean isSameColor(Piece piece);

	String toSymbol();

	default void handleMoveEvent(Board board, ChessGameReader gameReader, ChessGameWriter gameWriter) {

	}
}
