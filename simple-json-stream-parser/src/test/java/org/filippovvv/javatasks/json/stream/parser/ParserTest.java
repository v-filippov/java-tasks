package org.filippovvv.javatasks.json.stream.parser;

import org.filippovvv.javatasks.json.stream.parser.model.Result;
import org.filippovvv.javatasks.json.stream.parser.model.ValueType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {
    private Parser sut;

    public static Stream<Arguments> valuesByKeyProvider() {
        return Stream.of(Arguments.of("key", ValueType.NUMBER, new Result(ValueType.NUMBER, "123")),
                Arguments.of("nested", ValueType.JSON, new Result(ValueType.JSON, "{\"id\":  2, \"name\":  \"nested object\"}")),
                Arguments.of("label", ValueType.STRING, new Result(ValueType.STRING, "\"text value\"")),
                Arguments.of("nestedEmptyObject", ValueType.JSON, new Result(ValueType.JSON, "{}")));
    }

    @ParameterizedTest
    @MethodSource("valuesByKeyProvider")
    public void shouldFindValueByKey(final String key, final ValueType type, final Result expectedResult) throws FileNotFoundException {
        // given
        InputStream is = new FileInputStream("src/test/resources/test.json");
        sut = new Parser(is);
        // when
        final Optional<Result> actualResult = sut.findByKey(key);

        // then
        assertTrue(actualResult.isPresent());
        assertEquals(type, actualResult.get().valueType());
        assertEquals(expectedResult, actualResult.get());
    }
}
