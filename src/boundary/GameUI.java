package boundary;

import entity.combatant.Combatant;
import entity.combatant.player.Player;
import entity.combatant.player.Warrior;
import entity.combatant.player.Wizard;
import entity.combatant.enemy.Enemy;
import entity.effect.StatusEffect;
import entity.item.Item;
import entity.level.Difficulty;
import java.util.List;
import java.util.Scanner;

public class GameUI {
    private Scanner scanner;

    public GameUI() {
        this.scanner = new Scanner(System.in);
    }


    public void displayTitle() {
        msgOut("╔══════════════════════════════════════════════════╗");
        msgOut("║             TURN-BASED COMBAT ARENA              ║");
        msgOut("║                  SC2002 Assignment               ║");
        msgOut("╚══════════════════════════════════════════════════╝");
        msgOut("");
    }

    public void displayPlayerOptions() {
        msgOut("═══════════════ SELECT YOUR PLAYER ═══════════════");
        msgOut("");
        msgOut("    [1] Warrior");
        msgOut("        HP: 260 | ATK: 40 | DEF: 20 | SPD: 30");
        msgOut("        Special: Shield Bash - Damage + Stun (2 turns)");
        msgOut("");
        msgOut("    [2] Wizard");
        msgOut("        HP: 200 | ATK: 50 | DEF: 10 | SPD: 20");
        msgOut("        Special: Arcane Blast - AoE damage + ATK buff on kills");
        msgOut("");
    }

    public int selectPlayer() {
        displayPlayerOptions();
        return getValidInput("Choose your player (1-2): ", 1, 2);
    }

    public void displayItemOptions() {
        msgOut("");
        msgOut("═══════════════ SELECT YOUR ITEMS ════════════════");
        msgOut("Choose 2 items (duplicates allowed)");
        msgOut("");
        msgOut("    [1] Potion      - Heal 100 HP");
        msgOut("    [2] Power Stone - Free special skill use (no cooldown)");
        msgOut("    [3] Smoke Bomb  - Enemy attacks deal 0 damage (2 turns)");
        msgOut("");
    }

    public int selectItem(int itemNumber) {
        return getValidInput("Choose item " + itemNumber + " (1-3): ", 1, 3);
    }

    public void displayDifficultyOptions() {
        msgOut("");
        msgOut("════════════════ SELECT DIFFICULTY ════════════════");
        msgOut("");
        msgOut("    [1] Easy   - 3 Goblins");
        msgOut("    [2] Medium - 1 Goblin + 1 Wolf | Backup: 2 Wolves");
        msgOut("    [3] Hard   - 2 Goblins | Backup: 1 Goblin + 2 Wolves");
        msgOut("");
        msgOut("    Enemy Stats:");
        msgOut("        Goblin: HP:55 | ATK:35 | DEF:15 | SPD:25");
        msgOut("        Wolf:   HP:40 | ATK:45 | DEF:5  | SPD:35");
        msgOut("");
    }

    public int selectDifficulty() {
        displayDifficultyOptions();
        return getValidInput("Choose difficulty (1-3): ", 1, 3);
    }

    public int selectTurnOrderStrategy() {
        msgOut("");
        msgOut("═══════════ SELECT TURN ORDER STRATEGY ════════════");
        msgOut("");
        msgOut("    [1] Speed-Based  - Highest speed goes first");
        msgOut("    [2] Player First - Player always acts before enemies");
        msgOut("");
        return getValidInput("Choose turn order strategy (1-2): ", 1, 2);
    }

    public void displayRoundHeader(int roundNumber) {
        msgOut("");
        msgOut("══════════════════ ROUND " + roundNumber + " ══════════════════");
    }

    public void displayTurnOrder(List<Combatant> combatants) {
        System.out.print("  Turn Order: ");
        for (int i = 0; i < combatants.size(); i++) {
            Combatant c = combatants.get(i);
            System.out.print(c.getName() + " (SPD:" + c.getSpeed() + ")");
            if (i < combatants.size() - 1) System.out.print(" → ");
        }
        msgOut("");
        msgOut("");
    }

    public void displayCombatantStatus(Player player, List<Enemy> enemies) {
        msgOut("── Status ──────────────────────────────────────────");
        msgOut("  " + player.getName() + ": HP " + player.getHp() + "/" + player.getMaxHp() +
                           " | ATK:" + player.getAttack() + " | DEF:" + player.getDefense());

        // Active effects
        if (!player.getActiveEffects().isEmpty()) {
            System.out.print("    Effects: ");
            for (StatusEffect e : player.getActiveEffects()) {
                System.out.print("[" + e.getName() + " " + e.getDuration() + "t] ");
            }
            msgOut("");
        }

        // Items
        if (player.hasItems()) {
            System.out.print("    Items: ");
            for (Item item : player.getInventory()) {
                System.out.print("[" + item.getName() + "] ");
            }
            msgOut("");
        }

        // Cooldown
        if (player.getSpecialSkillCooldown() > 0) {
            msgOut("    Special Skill Cooldown: " + player.getSpecialSkillCooldown() + " rounds");
        } else {
            msgOut("    Special Skill: READY");
        }

        msgOut("");
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
                msgOut("");
            } else {
                msgOut("  " + enemy.getName() + ": ✗ ELIMINATED");
            }
        }
        msgOut("────────────────────────────────────────────────────");
    }

    public void displayTurnStart(Combatant combatant) {
        msgOut("");
        msgOut(">> " + combatant.getName() + "'s Turn");
    }

    public void displayStunned(Combatant combatant) {
        msgOut("   " + combatant.getName() + " is STUNNED! Turn skipped.");
    }

    public void displayEliminated(Combatant combatant) {
        msgOut("   " + combatant.getName() + " is ELIMINATED. Turn skipped.");
    }

    public void displayActionResult(String result) {
        msgOut("   " + result.replace("\n", "\n    "));
    }

    public int getPlayerAction(Player player) {
        msgOut("");
        msgOut("    Choose your action:");
        msgOut("    [1] Basic Attack");
        msgOut("    [2] Defend");

        int maxChoice = 2;
        if (player.hasItems()) {
            msgOut("    [3] Use Item");
            maxChoice = 3;
        }
        if (player.getSpecialSkillCooldown() == 0) {
            msgOut("    [4] " + player.getSpecialSkillName() + " (Special Skill)");
            maxChoice = 4;
        } else {
            msgOut("    [4] " + player.getSpecialSkillName() + " (Cooldown: " + player.getSpecialSkillCooldown() + " turns)");
        }

        int choice = getValidInput("    > ", 1, maxChoice);

        if (choice == 3 && !player.hasItems()) {
            msgOut("    No items available! Choose again.");
            return getPlayerAction(player);
        }
        if (choice == 4 && player.getSpecialSkillCooldown() > 0) {
            msgOut("    Special skill is on cooldown! Choose again.");
            return getPlayerAction(player);
        }
        return choice;
    }

    public void displayBackupSpawn(List<Enemy> backupEnemies) {
        msgOut("");
        msgOut("⚠ BACKUP SPAWN TRIGGERED! ⚠");
        msgOut("  New enemies enter the battlefield:");
        for (Enemy e : backupEnemies) {
            msgOut("  • " + e.getName() + " (HP: " + e.getHp() + " | ATK: " +
                             e.getAttack() + " | DEF: " + e.getDefense() + " | SPD: " + e.getSpeed() + ")");
        }
        msgOut("");
    }

    public void displayVictory(int remainingHp, int maxHp, int totalRounds, Player player) {
        msgOut("");
        msgOut("╔══════════════════════════════════════════════════╗");
        msgOut("║                     VICTORY!                     ║");
        msgOut("╠══════════════════════════════════════════════════╣");
        msgOut("║  Congratulations, you have defeated all your     ║");
        msgOut("║  enemies.                                        ║");
        msgOut("╠══════════════════════════════════════════════════╣");
        msgOut("  Remaining HP: " + remainingHp + "/" + maxHp);
        msgOut("  Total Rounds: " + totalRounds);
        // Display remaining items
        if (player.hasItems()) {
            System.out.print("  Remaining Items: ");
            for (Item item : player.getInventory()) {
                System.out.print(item.getName() + " ");
            }
            msgOut("");
        }
        System.out.println("╚══════════════════════════════════════════════════╝");
    }

    public void displayDefeat(int enemiesRemaining, int totalRounds) {
        msgOut();
        msgOut("╔══════════════════════════════════════════════════╗");
        msgOut("║                     DEFEAT                       ║");
        msgOut("╠══════════════════════════════════════════════════╣");
        msgOut("║  Defeated. Don't give up, try again!            ║");
        msgOut("╠══════════════════════════════════════════════════╣");
        msgOut("  Enemies Remaining: " + enemiesRemaining);
        msgOut("  Total Rounds Survived: " + totalRounds);
        msgOut("╚══════════════════════════════════════════════════╝");
    }

    public int getReplayChoice() {
        msgOut();
        msgOut("  What would you like to do?");
        msgOut("  [1] Replay with same settings");
        msgOut("  [2] Start a new game");
        msgOut("  [3] Exit");
        return getValidInput("  > ", 1, 3);
    }


    public void msgOut(String message) {
        System.out.println(message);
    }

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
