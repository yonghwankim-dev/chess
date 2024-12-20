package co.nemo.chess.domain.game;

import java.util.Optional;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.piece.NullPiece;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.player.AbstractCommand;
import co.nemo.chess.domain.player.Player;

public class CommandProcessor {

	private final Board board;
	private final InputStrategy inputStrategy;
	private final OutputStrategy outputStrategy;
	private final Player whitePlayer;
	private final Player darkPlayer;
	private Player currentPlayer;

	public CommandProcessor(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy) {
		this.board = board;
		this.inputStrategy = inputStrategy;
		this.outputStrategy = outputStrategy;
		this.whitePlayer = Player.white();
		this.darkPlayer = Player.dark();
		this.currentPlayer = this.whitePlayer;
	}

	public void setupPieces() {
		board.setupPieces();
	}

	/**
	 * 매개변수로 받은 명령어를 실행시킨다
	 * @param command 명령어
	 */
	public void process(AbstractCommand command) {
		boolean isChangedTurn = false;
		try {
			isChangedTurn = command.process(board, inputStrategy, outputStrategy, currentPlayer);
		} catch (IllegalArgumentException e) {
			outputStrategy.println(e.getMessage());
		}
		if (isChangedTurn) {
			switchPlayer();
		}
	}

	private void switchPlayer() {
		currentPlayer = (currentPlayer == whitePlayer) ? darkPlayer : whitePlayer;
	}

	public void printGameStatus(ChessGameStatusPrinter statusPrinter) {
		statusPrinter.printCurrentPlayer(currentPlayer);
		statusPrinter.printBoard(board);
	}

	public boolean isCheckmate(CheckmateChecker statusManager) {
		return statusManager.isCheckmate(board);
	}

	public Optional<Player> getWinner() {
		Piece checkmatedPiece = board.getCheckmatePiece().orElseGet(NullPiece::empty);
		if (whitePlayer.isOwnPiece(checkmatedPiece)) {
			return Optional.of(darkPlayer);
		} else if (darkPlayer.isOwnPiece(checkmatedPiece)) {
			return Optional.of(whitePlayer);
		} else {
			return Optional.empty();
		}
	}
}
