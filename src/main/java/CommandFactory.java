public class CommandFactory {
    public static Command moveCommand(String command){
        String[] positions = command.split("->");
        return new MoveCommand(Position.create(positions[0]), Position.create(positions[1]));
    }

    public static Command possibleCommand(String command){
        command = command.replace("?", "");
        return new PossibleCommand(Position.create(command));
    }

    public static Command exitCommand(){
        return new ExitCommand();
    }
}
