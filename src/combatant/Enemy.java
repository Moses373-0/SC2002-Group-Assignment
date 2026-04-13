package entity.combatant;

import java.util.List;

public class Enemy extends Combatant {
    private EnemyType enemyType;

    public enum EnemyType {
        GOBLIN("Goblin", 55, 35, 15, 25),
        WOLF("Wolf", 40, 45, 5, 35);

        private final String displayName;
        private final int baseHp;
        private final int baseAttack;
        private final int baseDefense;
        private final int baseSpeed;

        EnemyType(String displayName, int hp, int attack, int defense, int speed) {
            this.displayName = displayName;
            this.baseHp = hp;
            this.baseAttack = attack;
            this.baseDefense = defense;
            this.baseSpeed = speed;
        }

        public String getDisplayName() { return displayName; }
        public int getBaseHp() { return baseHp; }
        public int getBaseAttack() { return baseAttack; }
        public int getBaseDefense() { return baseDefense; }
        public int getBaseSpeed() { return baseSpeed; }
    }

    public Enemy(EnemyType type) {
        super(type.getDisplayName(), type.getBaseHp(), type.getBaseAttack(),
                type.getBaseDefense(), type.getBaseSpeed());
        this.enemyType = type;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    @Override
    public String getType() {
        return "Enemy";
    }

    @Override
    public String toString() {
        return String.format("%s (HP: %d/%d)", name, currentHp, maxHp);
    }
}