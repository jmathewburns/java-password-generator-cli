package xyz.jmburns.passgen.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordGeneratorTest {
    private static final List<String> TEST_PHRASE = List.of("correct", "horse", "battery", "staple");
    private static final int TEST_MAXIMUM_LENGTH = 2;

    @Nested
    class WhenPassedValidPhrase {
        @Test
        void shouldReturnSuccessfully() {
            PasswordGenerator.generate(TEST_PHRASE);
        }

        @Test
        void shouldNotReturnNull() {
            String result = PasswordGenerator.generate(TEST_PHRASE);
            assertFalse(result == null);
        }

        @Test
        void shouldNotReturnEmptyString() {
            String result = PasswordGenerator.generate(TEST_PHRASE);
            assertFalse(result.isEmpty());
        }

        @Test
        void shouldNotReturnSameText() {
            String result = PasswordGenerator.generate(TEST_PHRASE).toLowerCase();

            for (String word : TEST_PHRASE) {
                assertFalse(result.contains(word.toLowerCase()));
            }
        }

        @Test
        void shouldReturnConsistentResult() {
            String expected = PasswordGenerator.generate(TEST_PHRASE);

            for (int i = 0; i < 10; i++) {
                String latest = PasswordGenerator.generate(TEST_PHRASE);
                assertEquals(expected, latest);
            }
        }
    }

    @Nested
    class WhenPassedValidMaximumLength {
        @Test
        void shouldReturnSuccessfully() {
            PasswordGenerator.generate(TEST_PHRASE, TEST_MAXIMUM_LENGTH);
        }

        @Test
        void shouldNotReturnNull() {
            String result = PasswordGenerator.generate(TEST_PHRASE, TEST_MAXIMUM_LENGTH);
            assertFalse(result == null);
        }

        @Test
        void shouldNotReturnEmptyString() {
            String result = PasswordGenerator.generate(TEST_PHRASE, TEST_MAXIMUM_LENGTH);
            assertFalse(result.isEmpty());
        }

        @Test
        void shouldNotReturnSameText() {
            String result = PasswordGenerator.generate(TEST_PHRASE, TEST_MAXIMUM_LENGTH).toLowerCase();

            for (String word : TEST_PHRASE) {
                assertFalse(result.contains(word.toLowerCase()));
            }
        }

        @Test
        void resultShouldNotExceedMaximumLength() {
            String result = PasswordGenerator.generate(TEST_PHRASE, TEST_MAXIMUM_LENGTH);

            assertTrue(result.length() <= TEST_MAXIMUM_LENGTH);
        }
    }

    @Nested
    class WhenPassedNull {
        @Test
        void shouldThrowIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                    () -> PasswordGenerator.generate(null));
            assertThrows(IllegalArgumentException.class,
                    () -> PasswordGenerator.generate(null, TEST_MAXIMUM_LENGTH));
        }
    }

    @Nested
    class WhenPassedEmptyList {
        @Test
        void shouldThrowIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                    () -> PasswordGenerator.generate(List.of()));
            assertThrows(IllegalArgumentException.class,
                    () -> PasswordGenerator.generate(List.of(), TEST_MAXIMUM_LENGTH));
        }
    }

    @Nested
    class WhenPassedNegativeMaximumLength {
        @Test
        void shouldThrowIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class,
                    () -> PasswordGenerator.generate(TEST_PHRASE, -1));
        }
    }
}
