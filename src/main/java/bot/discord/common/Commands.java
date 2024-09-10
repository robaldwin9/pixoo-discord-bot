package bot.discord.common;

import bot.discord.*;
import bot.discord.tool.StartCountdownToolCommand;
import bot.discord.tool.StartNoiseToolCommand;
import bot.discord.tool.StartStopWatchToolCommand;

import java.util.HashMap;

public class Commands {
    HashMap<String, Command> commands;

    private static Commands instance;

    private Commands() {
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

    public static Commands getInstance() {
        if(instance == null) {
            instance = new Commands();
        }

        return instance;
    }
}
