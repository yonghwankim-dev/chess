package co.nemo.chess.domain.game;

import java.util.Optional;

public interface InputStrategy {
	Optional<String> readLine();

	void close();
}
