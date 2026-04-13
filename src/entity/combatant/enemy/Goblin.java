package entity.combatant.enemy;

/**
 * Goblin enemy type.
 * HP: 55, ATK: 35, DEF: 15, SPD: 25
 */
public class Goblin extends Enemy {

    private static int instanceCount = 0;

    public Goblin() {
        super("Goblin " + getNextLabel(), 55, 35, 15, 25);
    }

    public Goblin(String name) {
        super(name, 55, 35, 15, 25);
    }

    private static String getNextLabel() {
        instanceCount++;
        // Convert to letter: 1->A, 2->B, etc.
        return String.valueOf((char) ('A' + instanceCount - 1));
    }

    /**
     * Reset the instance counter (call when starting a new game).
     */
    public static void resetCounter() {
        instanceCount = 0;
    }
}
