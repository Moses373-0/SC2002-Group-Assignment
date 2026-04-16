package entity.combatant.player;

import entity.action.Action;
import entity.combatant.Combatant;
import entity.item.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for player characters.
 * Manages inventory and special skill execution.
 */
public abstract class Player extends Combatant {
    private List<Item> inventory;

    public Player(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
        this.inventory = new ArrayList<>();
    }

    public List<Item> getInventory() { return inventory; }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public boolean hasItems() {
        return !inventory.isEmpty();
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    /**
     * Execute this player's class-specific special skill.
     * @param targets list of all combatants in battle
     * @return description of what happened
     */
    public abstract String executeSpecialSkill(List<Combatant> targets);

    /**
     * Get the name of this player's special skill.
     */
    public abstract String getSpecialSkillName();

    /**
     * Get the target type for this player's special skill.
     * Delegates targeting knowledge to the player subclass,
     * so BattleEngine never needs instanceof checks.
     */
    public abstract Action.TargetType getSpecialSkillTargetType();
}
