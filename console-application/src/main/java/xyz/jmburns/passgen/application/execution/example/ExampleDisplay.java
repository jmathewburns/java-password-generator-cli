package xyz.jmburns.passgen.application.execution.example;

import xyz.jmburns.passgen.application.Console;
import xyz.jmburns.passgen.application.execution.Display;

import java.util.List;
import java.util.ResourceBundle;

import static java.util.Arrays.asList;


public class ExampleDisplay implements Display {
    private final ResourceBundle messages;
    private final String lineSplitDelimiter;

    public ExampleDisplay(ResourceBundle messages, String lineSplitDelimiter) {
        this.messages = messages;
        this.lineSplitDelimiter = lineSplitDelimiter;
    }

    @Override
    public Void run(Console console) {
        console.displayAllLines(getExample());
        return null;
    }

    private List<String> getExample() {
        return asList(messages.getString("help.example").split(lineSplitDelimiter));
    }
}
