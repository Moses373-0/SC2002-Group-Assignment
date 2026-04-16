package entity.effect;

import entity.combatant.Combatant;

/**
 * Stun effect: affected entity cannot take actions.
 * Duration: 2 turns (current turn + next turn).
 */
public class StunEffect extends StatusEffect {

    public StunEffect(int duration) {
        super("Stun", duration);
    }

    @Override
    public void onApply(Combatant target) {
        // No stat change on application
    }

    @Override
    public void onTurnStart(Combatant target) {
        // Stun just prevents action, no additional effect
    }

    @Override
    public void onRemove(Combatant target) {
        // No cleanup needed
    }

    @Override
    public boolean preventsAction() {
        return true;
    }
}
