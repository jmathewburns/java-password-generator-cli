package xyz.jmburns.passgen.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordGeneratorTest {
    private static final Pattern UPPERCASE_LETTERS = Pattern.compile("[A-Z]+");
    private static final Pattern LOWERCASE_LETTERS = Pattern.compile("[a-z]+");
    private static final Pattern NUMBERS = Pattern.compile("[0-9]+");
    private static final Pattern SYMBOLS = Pattern.compile("[!@?/#$%&]+");

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

        @Test
        void shouldBeCaseInsensitive() {
            List<String> withDifferentCasing = new ArrayList<>(TEST_PHRASE.size());
            withDifferentCasing.addAll(TEST_PHRASE);
            withDifferentCasing.set(0, withDifferentCasing.get(0).toUpperCase());

            String expected = PasswordGenerator.generate(TEST_PHRASE);
            String actual = PasswordGenerator.generate(withDifferentCasing);

            assertEquals(expected, actual);
        }

        @Test
        void shouldBeSpacingInsensitive() {
            List<String> withDifferentSpacing = new ArrayList<>(TEST_PHRASE.size());
            withDifferentSpacing.addAll(TEST_PHRASE);
            withDifferentSpacing.set(0, withDifferentSpacing.get(0).concat(" "));

            String expected = PasswordGenerator.generate(TEST_PHRASE);
            String actual = PasswordGenerator.generate(withDifferentSpacing);

            assertEquals(expected, actual);
        }

        @Test
        void shouldBeCaseAndSpacingInsensitive() {
            String expected = PasswordGenerator.generate(List.of("foo", "b  ar","BAZ"));
            String actual = PasswordGenerator.generate(List.of("FoO", "BAR", "ba z"));
            assertEquals(expected, actual);
        }

        @Test
        void shouldSatisfyPasswordCharacterRequirements() {
            String password = PasswordGenerator.generate(TEST_PHRASE);

            assertAll(
                    () -> contains(password, UPPERCASE_LETTERS),
                    () -> contains(password, LOWERCASE_LETTERS),
                    () -> contains(password, NUMBERS),
                    () -> contains(password, SYMBOLS)
            );
        }

        private boolean contains(String password, Pattern pattern) {
            return pattern.matcher(password).matches();
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
