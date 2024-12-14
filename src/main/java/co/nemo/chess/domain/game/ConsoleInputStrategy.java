package co.nemo.chess.domain.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class ConsoleInputStrategy implements InputStrategy {
	private final BufferedReader reader;

	private ConsoleInputStrategy() {
		this.reader = new BufferedReader(new InputStreamReader(System.in));
	}

	private static class ConsoleInputStrategyHelper {
		private static final InputStrategy INSTANCE = new ConsoleInputStrategy();
	}

	public static InputStrategy getInstance() {
		return ConsoleInputStrategyHelper.INSTANCE;
	}

	@Override
	public Optional<String> readLine() {
		try {
			String line = reader.readLine();
			return Optional.ofNullable(line);
		} catch (IOException e) {
			System.err.println("Error reading from keyboard: " + e.getMessage());
			return Optional.empty();
		}
	}

	@Override
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			System.err.println("Error closing keyboard input: " + e.getMessage());
		}
	}
}
