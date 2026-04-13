package entity.item;

import entity.combatant.Combatant;
import entity.combatant.player.Player;
import entity.effect.SmokeBombEffect;
import java.util.List;

/**
 * Smoke Bomb item: enemy attacks deal 0 damage for
 * the current turn and the next turn.
 */
public class SmokeBomb implements Item {

  @Override
  public String getName() {
    return "Smoke Bomb";
  }

  @Override
  public String use(Player player, List<Combatant> allCombatants) {
    player.addEffect(new SmokeBombEffect());
    return "Smoke Bomb deployed! Enemy attacks deal 0 damage for 2 turns.";
  }
}
