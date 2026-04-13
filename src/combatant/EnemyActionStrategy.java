package entity.combatant;

import entity.action.Action;
import entity.action.BasicAttack;
import java.util.List;

public interface EnemyActionStrategy {
    Action chooseAction(Enemy enemy, List<Combatant> allies, List<Combatant> enemies);
}

class BasicAttackStrategy implements EnemyActionStrategy {
    @Override
    public Action chooseAction(Enemy enemy, List<Combatant> allies, List<Combatant> enemies) {
        Combatant target = enemies.stream()
                .filter(Combatant::isAlive)
                .findFirst()
                .orElse(null);

        if (target != null) {
            return new BasicAttack();
        }
        return null;
    }
}