public interface Command {

    void execute();
    boolean execute(Player player, Board board);
}
