package entity.action;

import entity.combatant.Combatant;
import entity.combatant.player.Player;
import entity.item.Item;
import java.util.List;

/**
 * UseItem action: delegates to the selected Item's use() method.
 */
public class UseItem implements Action {
    private Item selectedItem;

    @Override
    public String getName() {
        return "Use Item";
    }

    @Override
    public boolean isAvailable(Combatant actor) {
        if (actor instanceof Player) {
            return ((Player) actor).hasItems();
        }
        return false;
    }

    @Override
    public TargetType getTargetType(Combatant actor) {
        return TargetType.SELF;
    }

    @Override
    public boolean requiresItemSelection() {
        return true;
    }

    public void setSelectedItem(Item item) {
        this.selectedItem = item;
    }

    @Override
    public String execute(Combatant actor, List<Combatant> targets) {
        if (!(actor instanceof Player)) {
            return "Only players can use items!";
        }
        Player player = (Player) actor;

        if (selectedItem == null) {
            return "No item selected!";
        }

        // Remove item from inventory and use it
        player.removeItem(selectedItem);
        String result = selectedItem.use(player, targets);

        // Clear selection
        selectedItem = null;

        return result;
    }
}