import java.util.List;

public class Bishop extends Piece{
    private static final int whiteBishop = 0x2657;
    private static final int blackBishop = 0x265D;

    public Bishop(Color color, Position position) {
        super(color, "bishop", 3, position);
    }

    @Override
    public List<Direction> movableDirections() {
        // 사용하지 않음
        return Direction.bishopDirection();
    }

    @Override
    public boolean isMovableDirection(Piece toPiece) {
        return lastPosition().isDiagonal(toPiece.lastPosition());
    }

    @Override
    public char toUnicodeString() {
        return isWhite() ? (char) (Integer.parseInt(String.valueOf(whiteBishop))) :
                (char)(Integer.parseInt(String.valueOf(blackBishop)));
    }
}
