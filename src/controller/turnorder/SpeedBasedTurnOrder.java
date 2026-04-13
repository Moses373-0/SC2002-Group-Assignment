package controller.turnorder;

import entity.combatant.Combatant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Speed-based turn order: higher speed goes first.
 */
public class SpeedBasedTurnOrder implements TurnOrderStrategy {

    @Override
    public List<Combatant> determineTurnOrder(List<Combatant> combatants) {
        List<Combatant> ordered = new ArrayList<>(combatants);
        ordered.sort(Comparator.comparingInt(Combatant::getSpeed).reversed());
        return ordered;
    }
}
