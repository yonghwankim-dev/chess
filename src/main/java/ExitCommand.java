public class ExitCommand implements Command{
    @Override
    public void execute() {
        System.exit(-1);
    }

    @Override
    public boolean execute(Player player, Board board) {
        return false;
    }
}
