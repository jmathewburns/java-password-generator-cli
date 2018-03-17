package xyz.jmburns.passgen.application;

import org.apache.commons.cli.Options;

import java.util.ResourceBundle;

final class SupportedOptions extends Options {
    SupportedOptions(ResourceBundle messages) {
        addOption(
                "h",
                "help",
                false,
                messages.getString("help.option.help")
        ).addOption(
                "e",
                "example",
                false,
                messages.getString("help.option.example")
        ).addOption(
                "l",
                "max-length",
                true,
                messages.getString("help.option.max_length")
        );
    }
}
