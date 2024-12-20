package co.nemo.chess.domain.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import co.nemo.chess.domain.piece.AbstractChessPiece;
import co.nemo.chess.domain.piece.King;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.NullPiece;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.piece.PieceFactory;

public class Board implements PieceMovable {
	private final PieceRepository repository;

	private Board(PieceRepository repository) {
		this.repository = repository;
	}

	public static Board init(PieceRepository repository, Piece... pieces) {
		Arrays.stream(pieces).forEach(repository::add);
		return new Board(repository);
	}

	public static Board empty() {
		return new Board(PieceRepository.empty());
	}

	@Override
	public Optional<Piece> movePiece(Location src, Location dst) {
		return repository.find(src)
			.flatMap(srcPiece -> srcPiece.move(dst, repository))
			.map(AbstractChessPiece::withMoved);
	}

	/**
	 * 매개변수 src 위치에 있는 기물의 이동 가능한 경로를 계산한다
	 * 조건
	 * - 위치가 비어있거나 적 기물이 존재해야 함
	 * - 해당 기물이 목적지(적기물, 빈공간)로 이동 가능해야함
	 * @param src 출발 지역
	 * @return src 위치에 존재한 기물이 이동가능한 Location 리스트
	 */
	@Override
	public List<Location> findPossibleLocations(Location src) {
		List<Location> result = new ArrayList<>();
		repository.find(src).ifPresent(piece -> {
			List<Location> allMoveLocations = piece.findAllMoveLocations();
			for (Location location : allMoveLocations) {
				Piece target = repository.find(location).orElseGet(() -> NullPiece.from(location));
				if (piece.canAttack(target, repository)) {
					result.add(location);
				}
			}
		});
		return result;
	}

	public List<Piece> setupPieces() {
		repository.clear();
		List<Piece> pieces = PieceFactory.getInstance().initializedPieces();
		pieces.forEach(repository::add);
		return repository.findAll();
	}

	public List<Piece> getAllPieces() {
		return Collections.unmodifiableList(repository.findAll());
	}

	@Override
	public Optional<Piece> findPiece(Location location) {
		return repository.find(location);
	}

	public void addPiece(Piece piece) {
		repository.add(piece);
	}

	public void removePiece(Piece piece) {
		repository.poll(piece);
	}

	public boolean canMove(Piece piece, Location destination) {
		return piece.canMove(destination, repository);
	}

	/**
	 * src와 dst 사이에 다른 기물이 있는지 여부 확인
	 * src와 dst는 포함 여부에서 제외입니다.
	 * @param src 출발지
	 * @param dst 목적지
	 * @return 기물 존재 여부
	 */
	public boolean existPieceBetween(Location src, Location dst) {
		return src.calBetweenLocations(dst).stream()
			.filter(location -> !location.equals(dst))
			.map(repository::find)
			.anyMatch(Optional::isPresent);
	}

	/**
	 * 체크메이트된 기물을 반환한다
	 * @return 체크메이트된 기물
	 */
	public Optional<Piece> getCheckmatePiece() {
		return repository.findAll().stream()
			.filter(King.class::isInstance)
			.filter(king -> ((King)king).isCheckmate(repository))
			.findAny();
	}
}
