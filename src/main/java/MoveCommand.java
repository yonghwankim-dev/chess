public class MoveCommand implements Command{
    private Position from;
    private Position to;

    public MoveCommand(Position from, Position to) {
        this.from = from;
        this.to = to;
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

        boolean result = board.move(from, to);
        if(!result){
            ConsoleView.failMove();
        }
        return result;
    }

}
