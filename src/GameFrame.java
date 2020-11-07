
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.Timer;

import GameGUI.Background;
import GameGUI.Floor;
import GameObject.Frog;
import GameObject.Pipe;

public class GameFrame implements ActionListener, MouseListener, KeyListener {

    public static GameFrame gameFrame;
    public final int WIDTH = 1000, HEIGHT = 800;
    Timer timer;
    public Font font = new Font("Bahnschrift", 1, 70);
    public MainGame game;
    public Random rand;

    // Đối tượng trong game
    public Frog frog;
    public ArrayList<Pipe> pipes;
    public ArrayList<Floor> floors;
    public ArrayList<Background> background;

    // Thông số
    public int gravity, score, maxScore = 0, mode, speed = 10;
    public boolean gameOver;
    public boolean started;
    public boolean inviciblity;
    public int scoreIncreasing = 1;
    public int space = 550;

    // GUI
    public Image bgImage, pipeImage1, pipeImage2, birdImage;
    private JComboBox<String> comboBox;
    private JButton btnStart;

    public GameFrame() {
	JFrame jframe = new JFrame();
	timer = new Timer(15, this);
	game = new MainGame();
	this.game.setBounds(0, 0, 1000, 800);
	rand = new Random();
	jframe.getContentPane().setLayout(null);

	jframe.getContentPane().add(game);
	this.game.setLayout(null);
	this.game.add(getComboBox());
	this.game.add(getBtnStart());
	jframe.setTitle("Happy Frog By DoanDucTin_SE1401");
	jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jframe.setSize(WIDTH, HEIGHT);
	jframe.addMouseListener(this);
	jframe.addKeyListener(this);
	jframe.setResizable(false);
	jframe.setVisible(true);

	// Init object
	updateObject();

	// Start game frame
	timer.start();
    }

    public void gameOver() {
	gameOver = true;
	maxScore = Math.max(maxScore, score);
	btnStart.setBackground(Color.orange);
	btnStart.setText("PLAY AGAIN");
	btnStart.setVisible(true);
	comboBox.setVisible(true);
    }

    public void updateObject() {
	// Lấy đối tượng cóc mới
	frog = new Frog(WIDTH / 2 - 25, HEIGHT / 2 - 25, 50, 50);
	// Lấy đối tượng cột mới
	if (pipes != null) pipes.clear();
	else {
	    pipes = new ArrayList<Pipe>();
	}
	// Lấy đối tượng đất mới
	if (floors != null) floors.clear();
	else {
	    floors = new ArrayList<Floor>();
	}
	// Lấy đối tượng nền mới
	if (background != null) background.clear();
	else {
	    background = new ArrayList<Background>();
	}

	// Thêm cột và đối tượng chuyển động mới vào list
	addContent(true);
	addContent(true);

	// Init
	gravity = 0;
	score = 0;
    }

    public void addContent(boolean isBegin) {

	// w,h của cột
	int width = 100;
	int height = 50 + rand.nextInt(300);
	// Thêm cột
	if (isBegin) {
	    int spaceBetweenTwoColumn = pipes.size() * 300;
	    // Cột dưới
	    pipes.add(new Pipe(WIDTH + width + spaceBetweenTwoColumn, HEIGHT - height - 120, width, height, 1));
	    // Cột trên
	    pipes.add(new Pipe(WIDTH + width + spaceBetweenTwoColumn, 0, width, HEIGHT - height - space, 2));
	    // Thêm đất
	    floors.add(new Floor(0, HEIGHT - 120, WIDTH, 120));
	    floors.add(new Floor(WIDTH, HEIGHT - 120, WIDTH, 120));
	    // Thêm nền
	    background.add(new Background(0, 0, WIDTH, HEIGHT));
	    background.add(new Background(WIDTH, 0, WIDTH, HEIGHT));
	} else {
	    int spaceBetweenTwoColumn = 2 * 300;
	    // Cột dưới
	    pipes.add(new Pipe(pipes.get(pipes.size() - 1).conllisionMask.x + spaceBetweenTwoColumn, HEIGHT - height - 120, width,
	                    height, 1));
	    // Cột trên
	    pipes.add(new Pipe(pipes.get(pipes.size() - 1).conllisionMask.x, 0, width, HEIGHT - height - space, 2));
	}

    }

    @Override
    public void actionPerformed(ActionEvent e) {

	if (started) {

	    // Di chuyển cột
	    for (int i = 0; i < pipes.size(); i++) {
		Pipe pipe = pipes.get(i);
		pipe.conllisionMask.x -= speed;
	    }
	    for (int i = 0; i < pipes.size(); i++) {
		Pipe pipe = pipes.get(i);
		if (pipe.conllisionMask.x + pipe.conllisionMask.width < 0) {
		    // Nếu cột ra ngoài viền thì xóa cột
		    pipes.remove(pipe);
		    // Khi game kết thúc, vẫn tiếp tục chạy màn hình
		    if (pipe.conllisionMask.y == 0) {
			addContent(false);
		    }
		}
	    }

	    // Di chuyển đất
	    for (int i = 0; i < floors.size(); i++) {
		Floor floor = floors.get(i);
		floor.conllisionMask.x -= speed;
	    }
	    for (int i = 0; i < floors.size(); i++) {
		Floor floor = floors.get(i);
		if (floor.conllisionMask.x + floor.conllisionMask.width < 0) {
		    floors.remove(floor);
		    floors.add(new Floor(WIDTH - 10, HEIGHT - 120, WIDTH + 10, 120));
		}
	    }

	    // Di chuyển nền
	    for (int i = 0; i < background.size(); i++) {
		Background bg = background.get(i);
		bg.conllisionMask.x -= speed - speed / 2 + 1;
	    }
	    for (int i = 0; i < background.size(); i++) {
		Background bg = background.get(i);
		if (bg.conllisionMask.x + bg.conllisionMask.width < 0) {
		    background.remove(bg);
		    background.add(new Background(WIDTH - 10, 0, WIDTH + 10, HEIGHT));
		}
	    }

	    // Trọng lực
	    if (gravity < 15) gravity += 1;
	    frog.fallDown(gravity);

	    for (Pipe pipe : pipes) {
		// Tính điểm
		if (pipe.conllisionMask.y == 0
		                && frog.conllisionMask.x + frog.conllisionMask.width / 2 > pipe.conllisionMask.x
		                                + pipe.conllisionMask.width / 2 - 10
		                && frog.conllisionMask.x + frog.conllisionMask.width / 2 < pipe.conllisionMask.x
		                                + pipe.conllisionMask.width / 2 + 10) {
		    score += scoreIncreasing;
		}

		// Kiểm tra đụng chạm
		if (!inviciblity) {

		    // Đụng cột
		    if (pipe.conllisionMask.intersects(frog.conllisionMask)) {
			gameOver();

			// Hất con cóc theo chiều ngang của cột
			if (frog.conllisionMask.x <= pipe.conllisionMask.x) {
			    frog.conllisionMask.x = pipe.conllisionMask.x - frog.conllisionMask.width;

			} else {
			    // Hất con cóc theo chiều dọc của cột
			    if (pipe.conllisionMask.y != 0) {
				frog.conllisionMask.y = pipe.conllisionMask.y - frog.conllisionMask.height;
			    } else if (frog.conllisionMask.y < pipe.conllisionMask.height) {
				frog.conllisionMask.y = pipe.conllisionMask.height;
			    }
			}
		    }

		    // Cóc đụng đất
		    if (frog.conllisionMask.y > HEIGHT - 117 - frog.conllisionMask.height) {
			frog.conllisionMask.y = HEIGHT - 117 - frog.conllisionMask.height;
			gameOver();
		    }

		    // Cóc nhảy quá cao
		    if (frog.conllisionMask.y < -100) {
			gameOver();
		    }
		} else {
		    if (frog.conllisionMask.y > HEIGHT - 117 - frog.conllisionMask.height) {
			frog.conllisionMask.y = HEIGHT - 117 - frog.conllisionMask.height;
		    }
		    if (frog.conllisionMask.y < -100) {
			frog.conllisionMask.y = -100;
		    }
		}
	    }
	}

	game.repaint();
    }

    public void repaint(Graphics g) {
	// Vẽ nền
	for (Background i : background) {
	    Background.paintScroolBackground(g, i);
	}

	// Vẽ cỏ
	for (Floor floor : floors) {
	    Floor.paintFloor(g, floor);
	}

	// Vẽ cóc
	g.drawImage(Frog.image, frog.conllisionMask.x, frog.conllisionMask.y, frog.conllisionMask.width,
	                frog.conllisionMask.height, null);

	// Vẽ cột
	for (Pipe pipe : pipes) {
	    Pipe.paintPipe(g, pipe);
	}

	// Hiển thị thông báo
	g.setFont(font);
	if (!started) {
	    g.setColor(Color.green);
	    g.drawString("Welcome to Happy Frog", 83, 150);
	}
	if (gameOver) {
	    g.setColor(Color.red);
	    g.drawString("You lose", 345, 130);
	    g.setFont(new Font("Bahnschrift", 1, 21));
	    g.drawString("You final score is: " + score, 380, 165);
	    g.setColor(Color.yellow);
	    g.drawString("Max score is: " + maxScore, 417, 200);
	}
	if (!gameOver && started) {
	    g.setColor(Color.white);
	    g.drawString(String.valueOf(score), 486, 100);
	}
    }

    public static void main(String[] args) {
	gameFrame = new GameFrame();
    }

    @Override
    public void mousePressed(MouseEvent e) {
	if (!gameOver) {
	    gravity = frog.jump(gravity);
	}
    }

    @Override
    public void keyPressed(KeyEvent e) {
	if (e.getKeyCode() == KeyEvent.VK_SPACE) {
	    if (!gameOver) {
		gravity = frog.jump(gravity);
	    }
	}
    }

    public JComboBox<String> getComboBox() {
	if (comboBox == null) {
	    comboBox = new JComboBox<>();
	    comboBox.setBackground(Color.ORANGE);
	    comboBox.setFont(new Font("Tahoma", Font.PLAIN, 19));
	    comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Easy", "Medium", "Hard", "Cheat"}));
	    comboBox.setBounds(398, 292, 170, 38);
	}
	return comboBox;
    }

    public JButton getBtnStart() {
	if (btnStart == null) {
	    btnStart = new JButton("START");
	    btnStart.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    btnStartActionPerformed(e);
		}
	    });
	    btnStart.setForeground(Color.WHITE);
	    btnStart.setBackground(Color.GREEN);
	    btnStart.setFont(new Font("Tahoma", Font.BOLD, 20));
	    btnStart.setBounds(342, 220, 281, 62);
	}
	return btnStart;
    }

    protected void btnStartActionPerformed(ActionEvent e) {
	// Lấy thông số cho game mới
	int mode = comboBox.getSelectedIndex();
	switch (mode) {
	case 0:
	    space = 550;
	    break;
	case 1:
	    space = 360;
	    break;
	case 2:
	    space = 280;
	    break;
	case 3:
	    timer.setDelay(1);
	    inviciblity = true;
	    scoreIncreasing = 34;
	    speed = 50;
	    break;
	}
	// Nếu game đã kết thúc mà nhấn thì tạo game mới
	if (gameOver) {
	    gameOver = false;
	    updateObject();
	}
	started = true;
	btnStart.setVisible(false);
	comboBox.setVisible(false);
    }

    // NOT USED =======================================================
    @Override
    public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
	// TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
	// TODO Auto-generated method stub

    }
}
