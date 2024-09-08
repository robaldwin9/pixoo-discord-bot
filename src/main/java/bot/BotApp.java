package bot;

import bot.commands.Command;
import bot.commands.Commands;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
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

        // Build our command's definition
        for (Command command : commands.getCommands().values()) {
            ApplicationCommandRequest cmdRequest = ApplicationCommandRequest.builder()
                    .name(command.getName().toLowerCase())
                    .description(command.getDescription())
                    .addOption(ApplicationCommandOptionData.builder()
                            .name(command.getName().toLowerCase())
                            .description(command.getDescription().toLowerCase())
                            .type(command.getType())
                            .required(command.isRequired())
                            .build()
                    ).build();
            logger.error(cmdRequest.name());
            logger.error(command.getName());

            // Create the command with Discord
            client.getApplicationService()
                    .createGuildApplicationCommand(applicationId, config.getGuildId(), cmdRequest)
                    .subscribe();
        }

        logger.info("Commands registered");
    }

    public static void overwriteCommands(DiscordClient client) {
        long applicationId = client.getApplicationId().block();

        // Get the commands from discord as a Map
        Map<String, ApplicationCommandData> discordCommands = client
                .getApplicationService()
                .getGlobalApplicationCommands(applicationId)
                .collectMap(ApplicationCommandData::name)
                .block();

        if (discordCommands != null && !discordCommands.isEmpty()) {
            for (Command command : commands.getCommands().values()) {

                // Remove command only if present
                if (discordCommands.containsKey(command.getName())) {

                    // Get the ID of our application command
                    long commandId = discordCommands.get(command.getName()).id().asLong();

                    // Delete it
                    client.getApplicationService()
                            .deleteGlobalApplicationCommand(applicationId, commandId)
                            .subscribe();
                }
            }

            logger.info("Old Slash Commands Removed");
        }
    }
}