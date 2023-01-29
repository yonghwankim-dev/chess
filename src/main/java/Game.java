public class Game {
    private final Player whitePlayer;
    private final Player blackPlayer;
    private final Board board;

    public Game() {
        this.whitePlayer = new Player(Color.WHITE);
        this.blackPlayer = new Player(Color.BLACK);
        this.board = new Board();
    }

    public void start(){
        gameStartMsg();
        while(true){
            if (!continueGame(whitePlayer, blackPlayer) || !continueGame(blackPlayer, whitePlayer)){
                break;
            }
        }
    }

    private boolean continueGame(Player me, Player you) {
        turnPlayer(me);
        if(checkmate(you)) {
            ConsoleView.playerWinMsg(me);
            return false;
        }
        display();
        ConsoleView.displayScore(board.score(whitePlayer), board.score(blackPlayer));
        return true;
    }

    private void turnPlayer(Player player){
        boolean stop = false;
        while(!stop){
            turnMsg(player);
            inputMsg();
            Command command = player.inputCommand();
            stop = execute(player, command);
        }
    }

    private boolean execute(Player player, Command command){
        if(command instanceof ExitCommand){
            command.execute();
        }
        return command.execute(player, board);
    }

    // true : player의 패배, false : player가 아직 패배하지 않음
    private boolean checkmate(Player player){
        return !board.hasKing(player);
    }

    private void gameStartMsg() {
        init();
        initMsg();
        display();
    }

    private void init(){
        board.init();
    }

    private void initMsg(){
        ConsoleView.initMsg();
    }

    private void display(){
        ConsoleView.display(new Ranks(board.display()));
    }

    private void inputMsg(){
        ConsoleView.inputMsg();
    }

    private void turnMsg(Player player){
        ConsoleView.turnMsg(player);
    }
}
