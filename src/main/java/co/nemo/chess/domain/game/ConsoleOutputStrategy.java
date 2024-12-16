package co.nemo.chess.domain.game;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.piece.File;
import co.nemo.chess.domain.piece.Location;
import co.nemo.chess.domain.piece.Piece;
import co.nemo.chess.domain.piece.Rank;

public class ConsoleOutputStrategy implements OutputStrategy {

	private static final String SPACE = " ";

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
		String fileHeader = Arrays.stream(File.values())
			.map(File::name)
			.collect(Collectors.joining(SPACE));
		result.append(SPACE)
			.append(SPACE)
			.append(fileHeader)
			.append(System.lineSeparator());

		for (int rankValue = 8; rankValue >= 1; rankValue--) {
			result.append(rankValue).append(SPACE);
			for (int fileValue = 1; fileValue <= 8; fileValue++) {
				Rank rank = Rank.from(rankValue);
				File file = File.valueOfColumn(fileValue);
				Location location = Location.of(file, rank);
				Optional<Piece> piece = board.findPiece(location);
				// 기물이 존재하지 않으면 두칸 공백, 기물이 존재하면 기호+공백
				piece.ifPresentOrElse(p -> result.append(p.toSymbol()).append(SPACE),
					() -> result.append(SPACE).append(SPACE));
			}
			result.append(System.lineSeparator()); // 각 행 끝에 줄 바꿈 추가
		}
		println(result.toString());
	}
}
