package entity.action;

import entity.combatant.Combatant;
import entity.combatant.player.Player;
import java.util.List;

/**
 * SpecialSkill action: executes the player's class-specific special ability.
 * Cooldown: 3 turns (including the current round).
 */
public class SpecialSkill implements Action {

  @Override
  public String getName() {
    return "Special Skill";
  }

  @Override
  public boolean isAvailable(Combatant actor) {
    if (actor instanceof Player) {
      return actor.getSpecialSkillCooldown() == 0;
    }
    return false;
  }

  @Override
  public String execute(Combatant actor, List<Combatant> targets) {
    if (!(actor instanceof Player)) {
      return "Only players can use special skills!";
    }
    Player player = (Player) actor;

    if (player.getSpecialSkillCooldown() > 0) {
      return "Special skill is on cooldown! (" + player.getSpecialSkillCooldown() + " turns remaining)";
    }

    // Set cooldown to 3 turns
    player.setSpecialSkillCooldown(3);

    // Execute the class-specific skill
    return player.executeSpecialSkill(targets);
  }
}
