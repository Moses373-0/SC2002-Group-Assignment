package entity.level;

import entity.combatant.enemy.Enemy;
import entity.combatant.enemy.Goblin;
import entity.combatant.enemy.Wolf;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game level with initial and backup enemy spawns.
 */
public class Level {
    private Difficulty difficulty;
    private List<Enemy> initialSpawn;
    private List<Enemy> backupSpawn;
    private boolean backupTriggered;

    public Level(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.initialSpawn = new ArrayList<>();
        this.backupSpawn = new ArrayList<>();
        this.backupTriggered = false;

        // Reset counters for clean naming
        Goblin.resetCounter();
        Wolf.resetCounter();

        setupEnemies();
    }

    private void setupEnemies() {
        switch (difficulty) {
            case EASY:
                // 3 Goblins
                initialSpawn.add(new Goblin());
                initialSpawn.add(new Goblin());
                initialSpawn.add(new Goblin());
                break;
            case MEDIUM:
                // Initial: 1 Goblin + 1 Wolf
                initialSpawn.add(new Goblin());
                initialSpawn.add(new Wolf());
                // Backup: 2 Wolves
                backupSpawn.add(new Wolf());
                backupSpawn.add(new Wolf());
                break;
            case HARD:
                // Initial: 2 Goblins
                initialSpawn.add(new Goblin());
                initialSpawn.add(new Goblin());
                // Backup: 1 Goblin + 2 Wolves
                backupSpawn.add(new Goblin());
                backupSpawn.add(new Wolf());
                backupSpawn.add(new Wolf());
                break;
        }
    }

    public Difficulty getDifficulty() { return difficulty; }
    public List<Enemy> getInitialSpawn() { return initialSpawn; }
    public List<Enemy> getBackupSpawn() { return backupSpawn; }
    public boolean hasBackupSpawn() { return !backupSpawn.isEmpty(); }
    public boolean isBackupTriggered() { return backupTriggered; }

    /**
     * Trigger backup spawn. Returns the backup enemies.
     */
    public List<Enemy> triggerBackupSpawn() {
        backupTriggered = true;
        return backupSpawn;
    }
}
