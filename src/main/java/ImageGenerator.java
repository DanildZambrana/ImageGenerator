import renders.AbstractRender;
import renders.BackgroundColorRender;
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
        BufferedImage tmp = new BufferedImage(200, 100, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics2D = tmp.createGraphics();

        //Background
        Set<Setting> backgroundSettings = new HashSet<>();
        backgroundSettings.add(new Setting(Setting.Type.COLOR, new Color(0, 0, 0)));
        AbstractRender backgroundColorRender = new BackgroundColorRender(backgroundSettings, 200, 100);
        backgroundColorRender.renderImage(tmp, graphics2D);


        //Text
        Set<Setting> fontSettings = new HashSet<>();
        fontSettings.add(new Setting(Setting.Type.SIZE, 12));
        fontSettings.add(new Setting(Setting.Type.FONT, "Ubuntu"));
        fontSettings.add(new Setting(Setting.Type.TEXT, "Hello world in Image"));
        fontSettings.add(new Setting(Setting.Type.COLOR, new Color(100, 255, 10)));

        AbstractRender render = new TextRender(fontSettings, 0, 0, placeholderUtil, drawUtil);

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
