package bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private static final String PIXOO_BOT_TOKEN_ENV_VAR = "PIXOO_BOT_TOKEN";

    private static final String PIXOO_IP_ENV_VAR = "PIXOO_IP";

    private static final String PIXOO_GUILD_IDS_ENV_VAR = "PIXOO_GUILD_IDS";

    private static final String PIXOO_USE_GUILD_IDS_ENV_VAR = "PIXOO_USE_GUILD_IDS";

    private static final String PIXOO_PUBLISH_SLASH_ENV_VAR = "PIXOO_PUBLISH_SLASH";

    private String botToken;

    private static Config instance;

    public static String pixooIp;

    private final List<Long> guildIds;

    private boolean isPublishSlashCommands;

    private boolean useGuildIds;

    private Config() {
        Properties config = new Properties();
        String dir;
        try {
            dir = new File(BotMain.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                    .getParentFile().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            config.load(new FileInputStream(dir + "/config.properties"));
            botToken = config.getProperty("botToken", "");
            String botTokenEnv = System.getenv(PIXOO_BOT_TOKEN_ENV_VAR);
            if (botToken.isEmpty() && botTokenEnv != null && !botTokenEnv.isEmpty()) {
                botToken = botTokenEnv;
            }
            useGuildIds = Boolean.parseBoolean(config.getProperty("useGuildIds", "false"));
            String useGuildIdsEnv = System.getenv(PIXOO_USE_GUILD_IDS_ENV_VAR);
            if (useGuildIdsEnv != null && !useGuildIdsEnv.isEmpty()) {
                boolean useGuildCommands = Boolean.parseBoolean(useGuildIdsEnv);
                if (useGuildCommands != useGuildIds) {
                    useGuildIds = useGuildIds;
                }

            }

            // parse guild ids
            guildIds = new ArrayList<>();
            String guildIdsEnv = System.getenv(PIXOO_GUILD_IDS_ENV_VAR);
            String guildIdsCsv = config.getProperty("guildIds", "");

            if(!guildIdsCsv.isEmpty()) {
                for (String id : guildIdsCsv.split(",")) {
                    long guildId = Long.parseLong(id);
                    guildIds.add(guildId);
                }
            } else if(guildIdsEnv != null && !guildIdsEnv.isEmpty()) {
                for (String id : guildIdsEnv.split(",")) {
                    long guildId = Long.parseLong(id);
                    guildIds.add(guildId);
                }
            }

            String pixooIpEnv = System.getenv(PIXOO_IP_ENV_VAR);
            pixooIp = config.getProperty("pixooIp","");
            if (pixooIp.isEmpty() && pixooIpEnv != null && !pixooIpEnv.isEmpty()) {
                pixooIp = pixooIpEnv;
            }
            isPublishSlashCommands = Boolean.parseBoolean(config.getProperty("publishSlashCommands",
                    "false"));
            String isPublishEnv = System.getenv(PIXOO_PUBLISH_SLASH_ENV_VAR);
            if (isPublishEnv != null && !isPublishEnv.isEmpty()) {
                boolean isPublishSlashCommandsEnv = Boolean.parseBoolean(isPublishEnv);
                if (isPublishSlashCommandsEnv != isPublishSlashCommands) {
                    isPublishSlashCommands = isPublishSlashCommandsEnv;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Config getInstance() {
        if(instance == null) {
            instance = new Config();
        }

        return instance;
    }

    public String getBotToken() {
        return botToken;
    }

    public List<Long> getGuildIds() {
        return guildIds;
    }

    public boolean isPublishSlashCommands() {
        return isPublishSlashCommands;
    }

    public String getPixooRequestUrl() {
        return "http://" +  pixooIp + ":80/post";
    }

    public boolean isUseGuildIds() {
        return useGuildIds;
    }
}
