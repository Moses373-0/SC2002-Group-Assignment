package entity.combatant.player;

import entity.action.Action;
import entity.combatant.Combatant;
import entity.combatant.enemy.Enemy;
import java.util.List;

/**
 * Wizard player class.
 * Special Skill: Arcane Blast — Deal BasicAttack damage to ALL enemies.
 * Each enemy defeated adds +10 ATK lasting until end of the level.
 */
public class Wizard extends Player {

    public Wizard() {
        super("Wizard", 200, 50, 10, 20);
    }

    @Override
    public String executeSpecialSkill(List<Combatant> targets) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName()).append(" uses Arcane Blast on all enemies!");

        for (Combatant target : targets) {
            if (target instanceof Enemy && target.isAlive()) {
                int damage = Math.max(0, this.getAttack() - target.getDefense());
                target.takeDamage(damage);

                sb.append("\n  ").append(target.getName()).append(": damage dealt: ").append(damage);
                sb.append(" | HP: ").append(target.getHp()).append("/").append(target.getMaxHp());

                if (!target.isAlive()) {
                    // +10 ATK per kill, lasting until end of level
                    this.setBonusAttack(this.getBonusAttack() + 10);
                    sb.append(" | ELIMINATED! (Wizard ATK now ").append(this.getAttack()).append(")");
                }
            }
        }

        return sb.toString();
    }

    @Override
    public String getSpecialSkillName() {
        return "Arcane Blast";
    }

    @Override
    public Action.TargetType getSpecialSkillTargetType() {
        return Action.TargetType.ALL;
    }
}
