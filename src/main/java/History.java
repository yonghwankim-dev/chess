import java.util.ArrayList;
import java.util.List;

public class History {
    private List<Position> positions;

    public History() {
        positions = new ArrayList<>();
    }

    public void append(Position position){
        positions.add(position);
    }

    public Position last() {
        return positions.get(positions.size() - 1);
    }
}
