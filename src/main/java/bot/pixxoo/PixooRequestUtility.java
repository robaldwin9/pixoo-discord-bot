package bot.pixxoo;

import bot.BotApp;
import bot.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PixooRequestUtility {

    private static final Logger logger = LoggerFactory.getLogger(BotApp.class);

    public static void sendImage(String imageUrl) {
        try {
            sendResetPicId();
            URL imageURL = new URL(imageUrl);
            BufferedImage image = ImageIO.read(imageURL);
            PixooSendAnimationRequest pixooAnimation = new PixooSendAnimationRequest();
            pixooAnimation.setPicId(System.currentTimeMillis());
            pixooAnimation.setPicData(String.valueOf(image));
            String body = pixooAnimation.toJsonString();
            logger.info(body);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Config.getInstance().getPixooRequestUrl()))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();

            logger.info("request prepared: {}, {}", request, body);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info(response.statusCode() + response.toString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static void sendText(String text) {
        try {
            PixooSendTextRequest pixooText = new PixooSendTextRequest();
            String body = pixooText.toJsonString();
            logger.info(body);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Config.getInstance().getPixooRequestUrl()))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();

            logger.info("request prepared: {}, {}", request, body);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info(response.statusCode() + response.toString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendResetPicId() {
        try {
            PixooSendResetPicId pixooResetPicId = new PixooSendResetPicId();
            String body = pixooResetPicId.toJsonString();
            logger.info(body);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Config.getInstance().getPixooRequestUrl()))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();

            logger.info("request prepared: {}, {}", request, body);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info(response.statusCode() + response.toString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
