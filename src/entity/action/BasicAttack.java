package entity.action;

import entity.combatant.Combatant;
import entity.item.Item;
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
        return true; // Always available
    }

    @Override
    public TargetType getTargetType(Combatant actor) {
        return TargetType.SINGLE_ENEMY;
    }

    @Override
    public boolean requiresItemSelection() {
        return false;
    }

    @Override
    public void setSelectedItem(Item item) {
        // No-op: BasicAttack does not use items
    }

    @Override
    public String execute(Combatant actor, List<Combatant> targets) {
        if (targets.isEmpty()) return "No target selected!";

        Combatant target = targets.get(0);

        // Check smoke bomb
        if (target.hasSmokeBombInvulnerability()) {
            return actor.getName() + " attacks " + target.getName() +
                   " but Smoke Bomb negates all damage! " +
                   target.getName() + " HP: " + target.getHp() + "/" + target.getMaxHp();
        }

        int damage = Math.max(0, actor.getAttack() - target.getDefense());
        target.takeDamage(damage);

        StringBuilder sb = new StringBuilder();
        sb.append(actor.getName()).append(" attacks ").append(target.getName()).append("!");
        sb.append("\n  Damage dealt: ").append(damage);
        sb.append(" (").append(actor.getAttack()).append(" - ").append(target.getDefense()).append(")");
        sb.append(" | ").append(target.getName()).append(" HP: ").append(target.getHp()).append("/").append(target.getMaxHp());
        if (!target.isAlive()) {
            sb.append(" | ").append(target.getName()).append(" ELIMINATED!");
        }
        return sb.toString();
    }
}