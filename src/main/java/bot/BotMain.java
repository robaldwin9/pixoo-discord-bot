package bot;

import bot.commands.common.Commands;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class BotMain {
    private static final Logger logger = LoggerFactory.getLogger(BotMain.class);

    private static Commands commands;

    public static void main(String[] args) {
        // Initialize Singletons
        Config config = Config.getInstance();
        commands = Commands.getInstance();

        // Connect to discord
        var client = DiscordClient.create(config.getBotToken());
        Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) ->  {
            // Login event
            Mono<Void> onLogin = gateway.on(ReadyEvent.class, event ->
                    Mono.fromRunnable(() -> {
                        final User self = event.getSelf();
                        logger.info("Logged in as :{}", self.getUsername());
                    })).then();

            // Respond to commands
            gateway.on(ChatInputInteractionEvent.class, event -> {
                // Match command, and call its execute logic
                if (commands.getCommands().containsKey(event.getCommandName())) {
                    return commands.getCommands().get(event.getCommandName()).execute(event);
                }
                return Mono.empty();
            }).subscribe();

            return onLogin;
        });

        // Setup Commands
        commands.registerCommands(client);
        login.block();
    }
}