package entity.action;

import entity.combatant.Combatant;
import java.util.List;

/**
 * BasicAttack action: attack a single target.
 * Damage = max(0, Attacker ATK - Target DEF).
 */
public class BasicAttack implements Action {

    @Override
    public String getName() {
        return "Basic Attack";
    }

    @Override
    public boolean isAvailable(Combatant actor) {
        return true;
    }

    @Override
    public String execute(Combatant actor, List<Combatant> targets) {
        if (targets.isEmpty()) {
            return "No target selected!";
        }

        Combatant target = targets.get(0);

        // Check smoke bomb invulnerability
        if (target.hasSmokeBombInvulnerability()) {
            return actor.getName() + " attacks " + target.getName() +
                    " but Smoke Bomb negates all damage! " +
                    target.getName() + " HP: " + target.getCurrentHp() +
                    "/" + target.getMaxHp();
        }

        int damage = Math.max(0, actor.getAttack() - target.getDefense());
        target.takeDamage(damage);

        return actor.getName() + " attacks " + target.getName() +
                " for " + damage + " damage! " +
                target.getName() + " HP: " + target.getCurrentHp() +
                "/" + target.getMaxHp();
    }
}