package bot.commands;

import discord4j.core.object.command.ApplicationCommandOption;

public abstract class AbstractCommand implements Command {
    private String name;

    private String description;

    private int type;

    private boolean required;

    private String userInputDescription = "";

    public AbstractCommand() {
        setType(ApplicationCommandOption.Type.STRING.getValue());
        setRequired(true);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserInputDescription() {
        return userInputDescription.equals("") ? getName() : userInputDescription;
    }

    public void setUserInputDescription(String userInputDescription) {
        this.userInputDescription = userInputDescription;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
