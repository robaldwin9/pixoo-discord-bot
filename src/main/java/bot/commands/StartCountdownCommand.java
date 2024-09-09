package bot.commands;

import bot.pixxoo.PixooRequestUtility;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import reactor.core.publisher.Mono;

public class StartCountdownCommand extends AbstractCommand {
    public StartCountdownCommand() {
        super();
        setName("countdown");
        setDescription("Starts countdown for user provided minutes on pixoo");
        setType(ApplicationCommandOption.Type.INTEGER.getValue());
        setUserInputDescription("minutes");
        setRequired(true);
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        int minutes = (int) event.getOption(getUserInputDescription()).get().getValue().get().asLong();
        String response ="Countdown started for " + minutes + " minutes";
        if (minutes > 100 || minutes < 0) {
          response = "Minutes must be less than 100, and a positive number.";
        }
        PixooRequestUtility.startCountdown(minutes, 0);
        return event.reply(response);
    }
}
