package co.nemo.chess.domain.game;

import java.util.Optional;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.piece.Color;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.player.AbstractCommand;
import co.nemo.chess.domain.player.CommandParser;
import co.nemo.chess.domain.player.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChessGame {

	private final Board board;

	private final Player whitePlayer;
	private final Player darkPlayer;
	private final InputStrategy inputStrategy;
	private final OutputStrategy outputStrategy;
	private final CommandProcessor processor;

	public ChessGame(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy) {
		this.board = board;
		this.whitePlayer = new Player(Color.WHITE);
		this.darkPlayer = new Player(Color.DARK);
		this.inputStrategy = inputStrategy;
		this.outputStrategy = outputStrategy;
		this.processor = new CommandProcessor(board, inputStrategy, outputStrategy, whitePlayer);
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

		while (!isCheckmate()) {
			printGameStatus();
			AbstractCommand command = inputPlayerCommand();

			if (command.isExistCommand()) {
				break;
			}

			boolean isChangedTurn = processor.process(command);
			if (isChangedTurn) {
				switchPlayer();
			}
		}
		inputStrategy.close();
	}

	private AbstractCommand inputPlayerCommand() {
		outputStrategy.print("> ");
		try {
			return inputStrategy.readLine()
				.map(text -> CommandParser.getInstance().parse(text))
				.orElseThrow(() -> new IllegalArgumentException("No input received. Exiting game."));
		} catch (IllegalArgumentException e) {
			outputStrategy.print(e.getMessage());
			return AbstractCommand.exitCommand();
		}
	}

	private boolean isCheckmate() {
		Optional<Piece> piece = board.getCheckmatePiece();
		if (piece.isEmpty()) {
			return false;
		}
		Piece checkmatedPiece = piece.orElseThrow();
		if (checkmatedPiece.isColorOf(Color.WHITE)) {
			outputStrategy.println("흑 플레이어 승리!");
			outputStrategy.printBoard(board);
			return true;
		} else if (checkmatedPiece.isColorOf(Color.DARK)) {
			outputStrategy.println("백 플레이어 승리!");
			outputStrategy.printBoard(board);
			return true;
		}
		return false;
	}

	private void printGameStatus() {
		outputStrategy.println("현재 차례: " + processor.getCurrentPlayer());
		outputStrategy.printBoard(board);
	}

	public void switchPlayer() {
		processor.switchPlayer(whitePlayer, darkPlayer);
	}
}
