package entity.combatant;

import java.util.ArrayList;
import java.util.List;

public enum Difficulty {
    EASY("Easy", 1,
            new SpawnWave(true, List.of(Enemy.EnemyType.GOBLIN, Enemy.EnemyType.GOBLIN, Enemy.EnemyType.GOBLIN)),
            null
    ),
    MEDIUM("Medium", 2,
            new SpawnWave(true, List.of(Enemy.EnemyType.GOBLIN, Enemy.EnemyType.WOLF)),
            new SpawnWave(false, List.of(Enemy.EnemyType.WOLF, Enemy.EnemyType.WOLF))
    ),
    HARD("Hard", 3,
            new SpawnWave(true, List.of(Enemy.EnemyType.GOBLIN, Enemy.EnemyType.GOBLIN)),
            new SpawnWave(false, List.of(Enemy.EnemyType.GOBLIN, Enemy.EnemyType.WOLF, Enemy.EnemyType.WOLF))
    );

    private final String displayName;
    private final int levelNumber;
    private final SpawnWave initialWave;
    private final SpawnWave backupWave;

    Difficulty(String displayName, int levelNumber, SpawnWave initial, SpawnWave backup) {
        this.displayName = displayName;
        this.levelNumber = levelNumber;
        this.initialWave = initial;
        this.backupWave = backup;
    }

    public String getDisplayName() { return displayName; }
    public int getLevelNumber() { return levelNumber; }
    public SpawnWave getInitialWave() { return initialWave; }
    public SpawnWave getBackupWave() { return backupWave; }
    public boolean hasBackupWave() { return backupWave != null; }

    @Override
    public String toString() {
        return displayName;
    }
}

class SpawnWave {
    private final boolean isInitial;
    private final List<Enemy.EnemyType> enemyTypes;

    public SpawnWave(boolean isInitial, List<Enemy.EnemyType> enemyTypes) {
        this.isInitial = isInitial;
        this.enemyTypes = new ArrayList<>(enemyTypes);
    }

    public boolean isInitial() { return isInitial; }
    public List<Enemy.EnemyType> getEnemyTypes() { return new ArrayList<>(enemyTypes); }

    public List<Enemy> spawn() {
        List<Enemy> enemies = new ArrayList<>();
        for (Enemy.EnemyType type : enemyTypes) {
            enemies.add(new Enemy(type));
        }
        return enemies;
    }
}