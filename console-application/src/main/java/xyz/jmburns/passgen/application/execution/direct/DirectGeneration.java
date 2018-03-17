package xyz.jmburns.passgen.application.execution.direct;

import org.apache.commons.cli.CommandLine;
import xyz.jmburns.passgen.api.PasswordGenerator;
import xyz.jmburns.passgen.application.Console;
import xyz.jmburns.passgen.application.execution.ExecutionException;
import xyz.jmburns.passgen.application.execution.Generation;

import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public final class DirectGeneration implements Generation {
    private final ResourceBundle messages;
    private final CommandLine arguments;
    private final int maximumPasswordLength;

    public DirectGeneration(ResourceBundle messages, CommandLine arguments, int maximumPasswordLength) {
        this.messages = messages;
        this.arguments = arguments;
        this.maximumPasswordLength = maximumPasswordLength;
    }

    @Override
    public String run(Console console) throws ExecutionException {
        int maximumLength = maximumPasswordLength;

        try {
            String maximumLengthArgument = arguments.getOptionValue("max-length");

            if (maximumLengthArgument != null) {
                maximumLength = parseInt(maximumLengthArgument);
            }
        } catch (NumberFormatException invalidInput) {
            throw new ExecutionException(messages.getString("error.invalid_length_option"));
        }

        return PasswordGenerator.generate(arguments.getArgList(), maximumLength);
    }
}
