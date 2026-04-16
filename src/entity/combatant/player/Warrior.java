package entity.combatant.player;
import entity.action.Action;
import entity.combatant.Combatant;
import entity.effect.StunEffect;
import java.util.List;
/**
 * Warrior player class.
 * Special Skill: Shield Bash — Deal BasicAttack damage to selected enemy.
 * Selected enemy is stunned for current turn and next turn.
 */
public class Warrior extends Player {
    public Warrior() {
        super("Warrior", 260, 40, 20, 30);
    }
    @Override
    public String executeSpecialSkill(List<Combatant> targets) {
        // Target selection is handled externally (by BattleEngine/GameUI).
        // This method is called with the selected target in targets list.
        if (targets.isEmpty()) return "No target selected!";
        Combatant target = targets.get(0);
        int damage = Math.max(0, this.getAttack() - target.getDefense());
        target.takeDamage(damage);
        // Apply stun for 2 turns (current + next)
        target.addEffect(new StunEffect(2));
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName()).append(" uses Shield Bash on ").append(target.getName()).append("!");
        sb.append("\n  Damage dealt: ").append(damage);
        sb.append(" | ").append(target.getName()).append(" HP: ").append(target.getHp()).append("/").append(target.getMaxHp());
        if (!target.isAlive()) {
            sb.append(" | ").append(target.getName()).append(" ELIMINATED!");
        } else {
            sb.append(" | ").append(target.getName()).append(" is STUNNED!");
        }
        return sb.toString();
    }
    @Override
    public String getSpecialSkillName() {
        return "Shield Bash";
    }
    @Override
    public Action.TargetType getSpecialSkillTargetType() {
        return Action.TargetType.SINGLE_ENEMY;
    }
}
