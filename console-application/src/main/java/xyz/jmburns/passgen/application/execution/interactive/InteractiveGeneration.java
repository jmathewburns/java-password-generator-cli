package xyz.jmburns.passgen.application.execution.interactive;

import xyz.jmburns.passgen.api.PasswordGenerator;
import xyz.jmburns.passgen.application.Console;
import xyz.jmburns.passgen.application.execution.Generation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import static java.util.Arrays.asList;

public final class InteractiveGeneration implements Generation {
    private final ResourceBundle messages;
    private final String lineSplitDelimiter;
    private final int maximumPasswordLength;

    public InteractiveGeneration(ResourceBundle messages, String lineSplitDelimiter, int maximumPasswordLength) {
        this.messages = messages;
        this.lineSplitDelimiter = lineSplitDelimiter;
        this.maximumPasswordLength = maximumPasswordLength;
    }

    @Override
    public String run(Console console) {
        console.display(localise("message.welcome"));
        console.nextLine();

        return PasswordGenerator.generate(
                promptForPhrase(console),
                promptForMaximumPasswordLength(console)
        );
    }

    private int promptForMaximumPasswordLength(Console console) {
        return console.promptForOptionalInt(
                localise("question.password_length"),
                maximumPasswordLength
        );
    }

    private List<String> promptForPhrase(Console console) {
        List<String> phrase = new ArrayList<>();

        String websiteName = console.promptForString(localise("question.website_name"));
        phrase.add(websiteName);

        phrase.addAll(console.promptForAllOptionalStrings(getSecurityQuestions()));

        return phrase;
    }

    private String localise(String messageKey) {
        return messages.getString(messageKey);
    }

    private Collection<String> getSecurityQuestions() {
        return asList(localise("question.security_questions").split(lineSplitDelimiter));
    }
}
