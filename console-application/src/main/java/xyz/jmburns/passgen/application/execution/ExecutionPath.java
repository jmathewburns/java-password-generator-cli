package xyz.jmburns.passgen.application.execution;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import xyz.jmburns.passgen.application.Console;
import xyz.jmburns.passgen.application.execution.direct.DirectGeneration;
import xyz.jmburns.passgen.application.execution.example.ExampleDisplay;
import xyz.jmburns.passgen.application.execution.help.HelpDisplay;
import xyz.jmburns.passgen.application.execution.interactive.InteractiveGeneration;
import xyz.jmburns.passgen.application.execution.result.ResultDisplay;

import java.util.ResourceBundle;

public interface ExecutionPath<R> {
    R run(Console console) throws ExecutionException;

    static HelpDisplay help(ResourceBundle messages, Options supportedOptions) {
        return new HelpDisplay(messages, supportedOptions);
    }

    static ExampleDisplay example(ResourceBundle messages, String lineSplitDelimiter) {
        return new ExampleDisplay(messages, lineSplitDelimiter);
    }

    static ResultDisplay interactiveGenerationWithResult(ResourceBundle messages,
                                                         String lineSplitDelimiter,
                                                         int maximumPasswordLength) {
        return new ResultDisplay(new InteractiveGeneration(messages, lineSplitDelimiter, maximumPasswordLength), messages);
    }

    static ResultDisplay directGenerationWithResult(ResourceBundle messages,
                                                    CommandLine arguments,
                                                    int maximumPasswordLength) {
        return new ResultDisplay(new DirectGeneration(messages, arguments, maximumPasswordLength), messages);
    }
}
