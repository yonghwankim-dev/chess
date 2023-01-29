import java.util.List;
import java.util.stream.Collectors;

public class Pieces {
    private final PieceManager manager;

    public Pieces() {
        this.manager = new PieceManager();
    }

    public void init(){
        manager.clear();
        manager.init();
    }

    public boolean move(Position from, Position to) {
        return manager.move(from, to);
    }

    public List<Position> possiblePositions(Position from) {
        return find(from).possiblePositions(manager);
    }

    public List<List<Piece>> display(){
        return Rank.list()
                    .stream()
                    .map(manager::display)
                    .collect(Collectors.toList());
    }

    public Piece find(Position from) {
        return manager.find(from);
    }

    public boolean hasKing(Color color) {
        return manager.hasKing(color);
    }

    public int score(Color competitorPlayerColor) {
        return manager.score(competitorPlayerColor);
    }
}
