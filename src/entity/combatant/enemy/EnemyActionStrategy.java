package entity.combatant.enemy;

import entity.combatant.Combatant;
import java.util.List;

/**
 * Strategy interface for enemy action behavior.
 * Demonstrates DIP/OCP: extensible for future AI strategies.
 */
public interface EnemyActionStrategy {
    /**
     * Execute action for the given enemy against the provided targets.
     * @param enemy the enemy performing the action
     * @param targets all combatants in battle
     * @return description of what happened
     */
    String execute(Enemy enemy, List<Combatant> targets);
}
