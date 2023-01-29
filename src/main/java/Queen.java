import java.util.List;

public class Queen extends Piece{
    private static final int whiteQueen = 0x2655;
    private static final int blackQueen = 0x265B;

    public Queen(Color color, Position position) {
        super(color, "queen", 9, position);
    }

    @Override
    public List<Direction> movableDirections() {
        // 사용안함
        return Direction.queenDirection();
    }

    @Override
    public boolean isMovableDirection(Piece toPiece) {
        return (lastPosition().isHorizontal(toPiece.lastPosition()) ||
                lastPosition().isVertical(toPiece.lastPosition()) ||
                lastPosition().isDiagonal(toPiece.lastPosition()));
    }

    @Override
    public char toUnicodeString() {
        return isWhite() ? (char) (Integer.parseInt(String.valueOf(whiteQueen))) :
                (char)(Integer.parseInt(String.valueOf(blackQueen)));
    }
}
