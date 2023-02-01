import java.util.Arrays;
import java.util.List;

public enum Direction {
    EAST(0, 1),
    WEST(0, -1),
    NORTH(-1, 0),
    SOUTH(1, 0),
    NORTHWEST(-1, -1),
    NORTHEAST(-1, 1),
    SOUTHEAST(1, 1),
    SOUTHWEST(1, -1),
    NNE(-2, 1),
    NNW(-2, -1),
    EEN(-1, 2),
    EES(1, 2),
    SSE(2, 1),
    SSW(2, -1),
    WWS(1, -2),
    WWN(-1, -2),
    INVALID(8, 8);

    private final int rankMove;
    private final int fileMove;

    Direction(int rankMove, int fileMove) {
        this.rankMove = rankMove;
        this.fileMove = fileMove;
    }



    public int getRankMove() {
        return rankMove;
    }

    public int getFileMove() {
        return fileMove;
    }

    public static List<Direction> blackPawnDirection(){
        return Arrays.asList(SOUTH, SOUTHWEST, SOUTHEAST);
    }

    public static List<Direction> whitePawnDirection(){
        return Arrays.asList(NORTH, NORTHWEST, NORTHEAST);
    }

    public static List<Direction> rookDirection(){
        return Arrays.asList(EAST, WEST, SOUTH, NORTH);
    }

    public static List<Direction> bishopDirection() {
        return Arrays.asList(NORTHWEST, NORTHEAST, SOUTHEAST, SOUTHWEST);
    }

    public static List<Direction> queenDirection() {
        return Arrays.asList(EAST, WEST, SOUTH, NORTH, NORTHWEST, NORTHEAST, SOUTHEAST, SOUTHWEST);
    }

    public static List<Direction> knightDirection() {
        return Arrays.asList(NNE, NNW, EEN, EES, SSE, SSW, WWS, WWN);
    }


    public static List<Direction> kingDirection() {
        return Arrays.asList(EAST, WEST, SOUTH, NORTH, NORTHWEST, NORTHEAST, SOUTHEAST, SOUTHWEST);
    }
}

