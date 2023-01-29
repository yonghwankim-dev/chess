import java.util.List;

public class PossibleCommand implements Command{
    private Position from;

    public PossibleCommand(Position from) {
        this.from = from;
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean execute(Player player, Board board) {
        if(!board.isOwner(player, from)){
            ConsoleView.warningColorMsg(player);
            return false;
        }

        List<Position> positions = board.possiblePositions(from);
        if(positions.isEmpty()){
            ConsoleView.emptyPossiblePositions();
            return false;
        }
        ConsoleView.possiblePositions(positions);
        return false;
    }
}
