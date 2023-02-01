import java.util.List;

public class Pawn extends Piece{
    private static final int whitePawn = 0x2659;
    private static final int blackPawn = 0x265F;

    public Pawn(Color color, Position position) {
        super(color,"pawn", 1, position);
    }

    @Override
    public List<Direction> movableDirections() {
        return isWhite() ? Direction.whitePawnDirection() : Direction.blackPawnDirection();
    }

    @Override
    public boolean isMovableDirection(Piece toPiece) {
        return lastPosition().isDiagonal(toPiece.lastPosition()) ?
                isMovableDiagonal(toPiece) && equalPosition(toPiece) :
                equalPosition(toPiece);
    }

    private boolean isMovableDiagonal(Piece toPiece){
        return lastPosition().isDiagonal(toPiece.lastPosition()) && !toPiece.isEmpty();
    }

    private boolean equalPosition(Piece toPiece){
        Direction direction = direction(toPiece.lastPosition());
        int diffRank = lastPosition().diffRank(toPiece.lastPosition());
        int diffFile = lastPosition().diffFile(toPiece.lastPosition());
        return direction.getRankMove() == diffRank && direction.getFileMove() == diffFile;
    }

    @Override
    public char toUnicodeString() {
        return isWhite() ? (char) Integer.parseInt(String.valueOf(whitePawn)) :
                            (char) Integer.parseInt(String.valueOf(blackPawn));
    }
}
