package com.turnbased.game.core;

import java.util.List;

/**
 * Decides what an enemy does on its turn.
 * Right now enemies only basic attack, but we can add more strategies later.
 */
public interface EnemyActionStrategy {
    Action chooseAction(Enemy enemy, List<Combatant> allies, List<Combatant> enemies);
}

/**
 * The basic strategy - just pick a living player and hit them.
 */
class BasicAttackStrategy implements EnemyActionStrategy {
    @Override
    public Action chooseAction(Enemy enemy, List<Combatant> allies, List<Combatant> enemies) {
        // Find the first alive player to attack
        Combatant target = enemies.stream()
                .filter(Combatant::isAlive)
                .findFirst()
                .orElse(null);

        if (target != null) {
            return new BasicAttack(enemy, target);
        }
        return null;
    }
}