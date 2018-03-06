package xyz.jmburns.passgen.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collection;
import java.util.function.UnaryOperator;

public class PasswordGenerator {
    private static final int DEFAULT_LENGTH = 25;

    public static String generate(Collection<String> phrase) {
        return generate(phrase, DEFAULT_LENGTH);
    }

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
