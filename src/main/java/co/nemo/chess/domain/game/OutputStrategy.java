package co.nemo.chess.domain.game;

import co.nemo.chess.domain.board.Board;

public interface OutputStrategy {
	void print(String message);

	void println(String message);

	void printBoard(Board board);
}
