package entity.combatant;

import java.util.List;
import java.util.ArrayList;

public class Wizard extends Player {

    public Wizard() {
        super("Wizard", 200, 50, 10, 20);
    }

    @Override
    public String executeSpecialSkill(Combatant target, List<Combatant> allEnemies) {
        if (allEnemies == null || allEnemies.isEmpty()) {
            return "No enemies to blast!";
        }

        int kills = 0;
        StringBuilder result = new StringBuilder(getName() + " casts Arcane Blast!\n");

        List<Combatant> enemiesCopy = new ArrayList<>(allEnemies);

        for (Combatant enemy : enemiesCopy) {
            if (enemy.isAlive()) {
                int damage = Math.max(0, this.getAttack() - enemy.getDefense());
                enemy.takeDamage(this.getAttack());
                result.append(" Hits ").append(enemy.getName()).append(" for ").append(damage).append(" damage!\n");

                if (!enemy.isAlive()) {
                    kills++;
                    result.append(" ").append(enemy.getName()).append(" eliminated! +10 Attack!\n");
                }
            }
        }

        if (kills > 0) {
            this.addPermanentAttackBonus(kills * 10);
            result.append(" Wizard Attack increased by ").append(kills * 10).append("! New Attack: ").append(this.getAttack());
        }

        return result.toString();
    }
}