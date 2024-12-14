package co.nemo.chess.domain.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.piece.Color;
import co.nemo.chess.domain.piece.Piece;
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
	private final BufferedReader reader;
	private final OutputStrategy outputStrategy;

	public ChessGame(Board board, OutputStrategy outputStrategy) {
		this.board = board;
		this.whitePlayer = new Player(Color.WHITE);
		this.blackPlayer = new Player(Color.DARK);
		this.currentPlayer = whitePlayer;
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.outputStrategy = outputStrategy;
	}

	public void startGame() {
		// 보드 초기 및 기물 배치
		board.setupPieces();
		outputStrategy.print("Game Start");

		while (true) {
			printGameStatus();
			outputStrategy.print("> ");
			AbstractCommand command = currentPlayer.inputCommand(reader);
			if (command.isTypeOf(CommandType.EXIT)) {
				break;
			} else if (command.isTypeOf(CommandType.HELP)) {
				command.process(outputStrategy);
				continue;
			} else if (command.isTypeOf(CommandType.NONE)) {
				continue;
			}
			command.process(board);
			switchPlayer();
		}
		closeReader();
	}

	private void closeReader() {
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}

	private void printGameStatus() {
		outputStrategy.print("CurrentPlayer: " + currentPlayer);
		String piecesMessage = board.getAllPieces().stream()
			.map(Piece::toString)
			.collect(Collectors.joining(Strings.LINE_SEPARATOR));
		outputStrategy.print(piecesMessage);
	}

	public void switchPlayer() {
		currentPlayer = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
	}
}
