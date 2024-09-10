package bot.discord;

import bot.discord.common.AbstractCommand;
import bot.pixxoo.PixooRequestUtility;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import reactor.core.publisher.Mono;

public class StartNoiseToolCommand extends AbstractCommand {
    public StartNoiseToolCommand() {
        super();
        setName("sound-visualization");
        setDescription("Set display to sound visualizer");
        setType(ApplicationCommandOption.Type.STRING.getValue());
        setRequired(false);
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        PixooRequestUtility.sendNoiseToolStart();
        return event.reply("Pixxo display set to sound visualizer");
    }
}
