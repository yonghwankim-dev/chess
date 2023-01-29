import java.util.List;

public class Board {
    private final Pieces pieces;

    public Board() {
        this.pieces = new Pieces();
    }

    public void init(){
        pieces.init();
    }

    public boolean move(Position from, Position to){
        return pieces.move(from, to);
    }

    public List<Position> possiblePositions(Position from){
        return pieces.possiblePositions(from);
    }

    public List<List<Piece>> display(){
        return pieces.display();
    }

    public boolean isOwner(Player player, Position from) {
        Color color = player.isWhite() ? Color.WHITE : Color.BLACK;
        return pieces.find(from).equalColor(color);
    }

    public boolean hasKing(Player player) {
        Color color = player.isWhite() ? Color.WHITE : Color.BLACK;
        return pieces.hasKing(color);
    }

    public int score(Player player) {
        Color competitorPlayerColor = player.isWhite() ? Color.BLACK : Color.WHITE;
        return pieces.score(competitorPlayerColor);
    }
}
