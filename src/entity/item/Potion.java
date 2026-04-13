package entity.item;

import entity.combatant.Combatant;
import entity.combatant.player.Player;
import java.util.List;

/**
 * Potion item: heals 100 HP (capped at max HP).
 */
public class Potion implements Item {

  @Override
  public String getName() {
    return "Potion";
  }

  @Override
  public String use(Player player, List<Combatant> allCombatants) {
    int oldHp = player.getHp();
    player.heal(100);
    int healed = player.getHp() - oldHp;
    return "Healed " + healed + " HP! HP: " + oldHp + " -> " + player.getHp() + "/" + player.getMaxHp();
  }
}
