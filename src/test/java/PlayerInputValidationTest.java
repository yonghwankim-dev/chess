import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PlayerInputValidationTest {

    @ParameterizedTest
    @ValueSource(strings = {"A1->A2", "B1->D5", "G1->G5"})
    public void validate_moveCommandFormat_success(String command){
        //given

        //when
        boolean validate = PlayerInputValidation.validate(command);
        //then
        Assertions.assertThat(validate).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"A0->A2", "Q1->A2", "", " ", "aiowejf", "32049234", "@#!@#", "A1A2", "A11->B11", "AA1->A2"})
    public void validate_moveCommandFormat_fail(String command){
        //given

        //when
        boolean validate = PlayerInputValidation.validate(command);
        //then
        Assertions.assertThat(validate).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"?A1", "?B1", "?C1"})
    public void validate_possibleCommandFormat_success(String command){
        //given

        //when
        boolean validate = PlayerInputValidation.validate(command);
        //then
        Assertions.assertThat(validate).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"?A9", "B1", "?C11", "awef", "A", "ABC", "", " ", "!@#1", "1234"})
    public void validate_possibleCommandFormat_fail(String command){
        //given

        //when
        boolean validate = PlayerInputValidation.validate(command);
        //then
        Assertions.assertThat(validate).isFalse();
    }
}