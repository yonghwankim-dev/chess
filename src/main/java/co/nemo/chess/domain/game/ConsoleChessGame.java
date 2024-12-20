package co.nemo.chess.domain.game;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.piece.Color;
import co.nemo.chess.domain.player.AbstractCommand;
import co.nemo.chess.domain.player.Player;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleChessGame implements ChessGame {

	private final Board board;
	private final Player whitePlayer;
	private final Player darkPlayer;
	private final InputStrategy inputStrategy;
	private final CommandProcessor processor;
	private final ChessGameStatusManager statusManager;
	private final ChessGameStatusPrinter statusPrinter;
	private final ChessGameReader reader;

	public ConsoleChessGame(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy) {
		this.board = board;
		this.whitePlayer = new Player(Color.WHITE);
		this.darkPlayer = new Player(Color.DARK);
		this.inputStrategy = inputStrategy;
		this.processor = new CommandProcessor(board, inputStrategy, outputStrategy, whitePlayer);
		this.statusManager = new ChessGameStatusManager(board, outputStrategy);
		this.statusPrinter = new ChessGameStatusPrinter(outputStrategy);
		this.reader = new ChessGameReader(inputStrategy, statusPrinter);
	}

	public static ConsoleChessGame init() {
		return new ConsoleChessGame(
			Board.empty(),
			ConsoleInputStrategy.getInstance(),
			ConsoleOutputStrategy.getInstance()
		);
	}

	@Override
	public void startGame() {
		// 보드 초기 및 기물 배치
		board.setupPieces();
		statusPrinter.printGameStart();

		while (!statusManager.isCheckmate()) {
			statusPrinter.printGameStatus(processor.getCurrentPlayer(), board);
			AbstractCommand command = reader.readCommand();

			if (command.isExistCommand()) {
				break;
			}

			boolean isChangedTurn = processor.process(command);
			if (isChangedTurn) {
				processor.switchPlayer(whitePlayer, darkPlayer);
			}
		}
		inputStrategy.close();
	}
}
