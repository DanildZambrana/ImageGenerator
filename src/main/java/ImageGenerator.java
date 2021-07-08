import renders.AbstractRender;
import renders.Setting;
import renders.TextRender;
import utils.DrawUtil;
import utils.PlaceholderUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ImageGenerator {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        PlaceholderUtil placeholderUtil = new PlaceholderUtil(new HashMap<>());
        DrawUtil drawUtil = new DrawUtil();
        Set<Setting> settings = new HashSet<>();
        settings.add(new Setting(Setting.Type.SIZE, 12));
        settings.add(new Setting(Setting.Type.FONT, "Ubuntu"));
        settings.add(new Setting(Setting.Type.COLOR, new Color(100, 255, 10)));

        AbstractRender render = new TextRender(settings, 200, 200, placeholderUtil, drawUtil);

        BufferedImage tmp = new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics2D = tmp.createGraphics();

        render.renderImage(tmp, graphics2D);

        File file = new File("test-image.png");
        try {
            ImageIO.write(tmp, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println((System.currentTimeMillis() - start) / 1000);
    }
}
