package bot.commands;

import bot.pixxoo.PixooRequestUtility;
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
        // Get user submitted attachment
        String attachmentsUrl= event.getInteraction().getCommandInteraction()
                .flatMap(ApplicationCommandInteraction::getResolved)
                .map(resolved -> resolved.getAttachments()
                        .values()
                        .stream()
                        .map(Attachment::getUrl)
                        .collect(Collectors.joining("\n")))
                .orElse("Command run without attachments");
        String response = "image sent to pixoo display";

        // file type checking
        if (containsIgnoreCase(attachmentsUrl, ".gif")) {
            PixooRequestUtility.sendGif(attachmentsUrl);
        } else if(containsIgnoreCase(attachmentsUrl, ".png")
                || containsIgnoreCase(attachmentsUrl, ".jpg")
                || containsIgnoreCase(attachmentsUrl, ".bmp")
                || containsIgnoreCase(attachmentsUrl, ".jpeg")) {
            PixooRequestUtility.sendSingleFrameImage(attachmentsUrl);
        } else {
            response = "image type not currently supported";
        }
        return event.reply(response);
    }

    public static boolean containsIgnoreCase(String string ,String subString) {
        return string.contains(subString)
                || string.contains(subString.toUpperCase())
                || string.contains(subString.toLowerCase());
    }
}
