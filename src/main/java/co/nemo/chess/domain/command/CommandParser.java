package co.nemo.chess.domain.command;

public class CommandParser {
	private static final String COMMAND_SEPARATOR_REGEX = " ";

	private CommandParser() {

	}

	private static class CommandParserHelper {
		private static final CommandParser INSTANCE = new CommandParser();
	}

	public static CommandParser getInstance() {
		return CommandParserHelper.INSTANCE;
	}

	public AbstractCommand parse(String text) {
		String[] commands;
		try {
			commands = text.split(COMMAND_SEPARATOR_REGEX);
		} catch (NullPointerException e) {
			return AbstractCommand.invalidCommand();
		}
		int commandNameIdx = 0;
		String commandText = commands[commandNameIdx].toUpperCase();
		return getCommandType(commandText).createCommand(commands);
	}

	private static CommandType getCommandType(String commandText) {
		try {
			return CommandType.valueOf(commandText);
		} catch (IllegalArgumentException e) {
			return CommandType.NONE;
		}
	}
}
