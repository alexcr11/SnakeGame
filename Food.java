import java.awt.*;
import java.util.Random;

public class Food {
    private int x;
    private int y;
    private final int UNIT_SIZE;
    private final int WIDTH;
    private final int HEIGHT;
    private Random random;

    public Food(int unitSize, int width, int height) {
        this.UNIT_SIZE = unitSize;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.random = new Random();
        generateNewPosition();
    }

    public void generateNewPosition() {
        x = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        y = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(250, 0, 0));
        g.fillOval(x, y, UNIT_SIZE, UNIT_SIZE);
    }

    public boolean isAtPosition(int checkX, int checkY) {
        return this.x == checkX && this.y == checkY;
    }
}
