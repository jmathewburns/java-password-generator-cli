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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import xyz.jmburns.passgen.application.execution.ExecutionPath;

import java.util.ResourceBundle;

import static xyz.jmburns.passgen.application.execution.ExecutionPath.*;

class Main {
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");
    private static final Options SUPPORTED_OPTIONS = new SupportedOptions(MESSAGES);

    private static final String LINE_SPLIT_DELIMITER = "&";
    private static final int SUCCESS = 0, ERROR = 2;
    private static final int DEFAULT_MAXIMUM_LENGTH = 15;

    private final Console console;
    private final CommandLine arguments;

    private Main(CommandLine arguments) {
        this.console = new Console();
        this.arguments = arguments;
    }

    public static void main(String[] arguments) throws Exception {
        CommandLine parsedArguments = new DefaultParser().parse(SUPPORTED_OPTIONS, arguments);
        new Main(parsedArguments).run();
    }

    private void run() {
        Thread.currentThread().setUncaughtExceptionHandler((thread, exception) -> {
            console.display(exception.getMessage());
            System.exit(ERROR);
        });

        ExecutionPath<Void> executionPath;
        if (arguments.hasOption("help")) {
            executionPath = help(MESSAGES, SUPPORTED_OPTIONS);
        } else if (arguments.hasOption("example")) {
            executionPath = example(MESSAGES, LINE_SPLIT_DELIMITER);
        } else if (!arguments.getArgList().isEmpty()) {
            executionPath = directGenerationWithResult(
                    MESSAGES, arguments, DEFAULT_MAXIMUM_LENGTH
            );
        } else {
            executionPath = interactiveGenerationWithResult(
                    MESSAGES, LINE_SPLIT_DELIMITER, DEFAULT_MAXIMUM_LENGTH
            );
        }

        executionPath.run(console);
        System.exit(SUCCESS);
    }
}