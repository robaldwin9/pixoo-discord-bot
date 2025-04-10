package bot.commands.common;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.possible.Possible;
import reactor.core.publisher.Mono;

public interface Command {
    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);

    int getType();
    void setType(int type);

    Possible<Boolean> isRequired();
    void setRequired(Possible<Boolean> required);

    String getUserInputDescription();
    void setUserInputDescription(String inputDescription);

    Mono<Void> execute(ChatInputInteractionEvent event);
    ApplicationCommandRequest getApplicationCommandRequest();
}
