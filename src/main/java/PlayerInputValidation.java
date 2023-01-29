import java.util.regex.Pattern;

public class PlayerInputValidation {
    private static final Pattern MOVE_COMMAND_FORMAT = Pattern.compile("[A-H][1-8]->[A-H][1-8]"); // A2->A3
    private static final Pattern POSSIBLE_COMMAND_FORMAT = Pattern.compile("\\?[A-H][1-8]"); // ?A1
    private static final Pattern EXIT_FORMAT = Pattern.compile("exit");
    public static boolean validate(String command){
        return validateMoveCommandFormat(command) ||
                validatePossibleCommandFormat(command) ||
                    validateExitCommandFormat(command);
    }

    public static boolean validateMoveCommandFormat(String command){
        return MOVE_COMMAND_FORMAT.matcher(command).matches();
    }

    public static boolean validatePossibleCommandFormat(String command){
        return POSSIBLE_COMMAND_FORMAT.matcher(command).matches();
    }

    public static boolean validateExitCommandFormat(String command) {
        return EXIT_FORMAT.matcher(command).matches();
    }
}
