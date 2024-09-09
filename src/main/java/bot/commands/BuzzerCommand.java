package bot.commands;

import bot.pixxoo.PixooRequestUtility;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandOption;
import reactor.core.publisher.Mono;

public class BuzzerCommand extends AbstractCommand {

    public BuzzerCommand() {
        super();
        setName("buzzer");
        setDescription("Plays a buzzer sound on pixoo for 1 second");
        setType(ApplicationCommandOption.Type.STRING.getValue());
        setRequired(false);
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {
        PixooRequestUtility.sendBuzzerCommand();
        return event.reply("Buzzer Started for 1s");
    }
}
