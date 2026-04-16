package controller;
import boundary.GameUI;
import controller.turnorder.TurnOrderStrategy;
import entity.action.Action;
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
 * Demonstrates OCP: new actions can be added to the actions list
 *   without modifying this class.
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
    // Actions — injected via constructor (OCP: add new actions without modifying this class)
    private List<Action> actions;
    public BattleEngine(Player player, Level level, TurnOrderStrategy turnOrderStrategy,
                        List<Action> actions, GameUI ui) {
        this.player = player;
        this.level = level;
        this.allEnemies = new ArrayList<>(level.getInitialSpawn());
        this.turnOrderStrategy = turnOrderStrategy;
        this.actions = actions;
        this.ui = ui;
        this.roundNumber = 0;
        this.battleOver = false;
        this.playerWon = false;
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
     * Run a single round.
     */
    private void runRound() {
        ui.displayRoundHeader(roundNumber);
        // Get alive combatants for turn order
        List<Combatant> aliveCombatants = getAliveCombatants();
        List<Combatant> turnOrder = turnOrderStrategy.determineTurnOrder(aliveCombatants);
        ui.displayTurnOrder(turnOrder);
        ui.displayCombatantStatus(player, allEnemies);
        // Process each combatant's turn
        for (Combatant combatant : turnOrder) {
            if (!combatant.isAlive()) {
                continue;
            }
            ui.displayTurnStart(combatant);
            // Apply existing effects at start of turn
            combatant.applyEffects();
            // Check if stunned
            if (combatant.isStunned()) {
                ui.displayStunned(combatant);
                // Tick effects and cooldown even when stunned
                combatant.tickAndRemoveExpiredEffects();
                combatant.tickCooldown();
                if (checkBattleEnd()) return;
                continue;
            }
            // Process action
            if (combatant instanceof Player) {
                processPlayerTurn((Player) combatant);
            } else if (combatant instanceof Enemy) {
                processEnemyTurn((Enemy) combatant);
            }
            // Tick effects and cooldown
            combatant.tickAndRemoveExpiredEffects();
            combatant.tickCooldown();
            if (checkBattleEnd()) return;
        }
        // Check for backup spawn at end of round
        checkBackupSpawn();
        // Display round end
        ui.displayRoundEnd(roundNumber, player, allEnemies);
    }
    /**
     * Process the player's turn.
     * Uses the injected action list dynamically — no hardcoded switch on action types.
     * Adding a new Action implementation requires zero changes to this method (OCP).
     */
    private void processPlayerTurn(Player player) {
        // Build available actions list dynamically
        List<Action> availableActions = new ArrayList<>();
        for (Action action : actions) {
            if (action.isAvailable(player)) {
                availableActions.add(action);
            }
        }
        // Display menu and get choice
        int actionChoice = ui.getPlayerAction(player, availableActions);
        Action selectedAction = availableActions.get(actionChoice - 1);
        // Resolve targeting based on the action's declared target type (no instanceof needed)
        List<Combatant> targets;
        Action.TargetType targetType = selectedAction.getTargetType(player);
        switch (targetType) {
            case SINGLE_ENEMY:
                List<Enemy> aliveEnemies = getAliveEnemies();
                int targetIndex = ui.selectTarget(aliveEnemies);
                targets = new ArrayList<>();
                targets.add(aliveEnemies.get(targetIndex));
                break;
            case ALL:
                targets = new ArrayList<>(getAllCombatants());
                break;
            case SELF:
            case NONE:
            default:
                targets = new ArrayList<>();
                break;
        }
        // Handle item selection if action requires it (no instanceof needed — uses interface method)
        if (selectedAction.requiresItemSelection()) {
            int itemIndex = ui.selectItemFromInventory(player.getInventory());
            Item selectedItem = player.getInventory().get(itemIndex);
            selectedAction.setSelectedItem(selectedItem);
            // Item-based actions pass all combatants so items like SmokeBomb can apply to player
            targets = new ArrayList<>(getAllCombatants());
        }
        String result = selectedAction.execute(player, targets);
        ui.displayActionResult(result);
    }
    /**
     * Process an enemy's turn.
     */
    private void processEnemyTurn(Enemy enemy) {
        List<Combatant> allCombatants = getAllCombatants();
        String result = enemy.performAction(allCombatants);
        ui.displayActionResult(result);
    }
    /**
     * Check if backup spawn should be triggered.
     */
    private void checkBackupSpawn() {
        if (!level.isBackupTriggered() && level.hasBackupSpawn()) {
            // Check if all current enemies are eliminated
            boolean allCurrentDefeated = true;
            for (Enemy e : allEnemies) {
                if (e.isAlive()) {
                    allCurrentDefeated = false;
                    break;
                }
            }
            if (allCurrentDefeated) {
                List<Enemy> backup = level.triggerBackupSpawn();
                allEnemies.addAll(backup);
                ui.displayBackupSpawn(backup);
            }
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