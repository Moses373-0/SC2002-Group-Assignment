package com.turnbased.game.core;

import java.util.ArrayList;
import java.util.List;

/**
 * All player characters extend this.
 * Warriors and Wizards are the two types.
 */
public abstract class Player extends Combatant {
    protected List<Item> inventory;
    protected int permanentAttackBonus;  // For Wizard's Arcane Blast stacking

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

    // For Wizard's Arcane Blast - each kill adds +10 attack permanently
    public void addPermanentAttackBonus(int bonus) {
        this.permanentAttackBonus += bonus;
        this.attack += bonus;
    }

    public int getPermanentAttackBonus() {
        return permanentAttackBonus;
    }

    // Special skill behavior - each player class implements their own
    public abstract void executeSpecialSkill(Combatant target, List<Combatant> allEnemies);

    @Override
    public String getType() {
        return "Player";
    }
}