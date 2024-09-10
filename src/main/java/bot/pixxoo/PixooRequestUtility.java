package bot.pixxoo;

import bot.Config;
import bot.pixxoo.image.PixooImage;
import bot.pixxoo.image.PixooSendAnimationRequest;
import bot.pixxoo.image.PixooSendResetPicId;
import bot.pixxoo.sound.PixooBuzzer;
import bot.pixxoo.text.PixooSendClearTextArea;
import bot.pixxoo.text.PixooSendTextRequest;
import bot.pixxoo.tools.PixooCountdownTool;
import bot.pixxoo.tools.PixooNoiseTool;
import bot.pixxoo.tools.PixooStopWatchTool;
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

    public static void startCountdown(int minutes, int seconds) {
        PixooCountdownTool countdown = new PixooCountdownTool();
        countdown.setStatus(1);
        countdown.setMinutes(minutes);
        countdown.setSeconds(seconds);
        sendHttpRequest(countdown.toJsonString());
    }

    public static void startStopWatch() {
        PixooStopWatchTool pixooStopWatch = new PixooStopWatchTool();
        pixooStopWatch.setStatus(1);
        sendHttpRequest(pixooStopWatch.toJsonString());

    }

    public static void sendBuzzerCommand() {
        sendHttpRequest(new PixooBuzzer().toJsonString());
    }

    public static void sendNoiseToolStart() {
        sendHttpRequest(new PixooNoiseTool().toJsonString());
    }

    public static void sendNoiseToolStop() {
        PixooNoiseTool pixooNoiseTool = new PixooNoiseTool();
        pixooNoiseTool.setNoiseToolStatus(0);
        sendHttpRequest(pixooNoiseTool.toJsonString());
    }

    public static void stopStopWatch() {
        PixooStopWatchTool pixooStopWatch = new PixooStopWatchTool();
        pixooStopWatch.setStatus(0);
        sendHttpRequest(pixooStopWatch.toJsonString());
    }

    public static void stopCountdown() {
        PixooCountdownTool countdown = new PixooCountdownTool();
        sendHttpRequest(countdown.toJsonString());
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
            PixooImage.writeImageToDisk(image.get64x64ScaledImage(), 512, 512);
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
                    PixooImage.writeImageToDisk(frames.get(0).get64x64ScaledImage(), 512, 512);
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
