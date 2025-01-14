package breakout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Item {
    public int x, y, width, height, dx, dy;
}

class ControlWindow extends JPanel implements Runnable, KeyListener {
    public static final int WIDTH = 800, HEIGHT = 600;
    public static final int BRICK_X = 10;
    public static final int BRICK_Y = 6;
    public static final int BRICK_WIDTH = 62;
    public static final int BRICK_HEIGHT = 34;

    private Item player, ball;
    private Item bricks[];

    private Thread gameThread;
    private java.util.Random rand = new java.util.Random();

    private boolean gameOver = false, gameWin = false;

    private int amount = 0;
    private int score = 0;

    private void setup() {
        gameThread = new Thread(this);
        gameThread.start();

        player = new Item();
        player.width = 120;
        player.height = 22;
        player.x = WIDTH / 2 - player.width / 2;
        player.y = HEIGHT - player.height - 50;
        player.dx = 28;

        ball = new Item();
        ball.width = 34;
        ball.height = 34;
        ball.x = WIDTH / 2 - ball.width / 2;
        ball.y = HEIGHT / 2 - ball.height / 2 - 50;
        ball.dx = -4;
        ball.dy = 5;
        
        bricks = new Item[BRICK_X * BRICK_Y];
        for (int i = 0; i < BRICK_X; i++) {
            for (int j = 0; j < BRICK_Y; j++) {
                bricks[amount] = new Item();
                bricks[amount].x = i * BRICK_WIDTH + 82;
                bricks[amount].y = j * BRICK_HEIGHT + 30;
                amount++;
            }
        }
    }

    private void logic() {
        if (!gameOver && !gameWin) {
            ball.x += ball.dx;
            for (int i = 0; i < amount; i++) {
                if (new Rectangle(bricks[i].x, bricks[i].y, BRICK_WIDTH, BRICK_HEIGHT)
                        .intersects(new Rectangle(ball.x, ball.y, ball.width, ball.height))) {
                    ball.dx *= -1;
                    bricks[i].x = -100;
                    score++;
                }
            }

            ball.y += ball.dy;
            for (int i = 0; i < amount; i++) {
                if (new Rectangle(bricks[i].x, bricks[i].y, BRICK_WIDTH, BRICK_HEIGHT)
                        .intersects(new Rectangle(ball.x, ball.y, ball.width, ball.height))) {
                    ball.dy *= -1;
                    bricks[i].x = -100;
                    score++;
                }
            }

            if (ball.x < 0 || ball.x > (WIDTH - ball.width)) ball.dx *= -1;
            if (ball.y < 0) ball.dy *= -1;

            if (ball.y > HEIGHT - ball.height) {
                gameOver = true;
            }

            if (score == amount) {
                gameWin = true;
            }

            if (new Rectangle(player.x, player.y, player.width, player.height)
                    .intersects(new Rectangle(ball.x, ball.y, ball.width, ball.height))) {
                ball.dy = -Math.abs(ball.dy);
                ball.dx = rand.nextInt(5 - 2) + 2;
                ball.dx = rand.nextInt(2) == 1 ? ball.dx : -ball.dx;
            }
        }
    }

    @Override
    public void run() {
        long time1 = System.nanoTime(), time2;
        double delta = 0.0;
        double ticks = 60.0;
        double nanos = 1e9 / ticks;

        while (gameThread != null) {
            time2 = System.nanoTime();
            delta += (time2 - time1) / nanos;
            time1 = time2;

            if (delta >= 1) {
                logic();
                repaint();
                delta--;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLUE);
        g.fillRect(player.x, player.y, player.width, player.height);

        g.setColor(Color.RED);
        g.fillOval(ball.x, ball.y, ball.width, ball.height);

        for (int i = 0; i < amount; i++) {
            g.setColor(Color.GREEN);
            g.fillRect(bricks[i].x, bricks[i].y, BRICK_WIDTH, BRICK_HEIGHT);
            g.setColor(Color.BLACK);
            g.drawRect(bricks[i].x, bricks[i].y, BRICK_WIDTH + 1, BRICK_HEIGHT + 1);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.drawString("SCORE " + score, 0, 32);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("GAME OVER", WIDTH / 2 - 160, HEIGHT / 2);
        }

        if (gameWin) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("GAME WIN", WIDTH / 2 - 140, HEIGHT / 2);
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver && !gameWin) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) player.x -= player.dx;
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.x += player.dx;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public ControlWindow() {
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(Color.BLACK);
        this.addKeyListener(this);
        setup();
    }
}
public class newtest extends JFrame {
	private ControlWindow cw = new ControlWindow();
    public newtest() {
    	this.add(cw);
        this.pack();
        this.setTitle("Breakout");
        this.setSize(cw.WIDTH, cw.HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        this.setVisible(true);
    }
    public static void main(String[] args) {
        new newtest();
    }
}