package entity.combatant.enemy;

import entity.combatant.Combatant;
import java.util.List;

/**
 * Abstract class for enemy combatants.
 * Uses EnemyActionStrategy for behavior (DIP).
 */
public abstract class Enemy extends Combatant {
    private EnemyActionStrategy actionStrategy;

    public Enemy(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
        this.actionStrategy = new BasicAttackStrategy(); // default
    }

    public void setActionStrategy(EnemyActionStrategy strategy) {
        this.actionStrategy = strategy;
    }

    public EnemyActionStrategy getActionStrategy() {
        return actionStrategy;
    }

    /**
     * Perform this enemy's action during its turn.
     */
    public String performAction(List<Combatant> targets) {
        return actionStrategy.execute(this, targets);
    }
}
