package controller.turnorder;

import entity.combatant.Combatant;
import entity.combatant.player.Player;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Turn order strategy that prioritizes players before enemies.
 * Within each group (players / enemies), combatants are sorted by speed (descending).
 * Demonstrates OCP: new strategy added without modifying BattleEngine.
 * Demonstrates DIP: BattleEngine depends on TurnOrderStrategy interface, not this class.
 */
public class PlayerFirstTurnOrder implements TurnOrderStrategy {

    @Override
    public List<Combatant> determineTurnOrder(List<Combatant> combatants) {
        List<Combatant> players = new ArrayList<>();
        List<Combatant> enemies = new ArrayList<>();

        // Separate players and enemies
        for (Combatant c : combatants) {
            if (c instanceof Player) {
                players.add(c);
            } else {
                enemies.add(c);
            }
        }

        // Sort each group by speed (descending)
        Comparator<Combatant> bySpeed = (a, b) -> Integer.compare(b.getSpeed(), a.getSpeed());
        players.sort(bySpeed);
        enemies.sort(bySpeed);

        // Players first, then enemies
        List<Combatant> result = new ArrayList<>();
        result.addAll(players);
        result.addAll(enemies);
        return result;
    }
}
