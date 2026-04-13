package entity.action;

import entity.combatant.Combatant;
import java.util.List;

public class DefendAction implements Action {

    @Override
    public String getName() {
        return "Defend";
    }

    @Override
    public boolean isAvailable(Combatant actor) {
        return true;
    }

    @Override
    public String execute(Combatant actor, List<Combatant> targets) {
        actor.applyDefendBuff();
        return actor.getName() + " raises their guard! Defense +10 for 2 turns.";
    }
}