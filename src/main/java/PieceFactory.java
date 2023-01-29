public class PieceFactory {
    public static Piece pawn(Color color, Position position){
        return new Pawn(color, position);
    }

    public static Piece rook(Color color, Position position) {
        return new Rook(color, position);
    }

    public static Piece bishop(Color color, Position position) {
        return new Bishop(color, position);
    }

    public static Piece queen(Piece piece){
        if(piece.isWhite()){
            return queen(Color.WHITE, piece.lastPosition());
        }
        return queen(Color.BLACK, piece.lastPosition());
    }

    public static Piece queen(Color color, Position position) {
        return new Queen(color, position);
    }

    public static Piece knight(Color color, Position position) {
        return new Knight(color, position);
    }

    public static Piece king(Color color, Position position){
        return new King(color, position);
    }

    public static Piece empty(Position position) {
        return new EmptyPiece(Color.EMPTY, position);
    }
}
