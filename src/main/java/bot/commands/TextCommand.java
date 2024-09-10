package bot.commands;

import bot.pixxoo.PixooRequestUtility;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
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
        String response = "text received";
        String text = event.getOption(getUserInputDescription()).get().getValue().get().asString();
        if (text.toCharArray().length > 600) {
            text = text.substring(0, 600);
        }

        PixooRequestUtility.sendText(text);

//        String userText = event.getOption(getName())
//                        .flatMap(ApplicationCommandInteractionOption::getValue)
//                                .map(resolved -> resolved.asString())
//                                        .orElse("command run without user input");



        return event.reply(response);
    }
}
