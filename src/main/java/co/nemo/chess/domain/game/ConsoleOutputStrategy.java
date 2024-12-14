package co.nemo.chess.domain.game;

import java.util.Optional;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.piece.File;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.piece.Rank;

public class ConsoleOutputStrategy implements OutputStrategy {

	private ConsoleOutputStrategy() {

	}

	private static class ConsoleOutputStrategyHelper {
		private static final OutputStrategy INSTANCE = new ConsoleOutputStrategy();
	}

	public static OutputStrategy getInstance() {
		return ConsoleOutputStrategyHelper.INSTANCE;
	}

	@Override
	public void print(String message) {
		System.out.print(message);
	}

	@Override
	public void println(String message) {
		System.out.println(message);
	}

	@Override
	public void printBoard(Board board) {
		StringBuilder result = new StringBuilder();
		final String SPACE = " ";

		result.append(SPACE).append(SPACE);
		for (File file : File.values()) {
			if (file != File.H) {
				result.append(file).append(SPACE);
			} else {
				result.append(file);
			}
		}
		result.append(System.lineSeparator());

		for (int rankValue = 8; rankValue >= 1; rankValue--) {
			result.append(rankValue).append(SPACE);
			for (int fileValue = 1; fileValue <= 8; fileValue++) {
				// 기물이 존재하는지 확인
				Rank rank = Rank.from(rankValue);
				File file = File.valueOfColumn(fileValue);
				Location location = Location.of(file, rank);
				Optional<Piece> piece = board.findPiece(location);
				if (piece.isEmpty()) {
					result.append(SPACE).append(SPACE); // 공백은 두칸 띄워서 출력
				} else {
					result.append(piece.get().toSymbol()); // 기물 기호
					result.append(SPACE); // 각 칸 사이에 공백 추가
				}

			}
			result.append(System.lineSeparator()); // 각 행 끝에 줄 바꿈 추가
		}
		println(result.toString());
	}
}
