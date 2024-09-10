package bot.discord.tool;

import bot.discord.common.AbstractCommand;
import bot.pixxoo.PixooRequestUtility;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import reactor.core.publisher.Mono;

public class StartStopWatchToolCommand extends AbstractCommand {
    public StartStopWatchToolCommand() {
        super();
        setName("stopwatch");
        setDescription("Start stop watch on pixoo display");
        setType(ApplicationCommandOption.Type.STRING.getValue());
        setRequired(false);
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        PixooRequestUtility.startStopWatch();
        return event.reply("Stop watch started");
    }
}
