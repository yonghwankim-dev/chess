package co.nemo.chess.domain;

public enum File {
	A,B,C,D,E,F,G,H;

	public static File from(String value){
		if (value == null || value.isEmpty()){
			throw new IllegalArgumentException("value cannot be null or empty");
		}
		try{
			return File.valueOf(value.toUpperCase());
		}catch (IllegalArgumentException e){
			throw new IllegalArgumentException("Invalid File value: " + value, e);
		}
	}
}
