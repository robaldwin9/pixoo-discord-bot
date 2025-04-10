package bot.commands.tool;

import bot.commands.common.AbstractCommand;
import bot.pixxoo.PixooRequestUtility;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.possible.Possible;
import reactor.core.publisher.Mono;

public class StartCountdownToolCommand extends AbstractCommand {
    public StartCountdownToolCommand() {
        super();
        setName("countdown");
        setDescription("Starts countdown for user provided minutes on pixoo");
        setType(ApplicationCommandOption.Type.INTEGER.getValue());
        setUserInputDescription("minutes");
        setRequired(Possible.of(true));
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        long minutes = event.getOption(getUserInputDescription())
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong).orElse(0L);
        String response ="Countdown started for " + minutes + " minutes";
        if (minutes > 100 || minutes < 0) {
          response = "Minutes must be less than 100, and a positive number.";
        }

        PixooRequestUtility.startCountdown((int) minutes, 0);
        return event.reply(response);
    }
}
