package bot.pixxoo;

import bot.Config;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PixooRequestUtility {
    private static final Logger logger = LoggerFactory.getLogger(PixooRequestUtility.class);

    public static void sendImage(String imageUrl) {
        try {
            sendResetPicId();
            logger.error(imageUrl);
            PixooImage image = new PixooImage(new URL(imageUrl));
            PixooSendAnimationRequest pixooAnimation = new PixooSendAnimationRequest();
            pixooAnimation.setPicId(System.currentTimeMillis());
            pixooAnimation.setPicData(image.getBase64Image());
            sendHttpRequest(pixooAnimation.toJsonString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendText(String text) {
        sendClearTextArea();
        PixooSendTextRequest pixooText = new PixooSendTextRequest();
        sendHttpRequest(pixooText.toJsonString());
    }

    public static void sendResetPicId() {
        PixooSendResetPicId pixooResetPicId = new PixooSendResetPicId();
        sendHttpRequest(pixooResetPicId.toJsonString());
    }

    public static void sendClearTextArea() {
        PixooSendClearTextArea pixooSendClearTextArea = new PixooSendClearTextArea();
        sendHttpRequest(pixooSendClearTextArea.toJsonString());
    }

    private static void sendHttpRequest(String body) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Config.getInstance().getPixooRequestUrl()))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            PixooResponse pixooResponse = gson.fromJson(response.body(), PixooResponse.class);
            logger.info("Response from pixoo: {} {}", response.statusCode(), pixooResponse.getErrorCode());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
