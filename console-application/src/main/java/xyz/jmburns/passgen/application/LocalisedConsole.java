/*
 * Copyright 2018 Jacques Burns
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so, subject
 * to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
