package co.nemo.chess.domain.game;

import java.util.Optional;

import co.nemo.chess.domain.player.Player;

public interface ChessGame {
	/**
	 * 체스 게임을 시작하고 종료시 승자 Player를 반환한다
	 * @return 승자 플레이어
	 */
	Optional<Player> startGame();
}
