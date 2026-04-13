package entity.combatant;

public abstract class StatusEffect {
    protected String name;
    protected int duration;
    protected int remainingDuration;

    public StatusEffect(String name, int duration) {
        this.name = name;
        this.duration = duration;
        this.remainingDuration = duration;
    }

    public String getName() { return name; }
    public int getDuration() { return duration; }
    public int getRemainingDuration() { return remainingDuration; }

    public void decrementDuration() { remainingDuration--; }

    public abstract void onApply(Combatant target);
    public abstract void onTick(Combatant target);
    public abstract void onRemove(Combatant target);
}

class StunEffect extends StatusEffect {
    public StunEffect() {
        super("Stun", 2);
    }

    @Override
    public void onApply(Combatant target) {
        System.out.println(target.getName() + " is stunned and cannot act!");
    }

    @Override
    public void onTick(Combatant target) {}

    @Override
    public void onRemove(Combatant target) {
        System.out.println(target.getName() + " is no longer stunned.");
    }
}

class SmokeBombEffect extends StatusEffect {
    public SmokeBombEffect() {
        super("SmokeBomb", 2);
    }

    @Override
    public void onApply(Combatant target) {
        System.out.println("Smoke bomb covers the area! Enemy attacks deal 0 damage.");
    }

    @Override
    public void onTick(Combatant target) {}

    @Override
    public void onRemove(Combatant target) {
        System.out.println("Smoke bomb clears away.");
    }

    public boolean isActive() {
        return remainingDuration > 0;
    }
}