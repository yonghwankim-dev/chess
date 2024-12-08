package co.nemo.chess.domain;

public enum File {
	A(1), B(2), C(3), D(4), E(5), F(6), G(7), H(8);

	private final int column;

	File(int column) {
		this.column = column;
	}

	public static File from(String value) {
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException("value cannot be null or empty");
		}
		try {
			return File.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid File value: " + value, e);
		}
	}

	public static File columnOf(int column) {
		for (File file : values()) {
			if (file.column == column) {
				return file;
			}
		}
		throw new IllegalArgumentException("invalid column value: " + column);
	}

	public File adjust(Direction direction, int distance) {
		int newColumn = direction.calFileDistance(distance);
		return columnOf(this.column + newColumn);
	}

	public int diff(File file) {
		return Math.abs(this.column - file.column);
	}
}
