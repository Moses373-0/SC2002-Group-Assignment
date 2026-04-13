package entity.action;

import entity.combatant.Combatant;
import entity.combatant.Player;
import entity.combatant.Item;
import java.util.List;

public class UseItemAction implements Action {
    private Item item;

    public UseItemAction(Item item) {
        this.item = item;
    }

    @Override
    public String getName() {
        return "Use " + item.getName();
    }

    @Override
    public boolean isAvailable(Combatant actor) {
        if (actor instanceof Player) {
            return !((Player) actor).getInventory().isEmpty();
        }
        return false;
    }

    @Override
    public String execute(Combatant actor, List<Combatant> targets) {
        if (targets.isEmpty()) {
            return "No target selected for item!";
        }
        Combatant target = targets.get(0);
        String result = item.use(actor, target);
        if (actor instanceof Player) {
            ((Player) actor).removeItem(item);
        }
        return result;
    }
}