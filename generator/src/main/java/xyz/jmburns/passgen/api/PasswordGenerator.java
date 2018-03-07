package xyz.jmburns.passgen.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collection;
import java.util.function.UnaryOperator;

/**
 * Public utility class for securely hashing collections of strings.
 *
 * This class is intended to be used to generate secure passwords for various
 * websites and services based on a passphrase or password and a unique identifier
 * for each service.
 *
 * For example:
 *
 * {@code
 *  PasswordGenerator.generate(List.of("google.com", "My5uper$s3cretP4s5w0rd"));
 * }
 * ... will produce "0r/8mnF/Y1veLa4DYcNHIH42o", while:
 *
 * {@code
 *  PasswordGenerator.generate(List.of("reddit.com", "My5uper$s3cretP4s5w0rd"));
 * }
 * ... will produce "ckhMlnl1P+Y9IAxI7otccnpI6".
 */
public class PasswordGenerator {
    private static final int DEFAULT_LENGTH = 25;

    /**
     * Produces as securely hashed and encoded concatenation of the given {@code String} instances.
     *
     * SHA3-512 is used for hashing, and encoding is done in URL-unsafe base 64 format.
     *
     * This method behaves exactly as if calling {@code generate(Collection<String>, int)} but with a
     * default {@code maximumLength} value of 25.
     *
     * @see #generate(Collection, int)
     * @param phrase the collection of Strings to hash.
     * @return the concatenated, hashed, and encoded collection of Strings.
     * @throws IllegalArgumentException if {@code phrase} is null or empty
     */
    public static String generate(Collection<String> phrase) {
        return generate(phrase, DEFAULT_LENGTH);
    }

    /**
     * Produces as securely hashed and encoded concatenation of the given {@code String} instances.
     *
     * SHA3-512 is used for hashing, and encoding is done in URL-unsafe base 64 format.
     *
     * @param phrase the collection of Strings to hash.
     * @param maximumLength the maximum length of the resulting string. Strings returned by this method
     *                      will never exceed this length.
     * @return the concatenated, hashed, and encoded collection of Strings.
     * @throws IllegalArgumentException if {@code phrase} is null or empty, or if {@code maximumLength} is negative
     */
    public static String generate(Collection<String> phrase, int maximumLength) {
        validate(phrase, maximumLength);

        return phrase.stream()
                .sorted()
                .reduce(String::concat)
                .map(PasswordGenerator::hash)
                .map(PasswordGenerator::encode)
                .map(limitLength(maximumLength))
                .get();
    }

    private static void validate(Collection<String> phrase, int maximumLength) {
        if (phrase == null || phrase.isEmpty()) {
            throw new IllegalArgumentException("Phrase cannot be null or empty.");
        }

        if (maximumLength < 0) {
            throw new IllegalArgumentException("Maximum length cannot be negative.");
        }
    }

    private static String hash(String message) {
        try {
            return new String(hashBytes(message.getBytes()));
        } catch (NoSuchAlgorithmException wontHappen) {
            throw new RuntimeException(wontHappen);
        }
    }

    private static byte[] hashBytes(byte[] message) throws NoSuchAlgorithmException {
        MessageDigest hashFunction = MessageDigest.getInstance("SHA3-512");
        return hashFunction.digest(message);
    }

    private static String encode(String message) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(message.getBytes());
    }

    private static UnaryOperator<String> limitLength(int maximumLength) {
        return (message) -> {
            if (message.length() > maximumLength) {
                return message.substring(0, maximumLength);
            }
            return message;
        };
    }
}
