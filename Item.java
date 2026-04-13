package com.turnbased.game.core;

public abstract class Item {
    protected String name;
    protected String description;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public abstract void use(Combatant user, Combatant target);
}

class Potion extends Item {
    private static final int HEAL_AMOUNT = 100;

    public Potion() {
        super("Potion", "Heals 100 HP");
    }

    @Override
    public void use(Combatant user, Combatant target) {
        int oldHp = user.getCurrentHp();
        user.heal(HEAL_AMOUNT);
        System.out.printf("%s uses Potion! HP: %d → %d%n", user.getName(), oldHp, user.getCurrentHp());
    }
}

class PowerStone extends Item {
    public PowerStone() {
        super("Power Stone", "Use special skill for free");
    }

    @Override
    public void use(Combatant user, Combatant target) {
        if (user instanceof Player) {
            Player player = (Player) user;
            System.out.println(player.getName() + " uses the Power Stone!");
            int oldCooldown = player.getSpecialCooldown();
            if (target != null) {
                // This needs all enemies - will be fixed in Day 2
                player.executeSpecialSkill(target, null);
            }
            player.setSpecialCooldown(oldCooldown);
        }
    }
}

class SmokeBombItem extends Item {
    public SmokeBombItem() {
        super("Smoke Bomb", "Enemies can't damage you this turn and next turn");
    }

    @Override
    public void use(Combatant user, Combatant target) {
        System.out.println(user.getName() + " throws a smoke bomb!");
    }
}