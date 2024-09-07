package bot.commands;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

public class TextCommand extends AbstractCommand {

    public TextCommand() {
        super();
        setName("text");
        setDescription("Command to place text on pixoo screen");
    }

    @Override
    public Mono<Void> execute(ApplicationCommandInteractionEvent event) {
        return event.reply("text received");
    }
}
