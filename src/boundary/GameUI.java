package boundary;

import entity.action.Action;
import entity.combatant.Combatant;
import entity.combatant.player.Player;
import entity.combatant.enemy.Enemy;
import entity.effect.StatusEffect;
import entity.item.Item;
import entity.level.Difficulty;
import java.util.List;
import java.util.Scanner;

/**
 * Boundary class for all CLI user interaction.
 * Demonstrates SRP: only handles input/output.
 * Separated from battle logic (DIP).
 */
public class GameUI {
    private Scanner scanner;

    public GameUI() {
        this.scanner = new Scanner(System.in);
    }

    // =================== LOADING SCREEN ===================

    public void displayTitle() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║         TURN-BASED COMBAT ARENA                ║");
        System.out.println("║              SC2002 Assignment                 ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();
    }

    public void displayPlayerOptions() {
        System.out.println("═══════════════ SELECT YOUR PLAYER ═══════════════");
        System.out.println();
        System.out.println("  [1] Warrior");
        System.out.println("      HP: 260 | ATK: 40 | DEF: 20 | SPD: 30");
        System.out.println("      Special: Shield Bash - Damage + Stun (2 turns)");
        System.out.println();
        System.out.println("  [2] Wizard");
        System.out.println("      HP: 200 | ATK: 50 | DEF: 10 | SPD: 20");
        System.out.println("      Special: Arcane Blast - AoE damage + ATK buff on kills");
        System.out.println();
    }

    public int selectPlayer() {
        displayPlayerOptions();
        return getValidInput("Choose your player (1-2): ", 1, 2);
    }

    public void displayItemOptions() {
        System.out.println();
        System.out.println("═══════════════ SELECT YOUR ITEMS ════════════════");
        System.out.println("  Choose 2 items (duplicates allowed)");
        System.out.println();
        System.out.println("  [1] Potion      - Heal 100 HP");
        System.out.println("  [2] Power Stone - Free special skill use (no cooldown)");
        System.out.println("  [3] Smoke Bomb  - Enemy attacks deal 0 damage (2 turns)");
        System.out.println();
    }

    public int selectItem(int itemNumber) {
        return getValidInput("Choose item " + itemNumber + " (1-3): ", 1, 3);
    }

    public void displayDifficultyOptions() {
        System.out.println();
        System.out.println("════════════════ SELECT DIFFICULTY ════════════════");
        System.out.println();
        System.out.println("  [1] Easy   - 3 Goblins");
        System.out.println("  [2] Medium - 1 Goblin + 1 Wolf | Backup: 2 Wolves");
        System.out.println("  [3] Hard   - 2 Goblins | Backup: 1 Goblin + 2 Wolves");
        System.out.println();
        System.out.println("  Enemy Stats:");
        System.out.println("    Goblin: HP:55 | ATK:35 | DEF:15 | SPD:25");
        System.out.println("    Wolf:   HP:40 | ATK:45 | DEF:5  | SPD:35");
        System.out.println();
    }

    public int selectDifficulty() {
        displayDifficultyOptions();
        return getValidInput("Choose difficulty (1-3): ", 1, 3);
    }

    // =================== TURN ORDER STRATEGY ===================

    public int selectTurnOrderStrategy() {
        System.out.println();
        System.out.println("═══════════ SELECT TURN ORDER STRATEGY ════════════");
        System.out.println();
        System.out.println("  [1] Speed-Based  - Highest speed goes first");
        System.out.println("  [2] Player First - Player always acts before enemies");
        System.out.println();
        return getValidInput("Choose turn order strategy (1-2): ", 1, 2);
    }

    // =================== BATTLE DISPLAY ===================

    public void displayRoundHeader(int roundNumber) {
        System.out.println();
        System.out.println("══════════════════ ROUND " + roundNumber + " ══════════════════");
    }

    public void displayTurnOrder(List<Combatant> turnOrder) {
        System.out.print("  Turn Order: ");
        for (int i = 0; i < turnOrder.size(); i++) {
            Combatant c = turnOrder.get(i);
            System.out.print(c.getName() + " (SPD:" + c.getSpeed() + ")");
            if (i < turnOrder.size() - 1) System.out.print(" → ");
        }
        System.out.println();
        System.out.println();
    }

    public void displayCombatantStatus(Player player, List<Enemy> enemies) {
        System.out.println("── Status ──────────────────────────────────────────");
        System.out.println("  " + player.getName() + ": HP " + player.getHp() + "/" + player.getMaxHp() +
                           " | ATK:" + player.getAttack() + " | DEF:" + player.getDefense());

        // Active effects
        if (!player.getActiveEffects().isEmpty()) {
            System.out.print("    Effects: ");
            for (StatusEffect e : player.getActiveEffects()) {
                System.out.print("[" + e.getName() + " " + e.getDuration() + "t] ");
            }
            System.out.println();
        }

        // Items
        if (player.hasItems()) {
            System.out.print("    Items: ");
            for (Item item : player.getInventory()) {
                System.out.print("[" + item.getName() + "] ");
            }
            System.out.println();
        }

        // Cooldown
        if (player.getSpecialSkillCooldown() > 0) {
            System.out.println("    Special Skill Cooldown: " + player.getSpecialSkillCooldown() + " rounds");
        } else {
            System.out.println("    Special Skill: READY");
        }

        System.out.println();
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                System.out.print("  " + enemy.getName() + ": HP " + enemy.getHp() + "/" + enemy.getMaxHp() +
                               " | ATK:" + enemy.getAttack() + " | DEF:" + enemy.getDefense());
                if (!enemy.getActiveEffects().isEmpty()) {
                    System.out.print(" | Effects: ");
                    for (StatusEffect e : enemy.getActiveEffects()) {
                        System.out.print("[" + e.getName() + " " + e.getDuration() + "t] ");
                    }
                }
                System.out.println();
            } else {
                System.out.println("  " + enemy.getName() + ": ✗ ELIMINATED");
            }
        }
        System.out.println("────────────────────────────────────────────────────");
    }

    public void displayTurnStart(Combatant combatant) {
        System.out.println();
        System.out.println(">> " + combatant.getName() + "'s Turn");
    }

    public void displayStunned(Combatant combatant) {
        System.out.println("   " + combatant.getName() + " is STUNNED! Turn skipped.");
    }

    public void displayEliminated(Combatant combatant) {
        System.out.println("   " + combatant.getName() + " is ELIMINATED. Turn skipped.");
    }

    public void displayActionResult(String result) {
        System.out.println("   " + result.replace("\n", "\n   "));
    }

    // =================== PLAYER ACTION SELECTION ===================

    /**
     * Display available actions and get player choice.
     * Accepts a dynamic list of available actions (OCP: new actions appear automatically).
     */
    public int getPlayerAction(Player player, List<Action> availableActions) {
        System.out.println();
        System.out.println("   Choose your action:");
        for (int i = 0; i < availableActions.size(); i++) {
            System.out.println("   [" + (i + 1) + "] " + availableActions.get(i).getName());
        }
        return getValidInput("   > ", 1, availableActions.size());
    }

    public int selectTarget(List<Enemy> aliveEnemies) {
        System.out.println("   Select target:");
        for (int i = 0; i < aliveEnemies.size(); i++) {
            Enemy e = aliveEnemies.get(i);
            System.out.println("   [" + (i + 1) + "] " + e.getName() +
                             " (HP: " + e.getHp() + "/" + e.getMaxHp() + ")");
        }
        return getValidInput("   > ", 1, aliveEnemies.size()) - 1;
    }

    public int selectItemFromInventory(List<Item> items) {
        System.out.println("   Select item to use:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("   [" + (i + 1) + "] " + items.get(i).getName());
        }
        return getValidInput("   > ", 1, items.size()) - 1;
    }

    // =================== BACKUP SPAWN ===================

    public void displayBackupSpawn(List<Enemy> backupEnemies) {
        System.out.println();
        System.out.println("⚠ BACKUP SPAWN TRIGGERED! ⚠");
        System.out.println("  New enemies enter the battlefield:");
        for (Enemy e : backupEnemies) {
            System.out.println("  • " + e.getName() + " (HP: " + e.getHp() + " | ATK: " +
                             e.getAttack() + " | DEF: " + e.getDefense() + " | SPD: " + e.getSpeed() + ")");
        }
        System.out.println();
    }

    // =================== GAME END ===================

    public void displayVictory(int remainingHp, int maxHp, int totalRounds, Player player) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║              🏆 VICTORY! 🏆                    ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║  Congratulations, you have defeated all your    ║");
        System.out.println("║  enemies.                                       ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("  Remaining HP: " + remainingHp + "/" + maxHp);
        System.out.println("  Total Rounds: " + totalRounds);
        // Display remaining items
        if (player.hasItems()) {
            System.out.print("  Remaining Items: ");
            for (Item item : player.getInventory()) {
                System.out.print(item.getName() + " ");
            }
            System.out.println();
        }
        System.out.println("╚══════════════════════════════════════════════════╝");
    }

    public void displayDefeat(int enemiesRemaining, int totalRounds) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║              💀 DEFEAT 💀                      ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║  Defeated. Don't give up, try again!            ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("  Enemies Remaining: " + enemiesRemaining);
        System.out.println("  Total Rounds Survived: " + totalRounds);
        System.out.println("╚══════════════════════════════════════════════════╝");
    }

    public int getReplayChoice() {
        System.out.println();
        System.out.println("  What would you like to do?");
        System.out.println("  [1] Replay with same settings");
        System.out.println("  [2] Start a new game");
        System.out.println("  [3] Exit");
        return getValidInput("  > ", 1, 3);
    }

    // =================== ROUND END ===================

    public void displayRoundEnd(int roundNumber, Player player, List<Enemy> enemies) {
        System.out.println();
        System.out.println("── End of Round " + roundNumber + " ──");
        System.out.print("  " + player.getName() + " HP: " + player.getHp() + "/" + player.getMaxHp());
        if (player.getSpecialSkillCooldown() > 0) {
            System.out.print(" | Cooldown: " + player.getSpecialSkillCooldown());
        }
        System.out.println();
        for (Enemy e : enemies) {
            if (e.isAlive()) {
                System.out.print("  " + e.getName() + " HP: " + e.getHp() + "/" + e.getMaxHp());
                if (!e.getActiveEffects().isEmpty()) {
                    for (StatusEffect eff : e.getActiveEffects()) {
                        System.out.print(" [" + eff.getName() + "]");
                    }
                }
                System.out.println();
            } else {
                System.out.println("  " + e.getName() + " ✗ ELIMINATED");
            }
        }
        // Display items
        System.out.print("  Items: ");
        if (player.hasItems()) {
            for (Item item : player.getInventory()) {
                System.out.print(item.getName() + " ");
            }
        } else {
            System.out.print("None");
        }
        System.out.println();
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    // =================== UTILITY ===================

    private int getValidInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine().trim();
                int choice = Integer.parseInt(line);
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.println("   Invalid choice. Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("   Invalid input. Please enter a number.");
            }
        }
    }
}