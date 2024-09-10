package bot.discord;

import bot.discord.common.AbstractCommand;
import bot.pixxoo.PixooRequestUtility;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class TextCommand extends AbstractCommand {

    private static final String ERROR_TEXT = "No text received";
    private static final Logger logger = LoggerFactory.getLogger(TextCommand.class);
    public TextCommand() {
        super();
        setName("text");
        setDescription("Command to place text on pixoo screen");
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        String response = "text received";
        String text = event.getOption(getUserInputDescription()).flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString).orElse(ERROR_TEXT);
        if (text.toCharArray().length > 600) {
            text = text.substring(0, 600);
        }

        if (!text.equals(ERROR_TEXT)) {
            PixooRequestUtility.sendText(text);
        }

        return event.reply(response);
    }
}
