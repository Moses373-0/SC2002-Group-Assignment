package entity.action;

import entity.combatant.Combatant;
import entity.item.Item;
import java.util.List;

/**
 * Interface for all combat actions.
 * Demonstrates ISP: focused interface with no bloat.
 * Demonstrates OCP: new actions added by implementing this interface.
 */
public interface Action {

    /**
     * Target type for action resolution.
     * BattleEngine uses this to determine targeting UI — no instanceof needed.
     */
    enum TargetType {
        SINGLE_ENEMY,  // Player picks one alive enemy
        SELF,          // No target selection needed
        ALL,           // Targets all combatants
        NONE           // No targets needed
    }

    /**
     * Get the name of this action.
     */
    String getName();

    /**
     * Check if this action is available for the given actor.
     */
    boolean isAvailable(Combatant actor);

    /**
     * Get the target type for this action.
     * BattleEngine queries this instead of hardcoding targeting logic.
     */
    TargetType getTargetType(Combatant actor);

    /**
     * Whether this action requires item selection before execution.
     * Default: false. UseItem overrides to return true.
     */
    boolean requiresItemSelection();

    /**
     * Set the selected item for this action (only meaningful for item-based actions).
     * Default implementation is a no-op. UseItem overrides this.
     */
    void setSelectedItem(Item item);

    /**
     * Execute this action.
     * @param actor the combatant performing the action
     * @param targets applicable targets
     * @return description of what happened
     */
    String execute(Combatant actor, List<Combatant> targets);
}
