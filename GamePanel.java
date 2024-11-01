//Created By Alexander Crone
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    static final int WIDTH = 500;
    static final int HEIGHT = 500;
    static final int UNIT_SIZE = 20;
    static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    final int x[] = new int[NUMBER_OF_UNITS];
    final int y[] = new int[NUMBER_OF_UNITS];

    int length = 5;
    int foodEaten;
    int mostFoodEaten;
    int foodX;
    int foodY;
    int foodX2;
    int foodY2;
    char direction = 'D';
    boolean running = false;
    Random random;
    Timer timer;
    JButton playAgainButton;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.setDoubleBuffered(true); // Enable double buffering
        this.setLayout(null); // Use null layout for manual positioning

        // Create and add the "Play Again" button
        playAgainButton = new JButton("Play Again");
        playAgainButton.setBounds((WIDTH - 100) / 2, HEIGHT / 2 + 50, 100, 30); // Position and size
        playAgainButton.setFocusable(false);
        playAgainButton.addActionListener(e -> restartGame());
        playAgainButton.setVisible(false); // Hide button initially
        this.add(playAgainButton);

        play();
    }

    public void play() {
        addFood();
        running = true;
        direction = 'D';
        foodEaten = 0;
        length = 5;

        timer = new Timer(70, this);
        timer.start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void move() {
        for (int i = length; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (direction == 'L') {
            x[0] = x[0] - UNIT_SIZE;
        } else if (direction == 'R') {
            x[0] = x[0] + UNIT_SIZE;
        } else if (direction == 'U') {
            y[0] = y[0] - UNIT_SIZE;
        } else {
            y[0] = y[0] + UNIT_SIZE;
        }
    }

    public void checkFood() {
        // Check collision with the first food item
        if (x[0] == foodX && y[0] == foodY) {
            length++;
            foodEaten++;
            // Only reposition the first food
            foodX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
            foodY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        }

        // Check collision with the second food item
        if (x[0] == foodX2 && y[0] == foodY2) {
            length++;
            foodEaten++;
            // Only reposition the second food
            foodX2 = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
            foodY2 = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        }

        switch (foodEaten) {
            case 10:
                timer.setDelay(65);
                break;
            case 20:
                timer.setDelay(60);
                break;
            case 30:
                timer.setDelay(55);
            case 40:
                timer.setDelay(50);
            case 50:
                timer.setDelay(45);
                break;
        }
    }

    public void draw(Graphics graphics) {
        if (running) {
            // Draw first food item
            graphics.setColor(new Color(250, 0, 0));
            graphics.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            // Draw second food item
            graphics.setColor(new Color(210, 0, 20));
            graphics.fillOval(foodX2, foodY2, UNIT_SIZE, UNIT_SIZE);

            // Decide snake color based on foodEaten
            Color snakeHeadColor;
            Color snakeBodyColor;

            if (foodEaten >= 50) {
                // Hard mode with random colors for each frame
                snakeHeadColor = new Color(random.nextInt(55), random.nextInt(200), random.nextInt(100));
                snakeBodyColor = new Color(random.nextInt(55), random.nextInt(200), random.nextInt(150));
            } else {
                // Default snake colors
                snakeHeadColor = new Color(20, 227, 20);
                snakeBodyColor = new Color(20, 200, 20);
            }

            // Draw the snake
            for (int i = 0; i < length; i++) {
                graphics.setColor(i == 0 ? snakeHeadColor : snakeBodyColor);
                graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Display the score
            graphics.setColor(Color.white);
            graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2,
                    graphics.getFont().getSize());
        } else {
            gameOver(graphics);
        }
    }

    public void addFood() {
        foodX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;

        // Place the second food item at a different position
        do {
            foodX2 = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
            foodY2 = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        } while (foodX2 == foodX && foodY2 == foodY); // Ensures it doesn’t overlap with the first food
    }

    public void checkHit() {
        // Check collision with body
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        // Check collision with screen edges
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
            playAgainButton.setVisible(true); // Show "Play Again" button on game over
        }
    }

    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 50));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);

        graphics.setColor(Color.white);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2,
                graphics.getFont().getSize());

        // Update and display mostFoodEaten
        mostFoodEaten = Math.max(mostFoodEaten, foodEaten);
        graphics.drawString("Best Score: " + mostFoodEaten,
                (WIDTH - metrics.stringWidth("Best Score: " + mostFoodEaten)) / 2, HEIGHT / 3);
    }

    // Restart game method
    public void restartGame() {
        playAgainButton.setVisible(false); // Hide the "Play Again" button
        running = true;

        // Reset game state
        length = 5; // Reset the snake length to the initial size
        foodEaten = 0; // Reset score
        direction = 'D'; // Reset initial direction to down

        // Initialize snake's position at the top-left or starting position
        for (int i = 0; i < length; i++) {
            x[i] = UNIT_SIZE * (length - i); // Align the segments horizontally
            y[i] = 0; // Start on the top row
        }

        addFood(); // Place new food in a grid-aligned location
        timer.setDelay(80);
        timer.start(); // Restart the timer for the game
        repaint(); // Redraw the panel with the reset state
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (running) {
            move();
            checkFood();
            checkHit();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}