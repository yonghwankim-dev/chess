package co.nemo.chess.domain.piece;

public interface Piece {
	/**
	 * 체스 기물을 목적지로 이동시킨 다음에 이동시킨 기물을 반환한다
	 *
	 * @param destination 목적지
	 * @return 이동을 완료한 체스 기물
	 * @throws IllegalArgumentException 체스 기물을 목적지로 이동시키지 못하면 예외 발생
	 */
	AbstractChessPiece move(Location destination) throws IllegalArgumentException;

	boolean match(Location location);
}