package bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private final String botToken;

    private static Config instance;

    public static String pixooIp;

    private final long guildId;

    private Config() {
        Properties config = new Properties();
        String dir = null;
        try {
            dir = new File(BotApp.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            config.load(new FileInputStream(dir + "/config.properties"));
            botToken = config.getProperty("botToken");
            guildId = Long.parseLong(config.getProperty("guildId"));
            pixooIp = config.getProperty("pixooIp");
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

    public long getGuildId() {
        return guildId;
    }

    public String getPixooRequestUrl() {
        return "http://" +  pixooIp + ":80/post";
    }
}
