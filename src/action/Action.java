package entity.action;

import entity.combatant.Combatant;
import java.util.List;

/**
 * Interface for all combat actions.
 * Demonstrates ISP: focused interface with no bloat.
 * Demonstrates OCP: new actions added by implementing this interface.
 */
public interface Action {
    String getName();
    boolean isAvailable(Combatant actor);
    String execute(Combatant actor, List<Combatant> targets);
}