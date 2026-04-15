package controller;

import boundary.GameUI;
import controller.turnorder.TurnOrderStrategy;
import entity.action.*;
import entity.combatant.Combatant;
import entity.combatant.player.Player;
import entity.combatant.enemy.Enemy;
import entity.item.Item;
import entity.level.Level;
import java.util.ArrayList;
import java.util.List;

/**
 * BattleEngine: manages the flow of battle rounds.
 * Demonstrates SRP: only manages battle flow.
 * Demonstrates DIP: depends on Combatant (abstract), Action (interface),
 *   TurnOrderStrategy (interface) — never on concrete classes.
 */
public class BattleEngine {
    private Player player;
    private List<Enemy> allEnemies; // includes initial + backup
    private Level level;
    private TurnOrderStrategy turnOrderStrategy;
    private GameUI ui;
    private int roundNumber;
    private boolean battleOver;
    private boolean playerWon;

    // Actions
    private BasicAttack basicAttackAction;
    private Defend defendAction;
    private UseItem useItemAction;
    private SpecialSkill specialSkillAction;

    public BattleEngine(Player player, Level level, TurnOrderStrategy turnOrderStrategy, GameUI ui) {
        this.player = player;
        this.level = level;
        this.allEnemies = new ArrayList<>(level.getInitialSpawn());
        this.turnOrderStrategy = turnOrderStrategy;
        this.ui = ui;
        this.roundNumber = 0;
        this.battleOver = false;
        this.playerWon = false;

        // Initialize actions
        this.basicAttackAction = new BasicAttack();
        this.defendAction = new Defend();
        this.useItemAction = new UseItem();
        this.specialSkillAction = new SpecialSkill();
    }

    /**
     * Run the entire battle until completion.
     */
    public void runBattle() {
        while (!battleOver) {
            roundNumber++;
            runRound();
        }
    }

    /**
     * Check if battle has ended (all enemies dead or player dead).
     */
    private boolean checkBattleEnd() {
        // Check player death
        if (!player.isAlive()) {
            battleOver = true;
            playerWon = false;
            return true;
        }

        // Check all enemies dead (including potential backup)
        boolean allEnemiesDead = true;
        for (Enemy e : allEnemies) {
            if (e.isAlive()) {
                allEnemiesDead = false;
                break;
            }
        }

        // Only declare victory if all enemies dead AND (no backup OR backup already triggered)
        if (allEnemiesDead) {
            if (!level.hasBackupSpawn() || level.isBackupTriggered()) {
                battleOver = true;
                playerWon = true;
                return true;
            } else {
                // Trigger backup spawn immediately
                List<Enemy> backup = level.triggerBackupSpawn();
                allEnemies.addAll(backup);
                ui.displayBackupSpawn(backup);
            }
        }

        return false;
    }

    // =================== HELPERS ===================

    private List<Combatant> getAliveCombatants() {
        List<Combatant> alive = new ArrayList<>();
        if (player.isAlive()) alive.add(player);
        for (Enemy e : allEnemies) {
            if (e.isAlive()) alive.add(e);
        }
        return alive;
    }

    private List<Combatant> getAllCombatants() {
        List<Combatant> all = new ArrayList<>();
        all.add(player);
        all.addAll(allEnemies);
        return all;
    }

    private List<Enemy> getAliveEnemies() {
        List<Enemy> alive = new ArrayList<>();
        for (Enemy e : allEnemies) {
            if (e.isAlive()) alive.add(e);
        }
        return alive;
    }

    // =================== GETTERS ===================

    public int getRoundNumber() { return roundNumber; }
    public boolean isPlayerWon() { return playerWon; }
    public Player getPlayer() { return player; }
    public List<Enemy> getAllEnemies() { return allEnemies; }

    public int getAliveEnemyCount() {
        int count = 0;
        for (Enemy e : allEnemies) {
            if (e.isAlive()) count++;
        }
        return count;
    }
}
