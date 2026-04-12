package entity.action;

import entity.combatant.Combatant;
import java.util.List;

/**
 * Interface for all combat actions.
 * Demonstrates ISP: focused interface with no bloat.
 * Demonstrates OCP: new actions added by implementing this interface.
 */
public interface Action {
    /**
     * Get the name of this action.
     */
    String getName();

    /**
     * Check if this action is available for the given actor.
     */
    boolean isAvailable(Combatant actor);

    /**
     * Execute this action.
     * @param actor the combatant performing the action
     * @param targets applicable targets
     * @return description of what happened
     */
    String execute(Combatant actor, List<Combatant> targets);
}
