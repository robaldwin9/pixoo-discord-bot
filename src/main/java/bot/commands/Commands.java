package bot.commands;

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

        StartCountdownCommand startCountdownCommand = new StartCountdownCommand();
        commands.put(startCountdownCommand.getName(), startCountdownCommand);

        StartStopWatchCommand startStopWatchCommand = new StartStopWatchCommand();
        commands.put(startStopWatchCommand.getName(), startStopWatchCommand);

        BuzzerCommand buzzerCommand = new BuzzerCommand();
        commands.put(buzzerCommand.getName(), buzzerCommand);
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
