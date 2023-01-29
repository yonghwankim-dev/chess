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
        Position from = lastPosition();
        Direction direction = direction(toPiece.lastPosition());
        int diffRank = from.diffRank(toPiece.lastPosition());
        int diffFile = from.diffFile(toPiece.lastPosition());
        return direction.getRankMove() == diffRank && direction.getFileMove() == diffFile;
    }

    @Override
    public char toUnicodeString() {
        return isWhite() ? (char) Integer.parseInt(String.valueOf(whitePawn)) :
                            (char) Integer.parseInt(String.valueOf(blackPawn));
    }
}
