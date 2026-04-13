package entity.combatant.enemy;

/**
 * Wolf enemy type.
 * HP: 40, ATK: 45, DEF: 5, SPD: 35
 */
public class Wolf extends Enemy {

    private static int instanceCount = 0;

    public Wolf() {
        super("Wolf " + getNextLabel(), 40, 45, 5, 35);
    }

    public Wolf(String name) {
        super(name, 40, 45, 5, 35);
    }

    private static String getNextLabel() {
        instanceCount++;
        return String.valueOf((char) ('A' + instanceCount - 1));
    }

    /**
     * Reset the instance counter (call when starting a new game).
     */
    public static void resetCounter() {
        instanceCount = 0;
    }
}
