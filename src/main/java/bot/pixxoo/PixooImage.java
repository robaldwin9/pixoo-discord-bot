package bot.pixxoo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

public class PixooImage {

    private static final Logger logger = LoggerFactory.getLogger(PixooImage.class);

    private BufferedImage originalImage;

    private BufferedImage scaled64x64Image;

    private String base64ImageString;

    private URL origionalImageUrl;

    public PixooImage(URL imageUrl) {
        try {
            origionalImageUrl = imageUrl;
            originalImage = ImageIO.read(imageUrl);
            writeImageToDisk(originalImage, System.currentTimeMillis() + "original.png");
            scaled64x64Image = createScaledImage(originalImage, 64, 64, true);
            writeImageToDisk(scaled64x64Image, System.currentTimeMillis() + "64x64.png");
            base64ImageString = convertToString(scaled64x64Image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage createScaledImage(BufferedImage originalImage, int width, int height, boolean preserveAlpha) {
        Image tmp = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage scaledImaged = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaledImaged.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return scaledImaged;
    }

    private String convertToString(BufferedImage image) {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(8000);
        String base64String = "";
        try {
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] bytes = baos.toByteArray();
            base64String  = Base64.getEncoder().encodeToString(bytes);
            baos.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return  base64String;
    }

    public String getBase64Image() {
        return base64ImageString;

    }

    public void writeImageToDisk(BufferedImage image, String fileName) {
        File outputfile = new File(fileName);
        try {
           boolean written = ImageIO.write(image, "png", outputfile);
           logger.info("{} file write success -> {}", fileName, written);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteExistingFile(String fileName) {
        File outputfile = new File(fileName);
        if (outputfile.exists()) {
            outputfile.delete();
        }
    }
}
