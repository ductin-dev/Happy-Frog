package GameGUI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Background {
    public Rectangle conllisionMask;
    public static Image image;

    public Background(int x, int y, int width, int height) {
	conllisionMask = new Rectangle(x, y, width, height);
	if (image == null) {
	    try {
		// Lấy ảnh background
		image = ImageIO.read(new File("Resource/bg.png"));
	    } catch (IOException e) {
	    }
	}
    }

    public static void paintScroolBackground(Graphics g, Background background) {
	g.drawImage(image, background.conllisionMask.x, background.conllisionMask.y, background.conllisionMask.width,
	                background.conllisionMask.height, null);
    }
}
