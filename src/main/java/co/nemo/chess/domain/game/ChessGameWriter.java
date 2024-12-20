package co.nemo.chess.domain.game;

import java.util.List;
import java.util.stream.Collectors;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.player.Player;

public class ChessGameWriter {
	private final OutputStrategy outputStrategy;

	public ChessGameWriter(OutputStrategy outputStrategy) {
		this.outputStrategy = outputStrategy;
	}

	public void printErrorMessage(Exception e) {
		outputStrategy.print(e.getMessage());
	}

	public void printPrompt() {
		outputStrategy.print("> ");
	}

	public void printGameStart() {
		outputStrategy.println("Game Start");
	}

	public void printCurrentPlayer(Player currentPlayer) {
		outputStrategy.println("현재 차례: " + currentPlayer);
	}

	public void printBoard(Board board) {
		outputStrategy.printBoard(board);
	}

	public void printDarkPlayerWin() {
		outputStrategy.println("흑 플레이어 승리!");
	}

	public void printWhitePlayerWin() {
		outputStrategy.println("백 플레이어 승리!");
	}

	public void printPromotionMessage() {
		outputStrategy.println(
			"Your phone has reached the end of your opponent's camp! Promote with any craft you want.");
		outputStrategy.println("Possible options: Queen, Rook, Bishop, Knight (Case free)");
		outputStrategy.println(
			"Type: Please enter the first letter of the object in capital letters (e.g., Queen, Rook, Bishop, Knight).");
	}

	public void printInvalidCommandMessage() {
		outputStrategy.print("invalid command.");
	}

	public void printLocations(List<Location> possibleLocations) {
		String message = possibleLocations.stream()
			.map(Location::toString)
			.collect(Collectors.joining(",", "[", "]"));
		outputStrategy.println(message);
	}

	public void printHelpMessage(String helpMessage) {
		outputStrategy.println(helpMessage);
	}

	public void printExitMessage() {
		outputStrategy.print("shutdown the game");
	}
}
