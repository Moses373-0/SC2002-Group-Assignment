package entity.level;

/**
 * Enum representing difficulty levels.
 */
public enum Difficulty {
    EASY("Easy", 1),
    MEDIUM("Medium", 2),
    HARD("Hard", 3);

    private final String displayName;
    private final int levelNumber;

    Difficulty(String displayName, int levelNumber) {
        this.displayName = displayName;
        this.levelNumber = levelNumber;
    }

    public String getDisplayName() { return displayName; }
    public int getLevelNumber() { return levelNumber; }

    @Override
    public String toString() { return displayName; }
}
