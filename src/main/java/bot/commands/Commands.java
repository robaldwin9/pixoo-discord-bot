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
