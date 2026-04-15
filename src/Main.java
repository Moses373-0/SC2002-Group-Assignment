import controller.GameManager;

/**
 * Main entry point for the Turn-Based Combat Arena game.
 */
public class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.start();
    }
}
