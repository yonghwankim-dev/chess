package co.nemo.chess.domain.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import co.nemo.chess.domain.piece.AbstractChessPiece;
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
	public List<Location> findPossiblePaths(Location src) {
		List<Location> result = new ArrayList<>();
		repository.find(src).ifPresent(piece -> {
			List<Location> possibleLocations = piece.findAllMoveLocations();
			for (Location location : possibleLocations) {
				Piece target = repository.find(location).orElse(NullPiece.from(location));
				if (piece.canAttack(target, repository)) {
					result.add(location);
				}
			}
		});
		return result;
	}

	public List<Piece> setupPieces() {
		repository.clear();
		PieceFactory factory = PieceFactory.getInstance();
		// 백폰 기물 배치
		List<Piece> whitePawns = Stream.of("a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2")
			.map(factory::whitePawn)
			.map(Piece.class::cast)
			.toList();
		// 흑폰 기물 배치
		List<Piece> darkPawns = Stream.of("a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7")
			.map(factory::darkPawn)
			.map(Piece.class::cast)
			.toList();

		// 기물 저장
		Stream.of(whitePawns, darkPawns)
			.flatMap(Collection::stream)
			.forEach(repository::add);

		return Stream.of(whitePawns, darkPawns)
			.flatMap(Collection::stream)
			.toList();
	}

	public List<Piece> getAllPieces() {
		return Collections.unmodifiableList(repository.findAll());
	}
}
