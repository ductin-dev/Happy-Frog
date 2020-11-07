package GameObject;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Frog {
    public Rectangle conllisionMask;
    public static Image image;

    public Frog(int x, int y, int width, int height) {
	conllisionMask = new Rectangle(x, y, width, height);
	if (image == null) {
	    try {
		// Lấy ảnh background
		image = ImageIO.read(new File("Resource/frog.png"));
	    } catch (IOException e) {
	    }
	}
    }

    public int jump(int gravity) {
	// Triệt tiêu trọng lực
	if (gravity > 0) {
	    gravity = 0;
	}
	// Nhảy
	gravity -= 15;
	return gravity;
    }

    public void fallDown(int gravity) {
	conllisionMask.y += gravity;
    }

}
