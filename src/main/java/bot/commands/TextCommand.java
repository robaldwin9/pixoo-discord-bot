package bot.commands;

import bot.pixxoo.PixooRequestUtility;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
//        String userText = event.getOption(getName())
//                        .flatMap(ApplicationCommandInteractionOption::getValue)
//                                .map(resolved -> resolved.asString())
//                                        .orElse("command run without user input");

        PixooRequestUtility.sendText(event.getOption(getName()).get().getValue().get().asString());

        return event.reply("text received: " + event.getOption(getName()).get().getValue().get().asString());
    }
}
