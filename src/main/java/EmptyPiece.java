import java.util.Collections;
import java.util.List;

public class EmptyPiece extends Piece{
    public EmptyPiece(Color color, Position position) {
        super(color, "empty", 0, position);
    }

    @Override
    public List<Direction> movableDirections() {
        return Collections.emptyList();
    }

    @Override
    public boolean isMovableDirection(Piece toPiece) {
        return false;
    }

    @Override
    public List<Position> possiblePositions(PieceManager manager) {
        return Collections.emptyList();
    }

    @Override
    public char toUnicodeString() {
        return '.';
    }
}
