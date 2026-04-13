package entity.combatant;

import java.util.ArrayList;
import java.util.List;

public abstract class Player extends Combatant {
    protected List<Item> inventory;
    protected int permanentAttackBonus;

    public Player(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
        this.inventory = new ArrayList<>();
        this.permanentAttackBonus = 0;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public boolean hasItems() {
        return !inventory.isEmpty();
    }

    public void addPermanentAttackBonus(int bonus) {
        this.permanentAttackBonus += bonus;
        this.attack += bonus;
    }

    public int getPermanentAttackBonus() {
        return permanentAttackBonus;
    }

    public abstract String executeSpecialSkill(Combatant target, List<Combatant> allEnemies);

    @Override
    public String getType() {
        return "Player";
    }
}