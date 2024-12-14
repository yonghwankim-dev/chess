package co.nemo.chess.domain.player;

import java.io.BufferedReader;
import java.io.IOException;

import co.nemo.chess.domain.piece.Color;

public class Player {
	private final Color color;

	public Player(Color color) {
		this.color = color;
	}

	public AbstractCommand inputCommand(BufferedReader reader) {
		try {
			return CommandParser.getInstance().parse(reader.readLine());
		} catch (IOException e) {
			return AbstractCommand.invalidCommand();
		}
	}

	@Override
	public String toString() {
		return color.name();
	}
}
