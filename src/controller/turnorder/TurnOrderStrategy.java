package controller.turnorder;

import entity.combatant.Combatant;
import java.util.List;

/**
 * Strategy interface for determining turn order.
 * Demonstrates DIP: BattleEngine depends on this abstraction.
 * Demonstrates OCP: new turn order strategies can be added.
 */
public interface TurnOrderStrategy {
    /**
     * Determine the order of turns for the given combatants.
     * @param combatants all alive combatants
     * @return ordered list from first to last action
     */
    List<Combatant> determineTurnOrder(List<Combatant> combatants);
}
