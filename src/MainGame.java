import java.awt.Graphics;

import javax.swing.JPanel;

public class MainGame extends JPanel {
    public MainGame() {
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	try {
	    GameFrame.gameFrame.repaint(g);
	} catch (Exception e) {
	}
    }

}
