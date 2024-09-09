package bot;

import bot.commands.Command;
import bot.commands.Commands;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;

import discord4j.core.object.entity.User;
import discord4j.discordjson.json.ApplicationCommandData;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import java.util.Map;

public class BotApp {
    private static final Logger logger = LoggerFactory.getLogger(BotApp.class);

    private static Config config;

    private static Commands commands;

    public static void main(String[] args) {
        // Connect to discord
        config = Config.getInstance();
        commands = Commands.getInstance();

        var client = DiscordClient.create(config.getBotToken());
        Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) ->  {
            // Login event
            Mono<Void> onLogin = gateway.on(ReadyEvent.class, event ->
                    Mono.fromRunnable(() -> {
                        final User self = event.getSelf();
                        logger.info("Logged in as {}:{}", self.getUsername(), self.getDiscriminator());
                    })).then();

            // Respond to commands
            gateway.on(ChatInputInteractionEvent.class, event -> {
                // logic
                if (commands.getCommands().containsKey(event.getCommandName())) {
                    return commands.getCommands().get(event.getCommandName()).execute(event);
                }
                return Mono.empty();
            }).subscribe();

            return onLogin;
        });

        // Setup Commands
        overwriteCommands(client);
        createCommands(client);
        login.block();
}

    public static void createCommands(DiscordClient client) {
        long applicationId = client.getApplicationId().block();

        for (long guildId : config.getGuildIds()) {
            // Build our command's definition
            for (Command command : commands.getCommands().values()) {
                ApplicationCommandRequest cmdRequest = ApplicationCommandRequest.builder()
                        .name(command.getName().toLowerCase())
                        .description(command.getDescription())
                        .addOption(ApplicationCommandOptionData.builder()
                                .name(command.getUserInputDescription())
                                .description(command.getDescription().toLowerCase())
                                .type(command.getType())
                                .required(command.isRequired())
                                .build()
                        ).build();

                // Create the command with Discord
                client.getApplicationService()
                        .createGuildApplicationCommand(applicationId, guildId, cmdRequest)
                        .subscribe();
            }
        }

        logger.info("Commands registered");
    }

    public static void overwriteCommands(DiscordClient client) {
        long applicationId = client.getApplicationId().block();

        for (long guildId : config.getGuildIds()) {
            // Get the commands from discord as a Map
            Map<String, ApplicationCommandData> discordCommands = client
                    .getApplicationService()
                    .getGuildApplicationCommands(applicationId, guildId)
                    .collectMap(ApplicationCommandData::name)
                    .block();

            // Remove all discord commands in case one was removed
            if (discordCommands != null && !discordCommands.isEmpty()) {
                for (Map.Entry<String, ApplicationCommandData> mapEntry : discordCommands.entrySet()) {
                    ApplicationCommandData commandData = mapEntry.getValue();

                    // Delete it
                    client.getApplicationService()
                            .deleteGuildApplicationCommand(applicationId, guildId, commandData.id().asLong())
                            .subscribe();
                }
            }
        }
        logger.info("Old Slash Commands Removed");
    }
}