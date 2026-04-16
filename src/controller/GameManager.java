package controller;

import boundary.GameUI;
import controller.turnorder.PlayerFirstTurnOrder;
import controller.turnorder.SpeedBasedTurnOrder;
import controller.turnorder.TurnOrderStrategy;
import entity.action.*;
import entity.combatant.player.Player;
import entity.combatant.player.Warrior;
import entity.combatant.player.Wizard;
import entity.item.*;
import entity.level.Difficulty;
import entity.level.Level;
import entity.combatant.enemy.Goblin;
import entity.combatant.enemy.Wolf;
import java.util.ArrayList;
import java.util.List;

/**
 * GameManager: orchestrates the overall game flow.
 * Handles character selection, item selection, difficulty, and replay.
 * Demonstrates SRP: only manages game flow coordination.
 * Acts as the Factory for creating concrete game objects (Factory Method pattern).
 */
public class GameManager {
    private GameUI ui;
    private TurnOrderStrategy turnOrderStrategy;

    // Saved settings for replay
    private int savedPlayerChoice;
    private int savedItem1Choice;
    private int savedItem2Choice;
    private int savedDifficultyChoice;
    private int savedStrategyChoice;

    public GameManager() {
        this.ui = new GameUI();
    }

    /**
     * Start the game loop.
     */
    public void start() {
        ui.displayTitle();

        boolean running = true;
        boolean isNewGame = true;

        while (running) {
            if (isNewGame) {
                // Player selection
                savedPlayerChoice = ui.selectPlayer();

                // Item selection
                ui.displayItemOptions();
                savedItem1Choice = ui.selectItem(1);
                savedItem2Choice = ui.selectItem(2);

                // Difficulty selection
                savedDifficultyChoice = ui.selectDifficulty();

                // Turn order strategy selection
                savedStrategyChoice = ui.selectTurnOrderStrategy();
            }

            // Create player
            Player player = createPlayer(savedPlayerChoice);

            // Add items
            player.addItem(createItem(savedItem1Choice));
            player.addItem(createItem(savedItem2Choice));

            // Create level
            Level level = createLevel(savedDifficultyChoice);

            // Create turn order strategy
            TurnOrderStrategy turnOrderStrategy = createTurnOrderStrategy(savedStrategyChoice);

            // Create actions list (OCP: add new actions here without modifying BattleEngine)
            List<Action> actions = createActions();

            // Display game info
            ui.displayMessage("");
            ui.displayMessage("═══════════════ BATTLE START ══════════════════");
            ui.displayMessage("  Player: " + player.getName());
            ui.displayMessage("  " + player);
            ui.displayMessage("  Items: " + player.getInventory().get(0).getName() +
                            " + " + player.getInventory().get(1).getName());
            ui.displayMessage("  Difficulty: " + level.getDifficulty().getDisplayName());
            ui.displayMessage("═══════════════════════════════════════════════");

            // Run battle
            BattleEngine engine = new BattleEngine(player, level, turnOrderStrategy, actions, ui);
            engine.runBattle();

            // Display results
            if (engine.isPlayerWon()) {
                ui.displayVictory(player.getHp(), player.getMaxHp(), engine.getRoundNumber(), player);
            } else {
                ui.displayDefeat(engine.getAliveEnemyCount(), engine.getRoundNumber());
            }

            // Replay?
            int replayChoice = ui.getReplayChoice();
            switch (replayChoice) {
                case 1: // Replay same settings
                    isNewGame = false;
                    break;
                case 2: // New game
                    isNewGame = true;
                    break;
                case 3: // Exit
                    running = false;
                    ui.displayMessage("");
                    ui.displayMessage("Thanks for playing! Goodbye.");
                    break;
            }
        }
    }

    /**
     * Create the list of player actions.
     * This is the single place where concrete actions are known.
     * To add a new action, simply add it here — BattleEngine needs no changes (OCP).
     */
    private List<Action> createActions() {
        List<Action> actions = new ArrayList<>();
        actions.add(new BasicAttack());
        actions.add(new Defend());
        actions.add(new UseItem());
        actions.add(new SpecialSkill());
        return actions;
    }

    private Player createPlayer(int choice) {
        switch (choice) {
            case 1: return new Warrior();
            case 2: return new Wizard();
            default: return new Warrior();
        }
    }

    private Item createItem(int choice) {
        switch (choice) {
            case 1: return new Potion();
            case 2: return new PowerStone();
            case 3: return new SmokeBomb();
            default: return new Potion();
        }
    }

    private Level createLevel(int choice) {
        switch (choice) {
            case 1: return new Level(Difficulty.EASY);
            case 2: return new Level(Difficulty.MEDIUM);
            case 3: return new Level(Difficulty.HARD);
            default: return new Level(Difficulty.EASY);
        }
    }

    private TurnOrderStrategy createTurnOrderStrategy(int choice) {
        switch (choice) {
            case 1: return new SpeedBasedTurnOrder();
            case 2: return new PlayerFirstTurnOrder();
            default: return new SpeedBasedTurnOrder();
        }
    }
}