package co.nemo.chess.domain.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class FileInputStrategy implements InputStrategy {

	private final BufferedReader reader;

	private FileInputStrategy(File file) {
		try {
			this.reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("not found file, FileName=" + file.getName(), e);
		}
	}

	public static InputStrategy from(File file) {
		return new FileInputStrategy(file);
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
