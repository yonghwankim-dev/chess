import java.util.List;

public class King extends Piece{
    private static final int whiteKing = 0x2654;
    private static final int blackKing = 0x265A;

    public King(Color color, Position position) {
        super(color, "king", 10, position);
    }

    @Override
    public List<Direction> movableDirections() {
        return Direction.kingDirection();
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
        return isWhite() ? (char) (Integer.parseInt(String.valueOf(whiteKing))) :
                (char)(Integer.parseInt(String.valueOf(blackKing)));
    }
}
