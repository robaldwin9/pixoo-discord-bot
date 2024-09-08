package bot.commands;

import bot.BotApp;
import bot.pixxoo.PixooRequestUtility;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;


public class TextCommand extends AbstractCommand {
    private static final Logger logger = LoggerFactory.getLogger(TextCommand.class);
    public TextCommand() {
        super();
        setName("text");
        setDescription("Command to place text on pixoo screen");
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        final String text;
        PixooRequestUtility.sendText("event.");
        return event.reply("text received: " + event.getOption(getName()).get().getValue().get().asString());
    }
}
