import java.util.*;

public abstract class Piece implements Comparable<Piece>{
    private final History history;
    private final Color color;
    private final String name;
    private final int score;

    public Piece(Color color, String name, int score, Position position) {
        this.history = new History();
        this.color = color;
        this.name = name;
        this.score = score;
        history.append(position);
    }

    public abstract List<Direction> movableDirections();

    public abstract boolean isMovableDirection(Piece toPiece);

    public abstract char toUnicodeString();

    public List<Position> possiblePositions(PieceManager manager){
        Position from = lastPosition();
        List<Position> result = new ArrayList<>();
        for(Direction direction : movableDirections()){
            Position target = from;
            while(!target.inValid()){
                target = target.move(direction);
                if(isMovable(manager, manager.find(target))){
                    result.add(target);
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    public boolean isMovable(PieceManager manager, Piece toPiece){
        Position from = lastPosition();
        Position to = toPiece.lastPosition();

        return isMovableDirection(toPiece) &&
                isOtherTeam(toPiece) &&
                !existPieceBetween(manager, from, to) &&
                !to.inValid();
    }

    private boolean existPieceBetween(PieceManager manager, Position from, Position to){
        return existPieceInVerticalBetween(manager, from, to) ||
                existPieceInHorizontalBetween(manager, from, to) ||
                existPieceInDiagonalBetween(manager, from, to);
    }

    Direction direction(Position to){
        int rankMove = lastPosition().diffRank(to);
        int fileMove = lastPosition().diffFile(to);
        return movableDirections().stream()
                                    .filter(direction -> direction.getRankMove() == rankMove)
                                    .filter(direction -> direction.getFileMove() == fileMove)
                                    .findFirst()
                                    .orElse(Direction.INVALID);
    }
    
    public void append(Position position) {
        history.append(position);
    }

    public Position lastPosition() {
        return history.last();
    }

    public boolean isOtherTeam(Piece p){
        return !equalColor(p.color);
    }

    public boolean equalColor(Color color){
        return this.color.equals(color);
    }

    public boolean isWhite(){
        return this.color == Color.WHITE;
    }

    public boolean isBlack(){
        return this.color == Color.BLACK;
    }

    public boolean isEmpty() {
        return this instanceof EmptyPiece;
    }

    public boolean existPieceInVerticalBetween(PieceManager pieces, Position from, Position to) {
        if(from.diffRank(to) == 0 || !from.isVertical(to)){
            return false;
        }
        Position[] positions = from.verticalBetween(to);
        return Arrays.stream(positions).anyMatch(p->existPiece(pieces, p));
    }

    public boolean existPieceInHorizontalBetween(PieceManager pieces, Position from, Position to){
        if(from.diffFile(to) == 0 || !from.isHorizontal(to)){
            return false;
        }
        Position[] positions = from.horizontalBetween(to);
        return Arrays.stream(positions).anyMatch(p->existPiece(pieces, p));
    }

    private boolean existPieceInDiagonalBetween(PieceManager pieces, Position from, Position to) {
        if(!from.isDiagonal(to)){
            return false;
        }
        Position[] positions = from.diagonalBetween(to);
        return Arrays.stream(positions).anyMatch(p->existPiece(pieces, p));
    }

    private boolean existPiece(PieceManager manager, Position position){
        return !manager.find(position).isEmpty();
    }

    public boolean possiblePawnPromotion(){
        return this instanceof Pawn &&
                (possibleWhitePawnPromotion() || possibleBlackPawnPromotion());
    }

    private boolean possibleWhitePawnPromotion(){
        return isWhite() && lastPosition().isFirstRank();
    }

    private boolean possibleBlackPawnPromotion(){
        return isBlack() && lastPosition().isEndRank();
    }

    public void swap(Piece toPiece){
        Position temp = lastPosition();
        append(toPiece.lastPosition());
        toPiece.append(temp);
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) return false;
        Piece piece = (Piece) o;
        return score == piece.score &&
                history.last().equals(piece.history.last()) &&
                color == piece.color &&
                name.equals(piece.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(history.last(), color, name, score);
    }

    @Override
    public String toString() {
        return String.format("%s-%s-%s", color.shortName(), name, lastPosition());
    }

    @Override
    public int compareTo(Piece p) {
        return this.lastPosition().compareTo(p.lastPosition());
    }

}
