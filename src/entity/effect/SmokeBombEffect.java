package entity.effect;

import entity.combatant.Combatant;

/**
 * Smoke Bomb effect: enemy attacks deal 0 damage.
 * Duration: 2 turns (current turn + next turn).
 */
public class SmokeBombEffect extends StatusEffect {

  public SmokeBombEffect() {
    super("Smoke Bomb Invulnerability", 2);
  }

  @Override
  public void onApply(Combatant target) {
    // No stat change, checked dynamically
  }

  @Override
  public void onTurnStart(Combatant target) {
    // No per-turn effect
  }

  @Override
  public void onRemove(Combatant target) {
    // No cleanup
  }

  @Override
  public boolean negatesDamage() {
    return true;
  }
}
