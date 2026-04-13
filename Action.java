package com.turnbased.game.core;

import java.util.List;

public abstract class Action {
    protected Combatant performer;
    protected String name;

    public Action(Combatant performer, String name) {
        this.performer = performer;
        this.name = name;
    }

    public String getName() { return name; }
    public Combatant getPerformer() { return performer; }

    public abstract void execute(List<Combatant> allCombatants);
}

class BasicAttack extends Action {
    private Combatant target;

    public BasicAttack(Combatant performer, Combatant target) {
        super(performer, "Basic Attack");
        this.target = target;
    }

    @Override
    public void execute(List<Combatant> allCombatants) {
        if (target == null || !target.isAlive()) {
            System.out.println(performer.getName() + " has no valid target!");
            return;
        }
        int damage = performer.calculateDamage(performer.getAttack());
        System.out.printf("%s attacks %s for %d damage!%n", performer.getName(), target.getName(), damage);
        target.takeDamage(performer.getAttack());
    }
}

class DefendAction extends Action {
    public DefendAction(Combatant performer) {
        super(performer, "Defend");
    }

    @Override
    public void execute(List<Combatant> allCombatants) {
        System.out.println(performer.getName() + " raises their guard.");
        performer.applyDefendBuff();
    }
}

class UseItemAction extends Action {
    private Item item;
    private Combatant target;

    public UseItemAction(Combatant performer, Item item, Combatant target) {
        super(performer, "Use " + item.getName());
        this.item = item;
        this.target = target;
    }

    @Override
    public void execute(List<Combatant> allCombatants) {
        item.use(performer, target);
        if (performer instanceof Player) {
            ((Player) performer).removeItem(item);
        }
    }
}

class SpecialSkillAction extends Action {
    private Combatant target;
    private List<Combatant> allEnemies;

    public SpecialSkillAction(Combatant performer, Combatant target, List<Combatant> allEnemies) {
        super(performer, "Special Skill");
        this.target = target;
        this.allEnemies = allEnemies;
    }

    @Override
    public void execute(List<Combatant> allCombatants) {
        if (performer instanceof Player) {
            Player player = (Player) performer;
            if (player.getSpecialCooldown() > 0) {
                System.out.println("Special skill needs " + player.getSpecialCooldown() + " more turns.");
                return;
            }
            player.executeSpecialSkill(target, allEnemies);
            player.setSpecialCooldown(3);
        }
    }
}