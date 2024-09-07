package bot.commands;

import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import reactor.core.publisher.Mono;

public class ImageCommand extends AbstractCommand {

    public ImageCommand() {
        super();
        setName("image");
        setDescription("Image to put on pixoo screen");
        setType(ApplicationCommandOption.Type.ATTACHMENT.getValue());
    }

    @Override
    public Mono<Void> execute(ApplicationCommandInteractionEvent event) {
        return event.reply("image received");
    }
}
