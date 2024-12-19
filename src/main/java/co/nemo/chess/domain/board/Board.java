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
		Piece whiteKing = factory.whiteKing("e1");
		List<Piece> whiteBishops = Stream.of("c1", "f1")
			.map(factory::whiteBishop)
			.map(Piece.class::cast)
			.toList();
		List<Piece> whiteRooks = Stream.of("a1", "h1")
			.map(factory::whiteRook)
			.map(Piece.class::cast)
			.toList();
		// 흑폰 기물 배치
		List<Piece> darkPawns = Stream.of("a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7")
			.map(factory::darkPawn)
			.map(Piece.class::cast)
			.toList();
		Piece darkKing = factory.darkKing("e8");
		List<Piece> darkBishops = Stream.of("c8", "f8")
			.map(factory::darkBishop)
			.map(Piece.class::cast)
			.toList();
		List<Piece> darkRooks = Stream.of("a8", "h8")
			.map(factory::darkRook)
			.map(Piece.class::cast)
			.toList();

		// 기물 저장
		Stream.of(whitePawns, darkPawns)
			.flatMap(Collection::stream)
			.forEach(repository::add);
		repository.add(whiteKing);
		repository.add(darkKing);
		Stream.of(whiteBishops, darkBishops)
			.flatMap(Collection::stream)
			.forEach(repository::add);
		Stream.of(whiteRooks, darkRooks)
			.flatMap(Collection::stream)
			.forEach(repository::add);

		List<Piece> result = new ArrayList<>();
		result.addAll(whitePawns);
		result.addAll(whiteBishops);
		result.addAll(whiteRooks);
		result.add(whiteKing);
		result.addAll(darkPawns);
		result.addAll(darkBishops);
		result.addAll(darkRooks);
		result.add(darkKing);
		return result;
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
}
