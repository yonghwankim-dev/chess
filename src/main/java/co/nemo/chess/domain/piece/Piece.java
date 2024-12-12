package co.nemo.chess.domain.piece;

import java.util.List;

import co.nemo.chess.domain.board.PieceRepository;

public interface Piece {
	/**
	 * 체스 기물을 목적지로 이동시킨 다음에 이동시킨 기물을 반환한다
	 *
	 * @param destination 목적지
	 * @return 이동을 완료한 체스 기물
	 * @throws IllegalArgumentException 체스 기물을 목적지로 이동시키지 못하면 예외 발생
	 */
	AbstractChessPiece move(Location destination, PieceRepository repository) throws IllegalArgumentException;

	boolean match(Location location);

	/**
	 * 체스 기물이 조건 없이 이동 가능한 모든 경로를 반환한다
	 * @return 이동 가능한 모든 경로 리스트
	 */
	List<Location> findAllMoveLocations();

	boolean canAttack(Piece target, PieceRepository repository);

	boolean canMove(Location location, PieceRepository repository);

	boolean isColorOf(Color color);

	default boolean isInitialTwoForward() {
		return false;
	}
}
