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

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.ResourceBundle;

import xyz.jmburns.passgen.api.PasswordGenerator;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;

class Main {
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");
    private static final Options SUPPORTED_OPTIONS;
    private static final int SUCCESS = 0;
    private static final int ERROR = 2;
    private static final int DEFAULT_MAXIMUM_LENGTH = 15;

    private final Console console;
    private final CommandLine arguments;

    static {
        SUPPORTED_OPTIONS = new Options().addOption(
                "h",
                "help",
                false,
                localise("help.option.help")
        ).addOption(
                "e",
                "example",
                false,
                localise("help.option.example")
        ).addOption(
                "l",
                "max-length",
                true,
                localise("help.option.max_length")
        );
    }

    private static String localise(String messageKey) {
        return MESSAGES.getString(messageKey);
    }

    private static List<String> localiseAll(String messagesKey, String lineSplitDelimiter) {
        return asList(localise(messagesKey).split(lineSplitDelimiter));
    }

    private Main(CommandLine arguments) {
        this.console = new Console();
        this.arguments = arguments;
    }    
    
    private void run() {
        Optional<String> result = Optional.empty();
        if (arguments.hasOption("help")) {
            handleHelpRequest();
        } else if (arguments.hasOption("example")) {
            handleExampleExample();
        } else if (!arguments.getArgList().isEmpty()) {
            result = Optional.of(generateFromArguments());
        } else {
            result = Optional.of(generateFromPrompts());
        }

        result.ifPresent(this::displayResult);
        System.exit(SUCCESS);
    }

    private void handleHelpRequest() {
        PrintWriter result = new PrintWriter(new StringWriter());

        new HelpFormatter().printHelp(
                result,
                HelpFormatter.DEFAULT_WIDTH,
                localise("help.usage"),
                localise("help.header"),
                SUPPORTED_OPTIONS,
                HelpFormatter.DEFAULT_LEFT_PAD,
                HelpFormatter.DEFAULT_DESC_PAD,
                localise("help.footer")
        );
        console.display(result.toString());
    }

    private void handleExampleExample() {
        console.displayAllLines(localiseAll("help.example", "%n"));
    }

    private String generateFromArguments() {
        int maximumLength = DEFAULT_MAXIMUM_LENGTH;

        try {
            String maximumLengthArgument = arguments.getOptionValue("max-length");

            if (maximumLengthArgument != null) {
                maximumLength = parseInt(maximumLengthArgument);
            }
        } catch (NumberFormatException invalidInput) {
            console.display(localise("error.invalid_length_option"));
            System.exit(ERROR);
        }

        return PasswordGenerator.generate(arguments.getArgList(), maximumLength);
    }

    private String generateFromPrompts() {
        console.display(localise("message.welcome"));
        console.nextLine();

        return PasswordGenerator.generate(
                promptForPhrase(),
                promptForMaximumPasswordLength()
        );
    }

    private int promptForMaximumPasswordLength() {
        OptionalInt length =
                console.promptForOptionalInt(localise("question.password_length"));

        return length.orElse(DEFAULT_MAXIMUM_LENGTH);
    }

    private List<String> promptForPhrase() {
        List<String> phrase = new ArrayList<>();

        String websiteName = console.promptForString(localise("question.website_name"));
        phrase.add(websiteName);

        phrase.addAll(console.promptForAllOptionalStrings(localiseAll(
                "question.security_questions",
                "&"
        )));

        return phrase;
    }

    private void displayResult(String password) {
        String message = localise("message.result") + " " + password;
        console.displayRaw(message);

        boolean shouldCopy = console.promptYesOrNo(localise("message.clipboard"));
        if (shouldCopy) {
            copyToClipboard(password);
        }
    }

    private void copyToClipboard(String password) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection data = new StringSelection(password);
        clipboard.setContents(data, null);
    }

    public static void main(String[] arguments) throws Exception {
        CommandLine parsedArguments = new DefaultParser().parse(SUPPORTED_OPTIONS, arguments);
        new Main(parsedArguments).run();
    }
}
