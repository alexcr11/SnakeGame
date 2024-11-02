//Created By Alexander Crone
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int UNIT_SIZE = 20;

    private final Food food;
    private final Food food2;

    static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    final int x[] = new int[NUMBER_OF_UNITS];
    final int y[] = new int[NUMBER_OF_UNITS];

    int length = 5;
    int foodEaten;
    int mostFoodEaten;
    char direction = 'D';
    boolean running = false;
    Random random;
    Timer timer;
    JButton playAgainButton;
    JButton mainMenuButton;
    JButton quitButton;
    JButton playButton;
    JButton settingsButton;

    boolean showMainMenu = true;

    GamePanel() {
        random = new Random();
        food = new Food(UNIT_SIZE, WIDTH, HEIGHT);  // Initialize Food with unit size and screen dimensions
        food2 = new Food(UNIT_SIZE, WIDTH, HEIGHT);  // Initialize Food with unit size and screen dimensions
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
        playAgainButton.setVisible(false); // Hide button initially// Create and add the "Play Again" button

        mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setBounds((WIDTH - 100) / 2, HEIGHT / 2 + 90, 100, 30); // Position and size
        mainMenuButton.setFocusable(false);
        mainMenuButton.addActionListener(e -> gameMenu());
        mainMenuButton.setVisible(false); // Hide button initially

        quitButton = new JButton("Quit");
        quitButton.setBounds((WIDTH - 100) / 2, HEIGHT / 2 + 170, 100, 30); // Position and size
        quitButton.setFocusable(false);
        quitButton.addActionListener(e -> quit());
        quitButton.setVisible(true); // Hide button initially

        playButton = new JButton("Play Game");
        playButton.setBounds((WIDTH - 100) / 2, HEIGHT / 2 + 90, 100, 30); // Position and size
        playButton.setFocusable(false);
        playButton.addActionListener(e -> play());
        playButton.setVisible(true);

        settingsButton = new JButton("Settings");
        settingsButton.setBounds((WIDTH - 100) / 2, HEIGHT / 2 + 130, 100, 30); // Position and size
        settingsButton.setFocusable(false);
        settingsButton.addActionListener(e -> settings());
        settingsButton.setVisible(true);

        this.add(playAgainButton);
        this.add(mainMenuButton);
        this.add(quitButton);
        this.add(playButton);
        this.add(settingsButton);
    }

    public void play() {
         // Start the game and hide the main menu
        showMainMenu = false;
        playButton.setVisible(false);
        quitButton.setVisible(false);
        // Initialize snake's position at the top-left or starting position
        for (int i = 0; i < length; i++) {
            x[i] = UNIT_SIZE * (length - i); // Align the segments horizontally
            y[i] = 0; // Start on the top row
        }
        addFood();
        running = true;
        direction = 'D';
        foodEaten = 0;
        length = 5;

        timer = new Timer(70, this);
        timer.start();
    }

    public void settings() {
        showMainMenu = false;
        playButton.setVisible(false);
        settingsButton.setVisible(false);
        quitButton.setVisible(false);
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (showMainMenu) {
            // Display the main menu screen
            graphics.setColor(Color.GREEN);
            graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 50));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Snake Game", (WIDTH - metrics.stringWidth("Snake Game")) / 2, HEIGHT / 2);
        } else if (running) {
            // Draw game elements if running
            draw(graphics);
        } else {
            // Show game over screen if the game is not running
            gameOver(graphics);
        }
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
        if (food.isAtPosition(x[0], y[0])) {
            length++;
            foodEaten++;
            food.generateNewPosition();  // Generate a new position when food is eaten
        }

        // Check collision with the second food item
        if (food2.isAtPosition(x[0], y[0])) {
            length++;
            foodEaten++;
            food2.generateNewPosition(); // Generate a new position when food is eaten
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
            graphics.fillOval(food.getX(), food.getY(), UNIT_SIZE, UNIT_SIZE);

            // Draw second food item
            graphics.setColor(new Color(250, 0, 0));
            graphics.fillOval(food2.getX(), food2.getY(), UNIT_SIZE, UNIT_SIZE);

            // Decide snake color based on foodEaten
            Color snakeHeadColor;
            Color snakeBodyColor;

            if (foodEaten >= 50) {
                // Hard mode with random colors after eating 50 food
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
        food.generateNewPosition();

        // Place the second food item at a different position
        do {
            food2.generateNewPosition();
        } while (food2.getX() == food.getX() && food.getY() == food2.getY()); // Ensures it doesn’t overlap with the first food
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
            mainMenuButton.setVisible(true);
            quitButton.setVisible(true);
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
        mainMenuButton.setVisible(false); // Hide the "main menu" button
        quitButton.setVisible(false); // Hide the "quit" button
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

    public void gameMenu() {
        // Hide all game-related buttons
        playAgainButton.setVisible(false);
        mainMenuButton.setVisible(false);
        quitButton.setVisible(true);
        playButton.setVisible(true);

        // Set states to show the main menu
        running = false;
        showMainMenu = true;

        repaint(); // Refresh the screen
    }

    public void quit() {
        System.exit(0);
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