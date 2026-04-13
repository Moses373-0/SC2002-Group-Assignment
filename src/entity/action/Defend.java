package entity.action;

import entity.combatant.Combatant;
import entity.effect.DefendEffect;
import java.util.List;

/**
 * Defend action: increases defense by 10 for current round + next round.
 */
public class Defend implements Action {

  @Override
  public String getName() {
    return "Defend";
  }

  @Override
  public boolean isAvailable(Combatant actor) {
    return true; // Always available
  }

  @Override
  public String execute(Combatant actor, List<Combatant> targets) {
    actor.addEffect(new DefendEffect());
    return actor.getName() + " takes a defensive stance! Defense increased by 10 for 2 turns." +
        "\n  Current Defense: " + actor.getDefense();
  }
}
