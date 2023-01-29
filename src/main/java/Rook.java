import java.util.List;

public class Rook extends Piece{
    private static final int whiteRook = 0x2656;
    private static final int blackRook = 0x265C;
    public Rook(Color color, Position position) {
        super(color, "rook", 5, position);
    }

    @Override
    public List<Direction> movableDirections() {
        return Direction.rookDirection();
    }

    @Override
    public boolean isMovableDirection(Piece toPiece) {
        return lastPosition().isVertical(toPiece.lastPosition()) || lastPosition().isHorizontal(toPiece.lastPosition());
    }

    @Override
    public char toUnicodeString() {
        return isWhite() ? (char) (Integer.parseInt(String.valueOf(whiteRook))) :
                (char)(Integer.parseInt(String.valueOf(blackRook)));
    }
}
