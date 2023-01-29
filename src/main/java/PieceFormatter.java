import java.util.List;

public class PieceFormatter {
    public static char[] convertPieceToUnicode(List<Piece> pieces){
        char[] result = new char[pieces.size()];
        int idx = 0;
        for(Piece piece : pieces){
            result[idx++] = piece.toUnicodeString();
        }
        return result;
    }
}
