import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PlayerInput {
    private final BufferedReader br;

    public PlayerInput() {
        this.br = new BufferedReader(new InputStreamReader(System.in));
    }

    public Command input(){
        String command = inputCommand();
        if(PlayerInputValidation.validateExitCommandFormat(command)){
            return CommandFactory.exitCommand();
        }
        if(PlayerInputValidation.validateMoveCommandFormat(command)){
            return CommandFactory.moveCommand(command);
        }
        return CommandFactory.possibleCommand(command);
    }

    private String inputCommand(){
        String command;

        while(true){
            try {
                command = br.readLine();
                if(!PlayerInputValidation.validate(command)){
                    throw new IOException();
                }
                break;
            } catch (IOException e) {
                ConsoleView.warningInputMsg();
                ConsoleView.inputMsg();
            }
        }
        return command;
    }
}
