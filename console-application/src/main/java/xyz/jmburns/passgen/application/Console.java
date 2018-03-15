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

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;

final class Console {
    private final TextIO console;
    private final TextTerminal<?> display;

    Console() {
        console = TextIoFactory.getTextIO();
        display = console.getTextTerminal();
    }

    String promptForString(String message) {
        return console
                .newStringInputReader()
                .withInputTrimming(true)
                .read(message);
    }

    OptionalInt promptForOptionalInt(String message) {
        Integer input = console
                .newIntInputReader()
                .withDefaultValue(Integer.MIN_VALUE)
                .read(message);

        if (input.equals(Integer.MIN_VALUE)) {
            return OptionalInt.empty();
        } else {
            return OptionalInt.of(input);
        }
    }

    Collection<String> promptForAllOptionalStrings(Collection<String> messages) {
        Collection<String> result = new ArrayList<>(messages.size());

        for (String message : messages) {
            String answer = console
                                .newStringInputReader()
                                .withMinLength(0)
                                .read(message);
            if (!answer.isEmpty()) result.add(answer);
        }
        return result;
    }

    boolean promptYesOrNo(String message) {
        return console
                .newBooleanInputReader()
                .read(message);
    }

    void display(String message) {
        display.println(message);
    }

    void displayRaw(String message) {
        display(message);
    }

    void displayAllLines(List<String> messages) {
        display.println(messages);
    }

    void nextLine() {
        display.println();
    }
}
