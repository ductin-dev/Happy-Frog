package GameGUI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Floor {
    public Rectangle conllisionMask;
    public static Image image;

    public Floor(int x, int y, int width, int height) {
	conllisionMask = new Rectangle(x, y, width, height);
	if (image == null) {
	    try {
		// Lấy ảnh background
		image = ImageIO.read(new File("Resource/floor.png"));
	    } catch (IOException e) {
	    }
	}
    }

    public static void paintFloor(Graphics g, Floor floor) {
	g.drawImage(image, floor.conllisionMask.x, floor.conllisionMask.y, floor.conllisionMask.width,
	                floor.conllisionMask.height, null);
    }
}
