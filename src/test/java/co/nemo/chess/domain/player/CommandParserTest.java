package co.nemo.chess.domain.player;

import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import co.nemo.chess.domain.piece.Location;

class CommandParserTest {

	public static Stream<Arguments> validCommandSource() {
		AbstractCommand moveCommandExpected = AbstractCommand.moveCommand(Location.from("a2"), Location.from("a3"));
		AbstractCommand locationsCommandExpected = AbstractCommand.locationsCommand(Location.from("a2"));
		AbstractCommand exitCommand = AbstractCommand.exitCommand();
		AbstractCommand helpCommand = AbstractCommand.helpCommand();
		return Stream.of(
			Arguments.of("move a2 a3", moveCommandExpected),
			Arguments.of("locations a2", locationsCommandExpected),
			Arguments.of("exit", exitCommand),
			Arguments.of("exit a2", exitCommand),
			Arguments.of("help", helpCommand)
		);
	}

	public static Stream<Arguments> invalidCommandTextSource() {
		Arguments[] arguments = Stream.of(
				null,
				"",
				"move",
				"move a2",
				"move a2 a3 a4",
				"move a9 a2",
				"move a2 a9",
				"move a2 i2",
				"location",
				"locations",
				"locations a9",
				"locations i2",
				"exi"
			)
			.map(Arguments::of)
			.toArray(Arguments[]::new);
		return Stream.of(arguments);
	}

	@DisplayName("명령어 텍스트로부터 명령어를 파싱한다")
	@ParameterizedTest
	@MethodSource(value = "validCommandSource")
	void parse(String text, AbstractCommand expected) {
		// given
		// when
		AbstractCommand actual = CommandParser.getInstance().parse(text);
		// then
		Assertions.assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("유효하지 않은 명령어 입력시 유효하지 않은 명령어 객체를 반환한다")
	@ParameterizedTest
	@MethodSource(value = "invalidCommandTextSource")
	void givenText_whenParseTheInvalidText_thenReturnInvalidCommand(String text) {
		// given

		// when
		AbstractCommand actual = CommandParser.getInstance().parse(text);
		// then
		AbstractCommand expected = AbstractCommand.invalidCommand();
		Assertions.assertThat(actual).isEqualTo(expected);
	}
}
