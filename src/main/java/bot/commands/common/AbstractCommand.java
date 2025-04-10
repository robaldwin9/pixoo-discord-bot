package bot.commands.common;

import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import discord4j.discordjson.possible.Possible;

public abstract class AbstractCommand implements Command {
    private String name;

    private String description;

    private int type;

    private Possible<Boolean> required;

    private String userInputDescription = "";

    private ApplicationCommandRequest applicationCommandRequest;

    public AbstractCommand() {
        setType(ApplicationCommandOption.Type.STRING.getValue());
        setRequired(Possible.of(true));
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
        return userInputDescription.isEmpty() ? getName() : userInputDescription;
    }

    public void setUserInputDescription(String userInputDescription) {
        this.userInputDescription = userInputDescription;
    }

    protected ApplicationCommandRequest createApplicationCommandRequest() {
        ImmutableApplicationCommandRequest.Builder commandBuilder = ApplicationCommandRequest.builder();
        commandBuilder.name(getName().toLowerCase());
        commandBuilder.description(getDescription().toLowerCase());
        if(isRequired().get()) {
            commandBuilder.addOption(ApplicationCommandOptionData.builder()
                    .name(getUserInputDescription())
                    .description(getDescription().toLowerCase())
                    .type(getType())
                    .required(isRequired())
                    .build());
        }

        return commandBuilder.build();
    }

    public ApplicationCommandRequest getApplicationCommandRequest() {
        if (applicationCommandRequest == null) {
            applicationCommandRequest = createApplicationCommandRequest();
        }

        return applicationCommandRequest;
    }

    public Possible<Boolean> isRequired() {
        return required;
    }

    public void setRequired(Possible<Boolean> required) {
        this.required = required;
    }
}
