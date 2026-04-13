package entity.combatant;

public abstract class Item {
    protected String name;
    protected String description;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public abstract String use(Combatant user, Combatant target);
}

class Potion extends Item {
    private static final int HEAL_AMOUNT = 100;

    public Potion() {
        super("Potion", "Heals 100 HP");
    }

    @Override
    public String use(Combatant user, Combatant target) {
        int oldHp = user.getCurrentHp();
        user.heal(HEAL_AMOUNT);
        return user.getName() + " uses Potion! HP: " + oldHp + " → " + user.getCurrentHp();
    }
}

class PowerStone extends Item {
    public PowerStone() {
        super("Power Stone", "Use special skill for free");
    }

    @Override
    public String use(Combatant user, Combatant target) {
        if (user instanceof Player) {
            Player player = (Player) user;
            int oldCooldown = player.getSpecialCooldown();
            String result = player.executeSpecialSkill(target, null);
            player.setSpecialCooldown(oldCooldown);
            return player.getName() + " uses Power Stone! " + result;
        }
        return user.getName() + " cannot use Power Stone!";
    }
}

class SmokeBombItem extends Item {
    public SmokeBombItem() {
        super("Smoke Bomb", "Enemies can't damage you this turn and next turn");
    }

    @Override
    public String use(Combatant user, Combatant target) {
        user.addStatusEffect(new SmokeBombEffect());
        return user.getName() + " throws a smoke bomb! Enemy attacks deal 0 damage for 2 turns.";
    }
}