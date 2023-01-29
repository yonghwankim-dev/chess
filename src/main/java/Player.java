public class Player {
    private final PlayerInput playerInput;
    private final Color color;

    public Player(Color color) {
        this.playerInput = new PlayerInput();
        this.color = color;
    }

    public Command inputCommand(){
        return playerInput.input();
    }

    public boolean isWhite(){
        return color == Color.WHITE;
    }
}
