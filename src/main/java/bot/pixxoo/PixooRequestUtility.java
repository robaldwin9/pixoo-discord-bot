package bot.pixxoo;

import bot.Config;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class PixooRequestUtility {
    private static final Logger logger = LoggerFactory.getLogger(PixooRequestUtility.class);

    public static void sendResetPicId() {
        PixooSendResetPicId pixooResetPicId = new PixooSendResetPicId();
        sendHttpRequest(pixooResetPicId.toJsonString());
    }

    public static void sendClearTextArea() {
        PixooSendClearTextArea pixooSendClearTextArea = new PixooSendClearTextArea();
        sendHttpRequest(pixooSendClearTextArea.toJsonString());
    }

    public static void sendText(String text) {
        sendClearTextArea();
        PixooSendTextRequest pixooText = new PixooSendTextRequest();
        pixooText.setTextString(text);
        sendHttpRequest(pixooText.toJsonString());
    }

    public static void sendSingleFrameImage(String imageUrl) {
        try {
            sendResetPicId();
            PixooImage image = new PixooImage(new URI(imageUrl));
            PixooSendAnimationRequest pixooAnimation = new PixooSendAnimationRequest();
            pixooAnimation.setPicId(System.currentTimeMillis());
            pixooAnimation.setPicData(image.getBase64Image());
            sendHttpRequest(pixooAnimation.toJsonString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendGif(String url) {
        sendResetPicId();
        long picId = System.currentTimeMillis();
        try {
            ArrayList<PixooImage> frames = PixooImage.createGifFrames(new URI(url));
            logger.info("gif frames: {}", frames.size());
            PixooSendAnimationRequest pixooSendAnimationRequest = new PixooSendAnimationRequest();
            int animationFrames = Math.min(frames.size(), 60);
            pixooSendAnimationRequest.setPicNum(animationFrames);
            pixooSendAnimationRequest.setPicId(picId);
            Thread thread = new Thread(() -> {
                if (!frames.isEmpty()) {
                    for (int i = 0; i < animationFrames; i++) {
                        PixooImage currentFrame = frames.get(i);
                        pixooSendAnimationRequest.setPicOffset(i);
                        pixooSendAnimationRequest.setPicData(currentFrame.getBase64Image());
                        sendHttpRequest(pixooSendAnimationRequest.toJsonString());
                    }
                }
            });
            thread.start();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendHttpRequest(String body) {
        try {
            HttpPost post = new HttpPost(Config.getInstance().getPixooRequestUrl());
            final StringEntity entity = new StringEntity(body);
            post.setEntity(entity);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpResponse response = client.execute(post);
                logger.info("{}", response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
