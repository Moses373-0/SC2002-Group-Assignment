package com.turnbased.game.core;

/**
 * Things that stick around on a character for a few turns.
 * Like stun, defend buff, smoke bomb protection.
 */
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

    // Lifecycle methods that each effect can override
    public abstract void onApply(Combatant target);
    public abstract void onTick(Combatant target);
    public abstract void onRemove(Combatant target);
}

/**
 * Stun effect - character can't act for current turn and next turn.
 * Used by Warrior's Shield Bash.
 */
class StunEffect extends StatusEffect {
    public StunEffect() {
        super("Stun", 2);  // Current turn + next turn
    }

    @Override
    public void onApply(Combatant target) {
        System.out.println(target.getName() + " is stunned and cannot act!");
    }

    @Override
    public void onTick(Combatant target) {
        // Nothing needed here - the stun check happens when choosing action
    }

    @Override
    public void onRemove(Combatant target) {
        System.out.println(target.getName() + " is no longer stunned.");
    }
}

/**
 * Smoke bomb makes enemies do 0 damage for current and next turn.
 */
class SmokeBombEffect extends StatusEffect {
    public SmokeBombEffect() {
        super("SmokeBomb", 2);
    }

    @Override
    public void onApply(Combatant target) {
        System.out.println("Smoke bomb covers the area! Enemy attacks deal 0 damage.");
    }

    @Override
    public void onTick(Combatant target) {
        // Checked during damage calculation
    }

    @Override
    public void onRemove(Combatant target) {
        System.out.println("Smoke bomb clears away.");
    }

    public boolean isActive() {
        return remainingDuration > 0;
    }
}