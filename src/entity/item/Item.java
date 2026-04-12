package entity.item;

import entity.combatant.Combatant;
import entity.combatant.player.Player;
import java.util.List;

/**
 * Interface for usable items.
 * Demonstrates ISP: focused interface.
 * Demonstrates OCP: new items added by implementing this interface.
 */
public interface Item {
    /**
     * Get the name of this item.
     */
    String getName();

    /**
     * Use this item.
     * @param player the player using the item
     * @param allCombatants all combatants currently in battle
     * @return description of the effect
     */
    String use(Player player, List<Combatant> allCombatants);
}
