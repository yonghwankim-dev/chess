package co.nemo.chess.domain.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

public class StringInputStrategy implements InputStrategy {

	private final BufferedReader reader;

	public StringInputStrategy(String input) {
		this.reader = new BufferedReader(new StringReader(input));
	}
	
	@Override
	public Optional<String> readLine() {
		try {
			String line = reader.readLine();
			return Optional.ofNullable(line);
		} catch (IOException e) {
			System.err.println("Error reading from string input: " + e.getMessage());
			return Optional.empty();
		}
	}

	@Override
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			System.err.println("Error closing string input: " + e.getMessage());
		}
	}
}
