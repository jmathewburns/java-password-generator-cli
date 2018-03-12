package xyz.jmburns.passgen.application;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.OptionalInt;
import java.util.ResourceBundle;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

final class LocalisedConsole {
    private static final Scanner INPUT = new Scanner(System.in);
    private static final PrintStream OUTPUT = System.out;

    private final ResourceBundle messages;

    LocalisedConsole(ResourceBundle messages) {
        this.messages = messages;
    }

    String promptForString(String messageKey, String errorMessageKey) {
        String result = promptForString(messageKey);
        while (result.isEmpty()) {
            display(errorMessageKey);
            result = promptForString(messageKey);
        }
        return result;
    }

    String promptForString(String messageKey) {
        display(messageKey);
        return INPUT.nextLine().trim();
    }

    OptionalInt promptForOptionalInt(String messageKey, String errorMessageKey) {
        try {
            return promptForOptionalInt(messageKey);
        } catch (NumberFormatException invalidInput) {
            display(errorMessageKey);
            return promptForOptionalInt(messageKey, errorMessageKey);
        }
    }

    private OptionalInt promptForOptionalInt(String messageKey) {
        String input = promptForString(messageKey);

        if (input.isEmpty()) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(parseInt(input));
    }

    Collection<String> promptForAllOptionalStrings(String messagesKey, String messageSplitDelimiter) {
        String[] prompts = messages.getString(messagesKey).split(messageSplitDelimiter);
        Collection<String> result = new ArrayList<>(prompts.length);

        for (String message : prompts) {
            displayRaw(message);
            String answer = INPUT.nextLine().trim();
            if (!answer.isEmpty()) result.add(answer);
        }
        return result;
    }

    void display(String messageKey) {
        displayRaw(messages.getString(messageKey));
    }

    void displayRaw(String message) {
        OUTPUT.println(message);
    }

    void displayAllLines(String messageKey, String lineSplitDelimiter) {
        String[] lines = messages.getString(messageKey).split(lineSplitDelimiter);
        for (String line : lines) {
            displayRaw(line.trim());
        }
    }

    void nextLine() {
        OUTPUT.println();
    }
}
