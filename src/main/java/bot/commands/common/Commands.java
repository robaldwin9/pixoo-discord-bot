package bot.commands.common;

import bot.Config;
import bot.commands.*;
import bot.commands.tool.StartCountdownToolCommand;
import bot.commands.tool.StartNoiseToolCommand;
import bot.commands.tool.StartStopWatchToolCommand;
import discord4j.core.DiscordClient;
import discord4j.discordjson.json.ApplicationCommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Commands {
    HashMap<String, Command> commands;

    private static Commands instance;

    private Config config;

    private static final Logger logger = LoggerFactory.getLogger(Commands.class);

    /**
     * Private constructor initializes all command objects, Singleton patter
     */
    private Commands() {
        config = Config.getInstance();
        commands = new HashMap<>();

        ImageCommand imageCommand = new ImageCommand();
        commands.put(imageCommand.getName(), imageCommand);

        TextCommand textCommand = new TextCommand();
        commands.put(textCommand.getName(), textCommand);

        CurrentImageCommand currentImageCommand = new CurrentImageCommand();
        commands.put(currentImageCommand.getName(), currentImageCommand);

        StartCountdownToolCommand startCountdownCommand = new StartCountdownToolCommand();
        commands.put(startCountdownCommand.getName(), startCountdownCommand);

        StartStopWatchToolCommand startStopWatchCommand = new StartStopWatchToolCommand();
        commands.put(startStopWatchCommand.getName(), startStopWatchCommand);

        BuzzerCommand buzzerCommand = new BuzzerCommand();
        commands.put(buzzerCommand.getName(), buzzerCommand);

        StartNoiseToolCommand startNoiseToolCommand = new StartNoiseToolCommand();
        commands.put(startNoiseToolCommand.getName(), startNoiseToolCommand);
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    /**
     * Singleton pattern
     * @return Commands instance
     */
    public static Commands getInstance() {
        if(instance == null) {
            instance = new Commands();
        }

        return instance;
    }

    public void registerCommands(DiscordClient client) {
        if (config.isPublishSlashCommands()) {
           if (config.isUseGuildIds()) {
               logger.warn("Registering guild commands, " +
                       "using a pre-defined list of guild commands is typically for development, or debug");
               registerGuildCommands(client);
           } else {
               logger.info("Registering global commands, changes to global commands may not update for 1 hour");
               registerGlobalCommands(client);
           }
        }
    }

    /**
     * Register Discord slash commands
     *
     * @param client Discord4J discord client
     */
    private void registerGlobalCommands(DiscordClient client) {
        if (client.getApplicationId().blockOptional().isPresent()) {
            long applicationId = client.getApplicationId().blockOptional().get();

            // Cleanup old commands if present
            removeGlobalCommands(client, applicationId);
            removeGuildCommands(client, applicationId);

            // Register global command for each command object
            for (Command command : getCommands().values()) {
                client.getApplicationService()
                        .createGlobalApplicationCommand(applicationId, command.getApplicationCommandRequest())
                        .subscribe();
            }
        }
    }

    /**
     * Register all commands as guild commands, useful for development and debugging since global commands update slow
     *
     * @param client Discord4J discord client
     */
    private void registerGuildCommands(DiscordClient client) {
        if (client.getApplicationId().blockOptional().isPresent()) {
            removeGuildCommands(client, client.getApplicationId().blockOptional().get());
            removeGuildCommands(client, client.getApplicationId().blockOptional().get());
            long applicationId = client.getApplicationId().blockOptional().get();
            // Register with listed guilds, useful for testing
            for (long guildId : config.getGuildIds()) {
                // Build our command's definition
                for (Command command : getCommands().values()) {
                    // Create the command with Discord
                    client.getApplicationService()
                            .createGuildApplicationCommand(applicationId, guildId, command.getApplicationCommandRequest())
                            .subscribe();
                }
            }

        }
    }

    /**
     * Removes all present global slash commands
     *
     * @param client Discord4J discord client
     * @param applicationId bot application id
     */
    private void removeGlobalCommands(DiscordClient client, long  applicationId) {
        // Get registered commands from discord
        Map<String, ApplicationCommandData> globalCommands = client
                .getApplicationService()
                .getGlobalApplicationCommands(applicationId)
                .collectMap(ApplicationCommandData::name)
                .block();

        // Remove all discord commands in case one was removed
        if (globalCommands != null && !globalCommands.isEmpty()) {
            for (Map.Entry<String, ApplicationCommandData> mapEntry : globalCommands.entrySet()) {
                ApplicationCommandData commandData = mapEntry.getValue();
                client.getApplicationService()
                        .deleteGlobalApplicationCommand(applicationId, commandData.id().asLong())
                        .subscribe();

            }

            logger.info("global commands removed");
        }
    }

    /**
     * Remove all guild commands from guild IDs provided in config
     *
     * @param client Discord4J discord client
     * @param applicationId Bot application ID
     */
    private void removeGuildCommands(DiscordClient client, long  applicationId) {
        for (long guildId : config.getGuildIds()) {
            // Get registered commands from discord
            Map<String, ApplicationCommandData> globalCommands = client
                    .getApplicationService()
                    .getGuildApplicationCommands(applicationId, guildId)
                    .collectMap(ApplicationCommandData::name)
                    .block();

            // Remove all discord commands in case one was removed
            if (globalCommands != null && !globalCommands.isEmpty()) {
                for (Map.Entry<String, ApplicationCommandData> mapEntry : globalCommands.entrySet()) {
                    ApplicationCommandData commandData = mapEntry.getValue();
                    client.getApplicationService()
                            .deleteGlobalApplicationCommand(applicationId, commandData.id().asLong())
                            .subscribe();

                }

                logger.info("guild commands removed");
            }
        }
    }
}
