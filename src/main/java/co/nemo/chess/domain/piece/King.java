package co.nemo.chess.domain.piece;

import static co.nemo.chess.domain.piece.Direction.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import co.nemo.chess.domain.board.PieceRepository;
import lombok.EqualsAndHashCode;

// TODO: 12/19/24 체크메이트 관련 메서드 리팩토링 
@EqualsAndHashCode(callSuper = true)
public class King extends AbstractChessPiece {

	King(Location location, Color color, boolean isMoved, Deque<Location> locationHistory) {
		super(location, color, isMoved, locationHistory);
	}

	public static AbstractChessPiece notMovedWhiteKing(Location location) {
		return new King(location, Color.WHITE, false, new ArrayDeque<>());
	}

	public static AbstractChessPiece notMOvedDarkKing(Location location) {
		return new King(location, Color.DARK, false, new ArrayDeque<>());
	}

	@Override
	AbstractChessPiece movedPiece(Location location, Color color, Deque<Location> moveHistory) {
		return new King(location, color, true, moveHistory);
	}

	@Override
	protected AbstractChessPiece withLocationHistory(Location location, Color color, boolean isMoved,
		Deque<Location> locationHistory) {
		return new King(location, color, isMoved, locationHistory);
	}

	@Override
	AttackType calAttackType(Location destination, PieceRepository repository) {
		return AttackType.NORMAL;
	}

	@Override
	public List<Location> findAllMoveLocations() {
		int distance = 1;
		return Stream.of(UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT)
			.map(direction -> this.calLocation(direction, distance))
			.flatMap(Optional::stream)
			.toList();
	}

	@Override
	public boolean canMove(Location location, PieceRepository repository) {
		if (isInCheckAfterMove(location, repository)) {
			return false;
		}
		if (isCastling(location, repository)) {
			return true;
		}
		return this.calDirection(location).isEqualDistance(this, location);
	}

	private boolean isCastling(Location location, PieceRepository repository) {
		if (isMoved() || isExistPieceOnCastling(location, repository)) {
			return false;
		}

		// 현재 킹이 체크 상태라면 캐슬링 불가능
		if (isCheckedStatus(repository)) {
			return false;
		}

		if (isWhite()) {
			return location.equals(Location.from("g1")) || location.equals(Location.from("c1"));
		} else if (isDark()) {
			return location.equals(Location.from("g8")) || location.equals(Location.from("c8"));
		}
		return false;
	}

	// 룩 기물을 제외한 다른 목적지까지의 다른 기물이 있는지 검사
	private boolean isExistPieceOnCastling(Location destination, PieceRepository repository) {
		return this.calBetweenLocations(destination).stream()
			.filter(loc -> !loc.equals(destination))
			.anyMatch(loc -> repository.find(loc)
				.filter(piece -> !(piece instanceof Rook))
				.filter(this::existPiece)
				.isPresent()
			);
	}

	/**
	 * 킹 기물이 목적지(destination)으로 이동한 후에 체크 상태가 되는지 여부 검사
	 * @param destination 킹 기물의 목적지
	 * @param repository 기물 저장소
	 * @return 체크 여부
	 */
	private boolean isInCheckAfterMove(Location destination, PieceRepository repository) {
		Piece king = super.movedPiece(destination);
		List<Piece> pieces = repository.findAll();
		pieces.remove(this);
		pieces.add(king);

		return pieces.stream()
			.filter(piece -> !(piece instanceof King))
			.filter(piece -> !piece.isSameColor(this))
			.anyMatch(piece -> piece.canAttack(king, pieces));
	}

	public boolean isCheckedStatus(PieceRepository repository) {
		// 킹을 제외한 다른 기물들이 공격이 가능하면 체크 상태
		Color color = this.isWhite() ? Color.WHITE : Color.DARK;
		return repository.findAll().stream()
			.filter(piece -> !piece.equals(this)) // 자신 제외
			.filter(piece -> !piece.isColorOf(color)) // 반대 색상
			.anyMatch(piece -> piece.canAttack(this, repository));
	}

	public boolean isCheckmate(PieceRepository repository) {
		// 킹이 체크 상태인지 확인
		if (!isCheckedStatus(repository)) {
			return false;
		}
		// 킹의 이동 가능 여부 확인
		for (Location location : findAllMoveLocations()) {
			if (!isInCheckAfterMove(location, repository)) {
				return false;
			}
		}
		// 다른 기물로 체크를 막을 수 있는지 확인
		if (canBlockCheck(repository) || canCaptureAttackingPiece(repository)) {
			return false;
		}
		return true;
	}

	private boolean canBlockCheck(PieceRepository repository) {
		Color curColor = isWhite() ? Color.WHITE : Color.DARK;
		List<Piece> pieces = repository.findAll().stream()
			.filter(piece -> !piece.equals(this))
			.filter(piece -> piece.isColorOf(curColor))
			.toList();
		// 체크를 막을 수 있는 기물이 있는지 확인
		for (Piece piece : pieces) {
			// 체크를 막을 수 있는지 확인하는 로직
			if (canBlockWith(piece, repository)) {
				return true;
			}
		}
		return false;
	}

	private boolean canBlockWith(Piece blockingPiece, PieceRepository repository) {
		// blockingPiece가 킹의 모든 이동 가능한 위치로 이동했을 때 체크 상태가 하나라도 아니게 되면 true를 반환한다. 그 외는 false
		List<Piece> pieces = repository.findAll();
		for (Location location : findAllMoveLocations()) {
			PieceRepository tempRepository = PieceRepository.init(pieces);
			blockingPiece.move(location, tempRepository);
			if (!isCheckedStatus(tempRepository)) {
				return true;
			}
		}
		return false;
	}

	private boolean canCaptureAttackingPiece(PieceRepository repository) {
		Color curColor = isWhite() ? Color.WHITE : Color.DARK;
		List<Piece> pieces = repository.findAll().stream()
			.filter(piece -> !piece.equals(this))
			.filter(piece -> piece.isColorOf(curColor))
			.toList();
		// 킹을 제외한 아군 기물들 중에서 적 기물을 잡아서 체크 상태가 아니게 하면 true 반환
		for (Piece piece : pieces) {
			if (canAttackWith(piece, repository)) {
				return true;
			}
		}
		return false;
	}

	private boolean canAttackWith(Piece piece, PieceRepository repository) {
		List<Piece> pieces = repository.findAll();
		Color pieceColor = piece.isColorOf(Color.WHITE) ? Color.WHITE : Color.DARK;
		// 공격 가능한 적 기물들을 대상으로 이동했을때 체크 상태가 아니면 true 반환
		List<AbstractChessPiece> enemyPieces = repository.findAll().stream()
			.filter(p -> !p.isColorOf(pieceColor))
			.map(AbstractChessPiece.class::cast)
			.toList();
		for (AbstractChessPiece enemyPiece : enemyPieces) {
			PieceRepository tempRepository = PieceRepository.init(pieces);
			if (piece.canAttack(enemyPiece, tempRepository)) {
				piece.move(enemyPiece, tempRepository);
				if (!isCheckedStatus(tempRepository)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toSymbol() {
		return isWhite() ? "♔" : "♚";
	}
}
