package co.nemo.chess.domain.game;

import java.util.Optional;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.command.AbstractCommand;
import co.nemo.chess.domain.piece.NullPiece;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.player.Player;

public class CommandProcessor {

	private final Board board;
	private final ChessGameReader gameReader;
	private final ChessGameWriter gameWriter;
	private final Player whitePlayer;
	private final Player darkPlayer;
	private Player currentPlayer;
	private final CheckmateChecker checkmateChecker;

	public CommandProcessor(Board board, ChessGameReader gameReader, ChessGameWriter gameWriter,
		CheckmateChecker checkmateChecker) {
		this.board = board;
		this.gameReader = gameReader;
		this.gameWriter = gameWriter;
		this.whitePlayer = Player.white();
		this.darkPlayer = Player.dark();
		this.currentPlayer = this.whitePlayer;
		this.checkmateChecker = checkmateChecker;
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
			isChangedTurn = command.process(board, gameReader, gameWriter, currentPlayer);
		} catch (IllegalArgumentException e) {
			gameWriter.printErrorMessage(e);
		}
		if (isChangedTurn) {
			switchPlayer();
		}
	}

	private void switchPlayer() {
		currentPlayer = (currentPlayer == whitePlayer) ? darkPlayer : whitePlayer;
	}

	public void printGameStatus(ChessGameWriter statusPrinter) {
		statusPrinter.printCurrentPlayer(currentPlayer);
		statusPrinter.printBoard(board);
	}

	public boolean isCheckmate() {
		return checkmateChecker.isCheckmate(board);
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

	public Player getEnemyPlayer() {
		return currentPlayer == whitePlayer ? darkPlayer : whitePlayer;
	}

	public boolean isStalemate() {
		return board.isStalemate(currentPlayer);
	}
}
