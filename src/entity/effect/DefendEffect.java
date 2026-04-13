package entity.effect;

import entity.combatant.Combatant;

/**
 * Defend effect: +10 Defense for current round + next round.
 * Duration: 2 turns.
 */
public class DefendEffect extends StatusEffect {
  private static final int DEFENSE_BONUS = 10;

  public DefendEffect() {
    super("Defend", 2);
  }

  @Override
  public void onApply(Combatant target) {
    target.setBonusDefense(target.getBonusDefense() + DEFENSE_BONUS);
  }

  @Override
  public void onTurnStart(Combatant target) {
    // Bonus already applied
  }

  @Override
  public void onRemove(Combatant target) {
    target.setBonusDefense(target.getBonusDefense() - DEFENSE_BONUS);
  }
}
