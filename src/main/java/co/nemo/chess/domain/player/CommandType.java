package co.nemo.chess.domain.player;

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
