package xyz.jmburns.passgen.application.execution.help;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import xyz.jmburns.passgen.application.Console;
import xyz.jmburns.passgen.application.execution.Display;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ResourceBundle;

public final class HelpDisplay implements Display {
    private final ResourceBundle messages;
    private final Options supportedOptions;

    public HelpDisplay(ResourceBundle messages, Options supportedOptions) {
        this.messages = messages;
        this.supportedOptions = supportedOptions;
    }

    @Override
    public Void run(Console console) {
        PrintWriter buffer = new PrintWriter(new StringWriter());

        new HelpFormatter().printHelp(
                buffer,
                HelpFormatter.DEFAULT_WIDTH,
                localise("help.usage"),
                localise("help.header"),
                supportedOptions,
                HelpFormatter.DEFAULT_LEFT_PAD,
                HelpFormatter.DEFAULT_DESC_PAD,
                localise("help.footer")
        );
        console.display(buffer.toString());
        return null;
    }

    private String localise(String messageKey) {
        return messages.getString(messageKey);
    }
}
