package co.nemo.chess.domain.player;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.InputStrategy;
import co.nemo.chess.domain.game.OutputStrategy;

public class HelpCommand extends AbstractCommand {

	private HelpCommand(CommandType type) {
		super(type);
	}

	public static AbstractCommand create() {
		return new HelpCommand(CommandType.HELP);
	}

	@Override
	public void process(Board board, InputStrategy inputStrategy, OutputStrategy outputStrategy, Player player) {
		StringBuilder guide = new StringBuilder();
		guide.append("=== Chess Command Guide ===\n\n");
		guide.append("1. 이동 명령어:\n");
		guide.append("   - 사용법: move <시작좌표> <목표좌표>\n");
		guide.append("   - 설명: 체스 기물을 지정된 위치로 이동합니다.\n");
		guide.append("     예시: move a2 a3\n\n");
		guide.append("2. 이동 가능 경로 명령어:\n");
		guide.append("   - 사용법: locations <좌표>\n");
		guide.append("   - 설명: 특정 좌표에 있는 기물이 이동할 수 있는 모든 경로를 표시합니다.\n");
		guide.append("     예시: locations a2\n\n");
		guide.append("3. 종료 명령어:\n");
		guide.append("   - 사용법: exit\n");
		guide.append("   - 설명: 체스 게임을 종료합니다.\n\n");
		guide.append("=== 명령어를 입력하여 게임을 시작하세요! ===\n");
		outputStrategy.print(guide.toString());
	}
}
