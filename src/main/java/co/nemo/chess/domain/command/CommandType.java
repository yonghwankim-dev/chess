package co.nemo.chess.domain.command;

import co.nemo.chess.domain.piece.Location;

public enum CommandType {
	MOVE {
		@Override
		public AbstractCommand createCommand(String[] commands) {
			if (commands.length != 3) {
				return AbstractCommand.invalidCommand();
			}
			int srcIdx = 1;
			int dstIdx = 2;
			try {
				Location src = Location.from(commands[srcIdx]);
				Location dst = Location.from(commands[dstIdx]);
				return AbstractCommand.moveCommand(src, dst);
			} catch (IllegalArgumentException e) {
				return AbstractCommand.invalidCommand();
			}
		}
	},
	LOCATIONS {
		@Override
		public AbstractCommand createCommand(String[] commands) {
			if (commands.length != 2) {
				return AbstractCommand.invalidCommand();
			}
			int srcIdx = 1;
			try {
				Location src = Location.from(commands[srcIdx]);
				return AbstractCommand.locationsCommand(src);
			} catch (IllegalArgumentException e) {
				return AbstractCommand.invalidCommand();
			}
		}
	},
	CASTLING {
		@Override
		public AbstractCommand createCommand(String[] commands) {
			if (commands.length != 3) {
				return AbstractCommand.invalidCommand();
			}
			int srcIdx = 1;
			int dstIdx = 2;
			try {
				Location src = Location.from(commands[srcIdx]);
				Location dst = Location.from(commands[dstIdx]);
				return AbstractCommand.castlingCommand(src, dst);
			} catch (IllegalArgumentException e) {
				return AbstractCommand.invalidCommand();
			}
		}
	},
	PROMOTION {
		/**
		 * 해당 메서드는 지원하지 않습니다.
		 * @param commands 명령어 및 매개변수
		 * @return 명령어
		 */
		@Override
		public AbstractCommand createCommand(String[] commands) {
			return AbstractCommand.invalidCommand();
		}
	},
	RESIGN {
		@Override
		public AbstractCommand createCommand(String[] commands) {
			return AbstractCommand.resignCommand();
		}
	},
	EXIT {
		@Override
		public AbstractCommand createCommand(String[] commands) {
			return AbstractCommand.exitCommand();
		}
	},
	HELP {
		@Override
		public AbstractCommand createCommand(String[] commands) {
			return AbstractCommand.helpCommand();
		}
	},
	NONE {
		@Override
		public AbstractCommand createCommand(String[] commands) {
			return AbstractCommand.invalidCommand();
		}
	};

	public abstract AbstractCommand createCommand(String[] commands);
}
