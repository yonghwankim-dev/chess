import java.util.List;

public class Ranks {
    private final List<List<Piece>> ranks;

    public Ranks(List<List<Piece>> ranks) {
        this.ranks = ranks;
    }

    public List<Piece> get(int row){
        return ranks.get(row - 1);
    }
}
