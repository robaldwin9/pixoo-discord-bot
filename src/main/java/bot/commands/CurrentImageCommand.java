package bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.spec.MessageCreateFields;
import reactor.core.publisher.Mono;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CurrentImageCommand extends AbstractCommand {

    public CurrentImageCommand() {
        super();
        setName("current");
        setDescription("Current image displayed by pixoo display");
        setType(ApplicationCommandOption.Type.STRING.getValue());
        setRequired(false);
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        MessageCreateFields.File file = new MessageCreateFields.File() {
            @Override
            public String name() {
                return "last-image.png";
            }

            @Override
            public InputStream inputStream() {
                try {
                    return new FileInputStream("last-image.png");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        return event.reply().withFiles(file);
    }
}
