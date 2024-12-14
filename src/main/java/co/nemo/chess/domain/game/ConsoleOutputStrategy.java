package co.nemo.chess.domain.game;

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
		System.out.println(message);
	}
}
