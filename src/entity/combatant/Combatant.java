package entity.combatant;

import entity.effect.StatusEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract base class for all combat participants.
 * Demonstrates SRP: only manages combatant state and effects.
 * Demonstrates LSP: Player and Enemy are interchangeable as Combatant.
 */
public abstract class Combatant {
    private String name;
    private int hp;
    private int maxHp;
    private int baseAttack;
    private int baseDefense;
    private int speed;
    private int bonusAttack;
    private int bonusDefense;
    private int specialSkillCooldown;
    private boolean alive;
    private List<StatusEffect> activeEffects;

    public Combatant(String name, int hp, int attack, int defense, int speed) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.baseAttack = attack;
        this.baseDefense = defense;
        this.speed = speed;
        this.bonusAttack = 0;
        this.bonusDefense = 0;
        this.specialSkillCooldown = 0;
        this.alive = true;
        this.activeEffects = new ArrayList<>();
    }

    // --- Getters ---
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return baseAttack + bonusAttack; }
    public int getBaseAttack() { return baseAttack; }
    public int getDefense() { return baseDefense + bonusDefense; }
    public int getBaseDefense() { return baseDefense; }
    public int getSpeed() { return speed; }
    public int getBonusAttack() { return bonusAttack; }
    public int getBonusDefense() { return bonusDefense; }
    public int getSpecialSkillCooldown() { return specialSkillCooldown; }
    public boolean isAlive() { return alive; }
    public List<StatusEffect> getActiveEffects() { return activeEffects; }

    // --- Setters ---
    public void setBonusAttack(int bonusAttack) { this.bonusAttack = bonusAttack; }
    public void setBonusDefense(int bonusDefense) { this.bonusDefense = bonusDefense; }
    public void setSpecialSkillCooldown(int cooldown) { this.specialSkillCooldown = cooldown; }

    /**
     * Apply damage to this combatant.
     * HP cannot go below 0.
     */
    public void takeDamage(int damage) {
        int effectiveDamage = Math.max(0, damage);
        this.hp = Math.max(0, this.hp - effectiveDamage);
        if (this.hp == 0) {
            this.alive = false;
        }
    }

    /**
     * Heal this combatant. HP cannot exceed maxHp.
     */
    public void heal(int amount) {
        this.hp = Math.min(this.maxHp, this.hp + amount);
    }

    /**
     * Add a status effect to this combatant.
     */
    public void addEffect(StatusEffect effect) {
        activeEffects.add(effect);
        effect.onApply(this);
    }

    /**
     * Apply all active status effects at the start of a turn.
     */
    public void applyEffects() {
        for (StatusEffect effect : activeEffects) {
            effect.onTurnStart(this);
        }
    }

    /**
     * Tick down effect durations and remove expired effects.
     */
    public void tickAndRemoveExpiredEffects() {
        Iterator<StatusEffect> it = activeEffects.iterator();
        while (it.hasNext()) {
            StatusEffect effect = it.next();
            effect.tick();
            if (effect.isExpired()) {
                effect.onRemove(this);
                it.remove();
            }
        }
    }

    /**
     * Reduce special skill cooldown by 1 (called when this combatant takes a turn).
     */
    public void tickCooldown() {
        if (specialSkillCooldown > 0) {
            specialSkillCooldown--;
        }
    }

    /**
     * Check if this combatant is stunned.
     */
    public boolean isStunned() {
        for (StatusEffect effect : activeEffects) {
            if (effect.preventsAction()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if this combatant has smoke bomb invulnerability.
     */
    public boolean hasSmokeBombInvulnerability() {
        for (StatusEffect effect : activeEffects) {
            if (effect.negatesDamage()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name + " (HP: " + hp + "/" + maxHp + " | ATK: " + getAttack() +
               " | DEF: " + getDefense() + " | SPD: " + speed + ")";
    }
}
