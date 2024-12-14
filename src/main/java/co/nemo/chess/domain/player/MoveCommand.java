package co.nemo.chess.domain.player;

import co.nemo.chess.domain.board.Board;
import co.nemo.chess.domain.game.OutputStrategy;
import co.nemo.chess.domain.piece.Location;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Slf4j
public class MoveCommand extends AbstractCommand {

	private final Location src;
	private final Location dst;

	private MoveCommand(CommandType type, Location src, Location dst) {
		super(type);
		this.src = src;
		this.dst = dst;
	}

	public static MoveCommand of(Location src, Location dst) {
		return new MoveCommand(CommandType.MOVE, src, dst);
	}

	@Override
	public void process(Board board) {
		board.movePiece(src, dst).ifPresent(piece -> log.info("{}, move {}->{}", piece, src, dst));
	}

	@Override
	public void process(OutputStrategy outputStrategy) {

	}

	@Override
	public String toString() {
		return super.toString() + String.format("src=%s, dst=%s", src, dst);
	}
}
