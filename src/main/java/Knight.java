import java.util.List;

public class Knight extends Piece{
    private static final int whiteKnight = 0x2658;
    private static final int blackKnight = 0x265E;

    public Knight(Color color, Position position) {
        super(color, "knight", 3, position);
    }

    @Override
    public List<Direction> movableDirections() {
        return Direction.knightDirection();
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
        return isWhite() ? (char) (Integer.parseInt(String.valueOf(whiteKnight))) :
                (char)(Integer.parseInt(String.valueOf(blackKnight)));
    }
}
