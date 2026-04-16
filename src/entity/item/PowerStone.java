package entity.item;

import entity.combatant.Combatant;
import entity.combatant.player.Player;
import java.util.List;

/**
 * Power Stone item: triggers the special skill effect once
 * without starting or changing the cooldown timer.
 */
public class PowerStone implements Item {

    @Override
    public String getName() {
        return "Power Stone";
    }

    @Override
    public String use(Player player, List<Combatant> allCombatants) {
        // Save and restore cooldown so Power Stone doesn't affect it
        int savedCooldown = player.getSpecialSkillCooldown();
        String result = player.executeSpecialSkill(allCombatants);
        player.setSpecialSkillCooldown(savedCooldown);
        return "Power Stone triggers " + player.getSpecialSkillName() + "!\n  " + result;
    }
}
