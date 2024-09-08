package bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public interface Command {
    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);

    int getType();
    void setType(int type);

    boolean isRequired();
    void setRequired(boolean required);

    Mono<Void> execute(ChatInputInteractionEvent event);
}
