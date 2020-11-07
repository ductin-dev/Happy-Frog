package GameObject;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author ADMIN
 *
 * 
 */

public class Pipe {

    public static Image image1;
    public static Image image2;
    public Rectangle conllisionMask;
    public int direction;
    public Image image;

    public Pipe(int x, int y, int width, int height, int direction) {
	// Init
	conllisionMask = new Rectangle(x, y, width, height);
	this.direction = direction;
	// Get image
	try {
	    if (image1 == null) image1 = ImageIO.read(new File("Resource/pipe1.png"));
	    if (image2 == null) image2 = ImageIO.read(new File("Resource/pipe2.png"));
	} catch (IOException e) {
	}
	getImage();
    }

    public static void paintPipe(Graphics g, Pipe pipe) {
	g.drawImage(pipe.image, pipe.conllisionMask.x, pipe.conllisionMask.y, pipe.conllisionMask.width,
	                pipe.conllisionMask.height, null);
    }

    private void getImage() {
	if (direction == 1) {
	    image = image1;
	} else {
	    image = image2;
	}
    }
}
