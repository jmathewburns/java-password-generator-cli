package xyz.jmburns.passgen.application.execution.result;

import xyz.jmburns.passgen.application.Console;
import xyz.jmburns.passgen.application.execution.Display;
import xyz.jmburns.passgen.application.execution.ExecutionException;
import xyz.jmburns.passgen.application.execution.ExecutionPath;
import xyz.jmburns.passgen.application.execution.Generation;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ResourceBundle;

public final class ResultDisplay implements Display {
    private final ExecutionPath<String> generator;
    private final ResourceBundle messages;

    public ResultDisplay(Generation generator, ResourceBundle messages) {
        this.generator = generator;
        this.messages = messages;
    }

    @Override
    public Void run(Console console) throws ExecutionException {
        String password = generator.run(console);

        String message = localise("message.result") + " " + password;
        console.display(message);

        boolean shouldCopy = console.promptYesOrNo(localise("message.clipboard"));
        if (shouldCopy) {
            copyToClipboard(password);
        }
        return null;
    }

    private String localise(String messageKey) {
        return messages.getString(messageKey);
    }

    private void copyToClipboard(String password) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection data = new StringSelection(password);
        clipboard.setContents(data, null);
    }
}
