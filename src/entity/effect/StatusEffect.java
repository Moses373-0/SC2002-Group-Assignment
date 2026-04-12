package entity.effect;

import entity.combatant.Combatant;

/**
 * Abstract base class for all status effects.
 * Demonstrates OCP: new effects can be added without modifying BattleEngine.
 */
public abstract class StatusEffect {
    private String name;
    private int duration;

    public StatusEffect(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() { return name; }
    public int getDuration() { return duration; }

    public boolean isExpired() {
        return duration <= 0;
    }

    /**
     * Decrease duration by 1.
     */
    public void tick() {
        duration--;
    }

    /**
     * Called when the effect is first applied.
     */
    public abstract void onApply(Combatant target);

    /**
     * Called at the start of each turn while active.
     */
    public abstract void onTurnStart(Combatant target);

    /**
     * Called when the effect expires and is removed.
     */
    public abstract void onRemove(Combatant target);

    /**
     * Returns true if this effect prevents the combatant from acting.
     */
    public boolean preventsAction() { return false; }

    /**
     * Returns true if this effect negates incoming damage.
     */
    public boolean negatesDamage() { return false; }

    @Override
    public String toString() {
        return name + " (" + duration + " turns)";
    }
}
