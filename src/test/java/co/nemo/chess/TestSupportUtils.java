package co.nemo.chess;

import java.util.Arrays;

import org.junit.jupiter.params.provider.Arguments;

public final class TestSupportUtils {

	public static Arguments[] createArgumentsArray(String source, String[] destinations) {
		return Arrays.stream(destinations)
			.map(destination -> Arguments.of(source, destination))
			.toArray(Arguments[]::new);
	}
}
