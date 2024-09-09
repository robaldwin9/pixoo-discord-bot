package bot.pixxoo;

import bot.Config;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

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
            sendHttpRequestAsync(pixooAnimation.toJsonString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendText(String text) {
        sendClearTextArea();
        PixooSendTextRequest pixooText = new PixooSendTextRequest();
        pixooText.setTextString(text);
        logger.error(pixooText.toJsonString());
        sendHttpRequestAsync(pixooText.toJsonString());
    }

    public static void sendResetPicId() {
        PixooSendResetPicId pixooResetPicId = new PixooSendResetPicId();
        sendHttpRequestSync(pixooResetPicId.toJsonString());
    }

    public static void sendClearTextArea() {
        PixooSendClearTextArea pixooSendClearTextArea = new PixooSendClearTextArea();
    }

    public static void sendGif(String url) {
        sendResetPicId();
        long picId = System.currentTimeMillis();
        try {
            ArrayList<PixooImage> frames = PixooImage.getGifFrames(new URL(url));
            logger.error("gif frames: {}", frames.size());

            Thread thread = new Thread(() -> {
                if (!frames.isEmpty()) {
                    for (int i = 0; i < frames.size(); i++) {
                        PixooImage currentFrame = frames.get(i);
                        PixooSendAnimationRequest pixooSendAnimationRequest = new PixooSendAnimationRequest();
                        pixooSendAnimationRequest.setPicNum(frames.size());
                        pixooSendAnimationRequest.setPicId(picId);
                        pixooSendAnimationRequest.setPicOffset(i);
                        pixooSendAnimationRequest.setPicData(currentFrame.getBase64Image());
                        sendHttpRequestSync(pixooSendAnimationRequest.toJsonString());
                    }
                }
            });
            thread.start();
//aq
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendHttpRequestAsync(String body) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.getInstance().getPixooRequestUrl()))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(res -> {
                    System.out.println("Thread is: " + Thread.currentThread().getName());
                    Gson gson = new Gson();
                    PixooResponse pixooResponse = gson.fromJson(res, PixooResponse.class);
                    logger.info("Response from pixoo: {} {}", res, pixooResponse.getErrorCode());
                });

    }


    private static void sendHttpRequestSync(String body) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.getInstance().getPixooRequestUrl()))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        try {
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("response: {}", response.toString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}
