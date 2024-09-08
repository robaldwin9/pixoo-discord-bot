package bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.Attachment;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

public class ImageCommand extends AbstractCommand {

    public ImageCommand() {
        super();
        setName("image");
        setDescription("Image to put on pixoo screen");
        setType(ApplicationCommandOption.Type.ATTACHMENT.getValue());
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        String attachmentsUrl= event.getInteraction().getCommandInteraction()
                .flatMap(ApplicationCommandInteraction::getResolved)
                .map(resolved -> resolved.getAttachments()
                        .values()
                        .stream()
                        .map(Attachment::getUrl)
                        .collect(Collectors.joining("\n")))
                .orElse("Command run without attachments");
        return event.reply("image received" + attachmentsUrl);
    }
}
