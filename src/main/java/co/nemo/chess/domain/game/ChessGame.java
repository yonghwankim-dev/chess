package co.nemo.chess.domain.game;

import java.util.Optional;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.piece.Color;
import co.nemo.chess.domain.player.AbstractCommand;
import co.nemo.chess.domain.player.CommandType;
import co.nemo.chess.domain.player.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChessGame {

	private final Board board;

	private final Player whitePlayer;
	private final Player blackPlayer;
	private Player currentPlayer;
	private final InputStrategy inputStrategy;
	private final OutputStrategy outputStrategy;

	public ChessGame(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy) {
		this.board = board;
		this.whitePlayer = new Player(Color.WHITE);
		this.blackPlayer = new Player(Color.DARK);
		this.currentPlayer = whitePlayer;
		this.inputStrategy = inputStrategy;
		this.outputStrategy = outputStrategy;
	}

	public static ChessGame consoleBased() {
		return new ChessGame(
			Board.empty(),
			ConsoleInputStrategy.getInstance(),
			ConsoleOutputStrategy.getInstance()
		);
	}

	public void startGame() {
		// 보드 초기 및 기물 배치
		board.setupPieces();
		outputStrategy.println("Game Start");

		while (true) {
			printGameStatus();
			outputStrategy.print("> ");
			Optional<String> inputLine = inputStrategy.readLine();
			if (inputLine.isEmpty()) {
				outputStrategy.print("No input received. Exiting game.");
				break;
			}

			AbstractCommand command = currentPlayer.inputCommand(inputLine.get());
			if (command.isTypeOf(CommandType.EXIT)) {
				break;
			} else if (command.isTypeOf(CommandType.LOCATIONS) ||
				command.isTypeOf(CommandType.HELP)) {
				command.process(board, inputStrategy, outputStrategy, currentPlayer);
				continue;
			} else if (command.isTypeOf(CommandType.NONE)) {
				continue;
			}

			try {
				command.process(board, inputStrategy, outputStrategy, currentPlayer);
			} catch (IllegalArgumentException e) {
				outputStrategy.println(e.getMessage());
				continue;
			}
			this.switchPlayer();
		}
		inputStrategy.close();
	}

	private void printGameStatus() {
		outputStrategy.println("CurrentPlayer: " + currentPlayer);
		outputStrategy.printBoard(board);
	}

	public void switchPlayer() {
		currentPlayer = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
	}
}
