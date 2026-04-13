package com.turnbased.game.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Every fighter in the game extends this class - players and enemies.
 * This holds all the basic stats and behaviors everyone shares.
 */
public abstract class Combatant {
    protected String name;
    protected int maxHp;
    protected int currentHp;
    protected int attack;
    protected int defense;
    protected int speed;
    protected boolean isAlive;
    protected List<StatusEffect> activeEffects;
    protected int specialCooldown;
    protected int defendBuffRemaining;

    public Combatant(String name, int maxHp, int attack, int defense, int speed) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.isAlive = true;
        this.activeEffects = new ArrayList<>();
        this.specialCooldown = 0;
        this.defendBuffRemaining = 0;
    }

    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getSpecialCooldown() {
        return specialCooldown;
    }

    // HP cannot go below 0 - if it does, character dies
    public void setCurrentHp(int hp) {
        this.currentHp = Math.max(0, Math.min(hp, maxHp));
        if (this.currentHp <= 0) {
            this.isAlive = false;
        }
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void modifyAttack(int delta) {
        this.attack += delta;
    }

    public void setSpecialCooldown(int cooldown) {
        this.specialCooldown = cooldown;
    }

    // Only decrease cooldown if this character actually took a turn
    public void decreaseCooldown() {
        if (specialCooldown > 0) {
            specialCooldown--;
        }
    }

    // Defend action gives +10 defense for this turn and next turn
    public void applyDefendBuff() {
        this.defendBuffRemaining = 2;
    }

    public int getEffectiveDefense() {
        int bonus = (defendBuffRemaining > 0) ? 10 : 0;
        return defense + bonus;
    }

    public void decrementDefendBuff() {
        if (defendBuffRemaining > 0) {
            defendBuffRemaining--;
        }
    }

    // Damage formula from the spec: max(0, attackerAttack - targetDefense)
    public int calculateDamage(int rawDamage) {
        int effectiveDefense = getEffectiveDefense();
        return Math.max(0, rawDamage - effectiveDefense);
    }

    public void takeDamage(int rawDamage) {
        int actualDamage = calculateDamage(rawDamage);
        setCurrentHp(currentHp - actualDamage);
    }

    public void heal(int amount) {
        setCurrentHp(currentHp + amount);
    }

    // Status effect management
    public void addStatusEffect(StatusEffect effect) {
        activeEffects.add(effect);
        effect.onApply(this);
    }

    public void removeStatusEffect(StatusEffect effect) {
        activeEffects.remove(effect);
        effect.onRemove(this);
    }

    public void updateStatusEffects() {
        List<StatusEffect> toRemove = new ArrayList<>();
        for (StatusEffect effect : activeEffects) {
            effect.onTick(this);
            effect.decrementDuration();
            if (effect.getRemainingDuration() <= 0) {
                toRemove.add(effect);
            }
        }
        for (StatusEffect effect : toRemove) {
            removeStatusEffect(effect);
        }
    }

    public boolean hasStatusEffect(Class<? extends StatusEffect> effectClass) {
        for (StatusEffect effect : activeEffects) {
            if (effect.getClass().equals(effectClass)) {
                return true;
            }
        }
        return false;
    }

    public boolean isStunned() {
        return hasStatusEffect(StunEffect.class);
    }

    public void resetTurnBuffs() {
        decrementDefendBuff();
    }

    // Each subclass decides how to pick an action
    public abstract Action chooseAction(List<Combatant> allies, List<Combatant> enemies, java.util.Scanner scanner);

    public abstract String getType();
}