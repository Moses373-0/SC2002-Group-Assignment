package entity.action;

import entity.combatant.Combatant;
import entity.combatant.Player;
import java.util.List;

public class SpecialSkillAction implements Action {

    @Override
    public String getName() {
        return "Special Skill";
    }

    @Override
    public boolean isAvailable(Combatant actor) {
        if (actor instanceof Player) {
            return actor.getSpecialCooldown() == 0;
        }
        return false;
    }

    @Override
    public String execute(Combatant actor, List<Combatant> targets) {
        if (!(actor instanceof Player)) {
            return "Only players can use special skills!";
        }

        Player player = (Player) actor;

        if (player.getSpecialCooldown() > 0) {
            return "Special skill needs " + player.getSpecialCooldown() + " more turns.";
        }

        Combatant target = targets.isEmpty() ? null : targets.get(0);
        List<Combatant> allEnemies = targets;

        String result = player.executeSpecialSkill(target, allEnemies);
        player.setSpecialCooldown(3);

        return result;
    }
}