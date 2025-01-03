package co.nemo.chess.domain.piece;

import java.util.Deque;
import java.util.List;
import java.util.Optional;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.board.PieceRepository;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(exclude = "locationHistory")
@Slf4j
public abstract class AbstractChessPiece implements Piece {
	private final Location location;
	private final Color color;
	private final boolean isMoved;
	private final Deque<Location> locationHistory;

	AbstractChessPiece(Location location, Color color, boolean isMoved, Deque<Location> locationHistory) {
		this.location = location;
		this.color = color;
		this.isMoved = isMoved;
		this.locationHistory = locationHistory;
	}

	@Override
	public Optional<AbstractChessPiece> move(Location destination, PieceRepository repository) {
		Piece target = repository.find(destination).orElse(NullPiece.from(destination));
		if (!this.canAttack(target, repository)) {
			return Optional.empty();
		}
		locationHistory.push(this.location);
		return Optional.ofNullable(this.relocatePieces(destination, repository));
	}

	@Override
	public Optional<AbstractChessPiece> move(AbstractChessPiece piece, PieceRepository repository) {
		return move(piece.location, repository);
	}

	@Override
	public boolean match(Location location) {
		return this.location.equals(location);
	}

	@Override
	public boolean canAttack(Piece target, List<Piece> pieces) {
		PieceRepository temporaryRepository = PieceRepository.init(pieces);
		return canAttack(target, temporaryRepository);
	}

	@Override
	public boolean canAttack(Piece target, PieceRepository repository) {
		if (target instanceof AbstractChessPiece piece) {
			return this.canMove(piece.location, repository) && this.color != piece.color;
		}
		return false;
	}

	@Override
	public boolean isColorOf(Color color) {
		return this.color == color;
	}

	@Override
	public boolean isSameColor(Piece piece) {
		return piece.isColorOf(this.color);
	}

	public boolean isWhite() {
		return this.color == Color.WHITE;
	}

	public boolean isDark() {
		return this.color == Color.DARK;
	}

	private AbstractChessPiece relocatePieces(Location destination, PieceRepository repository) {
		AttackType type = this.calAttackType(destination, repository);
		return switch (type) {
			case NORMAL -> this.relocateNormalPieces(destination, repository);
			case EN_PASSANT -> this.relocateEnPassant(destination, repository);
			default -> NullPiece.from(destination);
		};
	}

	private AbstractChessPiece relocateEnPassant(Location destination,
		PieceRepository repository) {
		repository.poll(this);
		Direction direction = this.calDirection(destination);
		int distance = 1;
		if ((isWhite() && direction == Direction.UP_LEFT) ||
			(isDark() && direction == Direction.DOWN_LEFT)) {
			Location leftLocation = this.calLocation(Direction.LEFT, distance).orElseThrow();
			repository.poll(leftLocation);
		} else if ((isWhite() && direction == Direction.UP_RIGHT) ||
			(isDark() && direction == Direction.DOWN_RIGHT)) {
			Location rightLocation = this.calLocation(Direction.RIGHT, distance).orElseThrow();
			repository.poll(rightLocation);
		}
		AbstractChessPiece result = this.movedPiece(destination);
		repository.add(result);
		return result;
	}

	private AbstractChessPiece relocateNormalPieces(Location destination, PieceRepository repository) {
		repository.poll(this);
		repository.poll(destination);
		AbstractChessPiece newPiece = this.movedPiece(destination);
		repository.add(newPiece);
		return newPiece;
	}

	AbstractChessPiece movedPiece(Location location) {
		return movedPiece(location, color, locationHistory);
	}

	public AbstractChessPiece withMoved() {
		return movedPiece(location, color, locationHistory);
	}

	abstract AbstractChessPiece movedPiece(Location location, Color color, Deque<Location> moveHistory);

	public AbstractChessPiece withLocationHistory(Deque<Location> locationHistory) {
		return withLocationHistory(location, color, isMoved, locationHistory);
	}

	protected abstract AbstractChessPiece withLocationHistory(Location location, Color color, boolean isMoved,
		Deque<Location> locationHistory);

	LocationDifference diffLocation(Location location) {
		return this.location.diff(location);
	}

	boolean isMoved() {
		return this.isMoved;
	}

	boolean isNotMoved() {
		return !this.isMoved;
	}

	Direction calDirection(Location location) {
		return this.location.calDirection(location);
	}

	Optional<Location> calLocation(Direction direction, int distance) {
		return this.location.calLocation(direction, distance);
	}

	abstract AttackType calAttackType(Location destination, PieceRepository repository);

	Optional<Location> getLastMovedLocation() {
		return Optional.ofNullable(locationHistory.peekFirst());
	}

	boolean isOnRank(Rank rank) {
		return this.location.isOnRank(rank);
	}

	@Override
	public String toString() {
		return String.format("%s %s %s", color, this.getClass().getSimpleName(), location);
	}

	AbstractChessPiece createPiece(PieceType type) {
		String position = location.toPositionText();
		return switch (type) {
			case ROOK -> PieceFactory.getInstance().rook(position, color);
			case KNIGHT -> PieceFactory.getInstance().knight(position, color);
			case BISHOP -> PieceFactory.getInstance().bishop(position, color);
			case QUEEN -> PieceFactory.getInstance().queen(position, color);
			default -> throw new IllegalArgumentException("Invalid piece type for promotion.");
		};
	}

	List<Location> calBetweenLocations(Location dst) throws IllegalStateException {
		return this.location.calBetweenLocations(dst);
	}

	boolean existPiece(Piece piece) {
		return !(piece instanceof NullPiece);
	}

	boolean existPieceUntil(Location dst, PieceRepository repository) {
		return getInterLocationsUntil(dst).stream()
			.anyMatch(repository::contains);
	}

	private List<Location> getInterLocationsUntil(Location dst) {
		return this.calBetweenLocations(dst).stream()
			.filter(loc -> !this.shouldContainDestination(loc, dst))
			.toList();
	}

	boolean shouldContainDestination(Location curLocation, Location destination) {
		return curLocation.equals(destination);
	}

	boolean emptyPieceUntil(Location dst, PieceRepository repository) {
		return getInterLocationsUntil(dst).stream()
			.noneMatch(repository::contains);
	}

	boolean isValidLocationDifference(Location location, int fileDiff, int rankDiff) {
		LocationDifference locationDifference = diffLocation(location);
		return locationDifference.isEqualDistance(fileDiff, rankDiff);
	}

	@Override
	public List<Location> findPossibleLocations(Board board) {
		return board.findPossibleLocations(location);
	}
}
