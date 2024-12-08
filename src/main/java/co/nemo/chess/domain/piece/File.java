package co.nemo.chess.domain.piece;

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

	public int diff(File file) {
		return this.column - file.column;
	}
}
