package entity.combatant;

import java.util.List;

public class Warrior extends Player {

    public Warrior() {
        super("Warrior", 260, 40, 20, 30);
    }

    @Override
    public String executeSpecialSkill(Combatant target, List<Combatant> allEnemies) {
        if (target == null) {
            return "No target for Shield Bash!";
        }

        int damage = Math.max(0, this.getAttack() - target.getDefense());
        target.takeDamage(this.getAttack());
        target.addStatusEffect(new StunEffect());

        return getName() + " uses Shield Bash on " + target.getName() +
                " for " + damage + " damage! " + target.getName() + " is STUNNED for 2 turns!";
    }
}