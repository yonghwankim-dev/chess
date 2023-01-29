import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Position implements Comparable<Position>{
    private final Rank rank;
    private final File file;

    private Position(File file, Rank rank) {
        this.rank = rank;
        this.file = file;
    }

    public static Position create(String position){
        String[] strings = position.split("");
        File file = File.valueOf(strings[0]);
        Rank rank = Rank.find(Integer.parseInt(strings[1]));
        return create(file, rank);
    }

    public static Position create(File file, Rank rank){
        return new Position(file, rank);
    }

    public int diffRank(Position to) {
        return to.rank.value() - this.rank.value();
    }

    public int diffFile(Position to) {
        return to.file.value() - this.file.value();
    }

    public boolean isVertical(Position to) {
        return this.file == to.file;
    }

    public boolean isHorizontal(Position to) {
        return this.rank == to.rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return rank == position.rank && file == position.file;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, file);
    }

    @Override
    public String toString() {
        return String.format("%s%s", file.name(), rank.value());
    }

    // 두 Position이 존재할때 정렬 기준
    // 1차 정렬기준 : file(열) 오름차순
    // 2차 정렬기준 : rank(행) 오름차순
    // 정렬결과 : 체스판의 왼쪽 상단에 있을수록 높은 값
    @Override
    public int compareTo(Position p) {
        if(this.file.value() == p.file.value()){
            return this.rank.value() - p.rank.value();
        }
        return this.file.value() - p.file.value();
    }

    public Position[] verticalBetween(Position to) {
        int start = Math.min(this.rank.value(), to.rank.value());
        int end = Math.max(this.rank.value(), to.rank.value());
        Position[] result = new Position[end - start - 1];
        int idx = 0;
        for(int i = start + 1; i < end; i++){
            result[idx++] = Position.create(file, Rank.find(i));
        }
        return result;
    }

    public Position[] horizontalBetween(Position to) {
        int start = Math.min(this.file.value(), to.file.value());
        int end = Math.max(this.file.value(), to.file.value());
        Position[] result = new Position[end - start - 1];
        int idx = 0;
        for(int i = start + 1; i < end; i++){
            result[idx++] = Position.create(File.find(i), rank);
        }
        return result;
    }

    public Position[] diagonalBetween(Position to) {
        Position start = compareTo(to) < 0 ? this : to;
        Position end = compareTo(to) >= 0 ? this : to;

        int moveRank = start.diffRank(end);
        int moveFile = start.diffFile(end);
        int linear = moveRank / moveFile > 0 ? 1 : -1;
        // linear= -1인 경우 x축 *-1, linear=1인 경우 x축 * 1
        int x_increase = linear < 0 ? linear * -1 : linear;

        int y1 = start.rank.value();
        int x1 = start.file.value();
        int y2 = end.rank.value();
        int x2 = end.file.value();

        List<Position> positions = new ArrayList<>();
        y1 += linear;
        x1 += x_increase;
        while(x1 != x2 && y1 != y2){
            positions.add(Position.create(File.find(x1), Rank.find(y1)));
            y1 += linear;
            x1 += x_increase;
        }
        return positions.toArray(new Position[0]);
    }

    public boolean isDiagonal(Position to) {
        int rankMove = Math.abs(diffRank(to));
        int fileMove = Math.abs(diffFile(to));
        return rankMove == fileMove;
    }

    public boolean equalRank(Rank rank){
        return this.rank.equals(rank);
    }

    public Position move(Direction direction){
        return Position.create(File.find(this.file.value() + direction.getFileMove()),
                Rank.find(this.rank.value() + direction.getRankMove()));
    }

    public boolean inValid() {
        return this.rank == Rank.INVALID || this.file == File.INVALID;
    }

    public boolean isFirstRank(){
        return this.rank == Rank.ONE;
    }

    public boolean isEndRank(){
        return this.rank == Rank.EIGHT;
    }
}
