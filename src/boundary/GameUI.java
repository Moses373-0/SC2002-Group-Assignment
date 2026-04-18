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
    System.out.println("""
          ╔══════════════════════════════════════════════════╗
          ║         TURN-BASED COMBAT ARENA                  ║
          ║              SC2002 Assignment                   ║
          ╚══════════════════════════════════════════════════╝

        """);
  }

  public void displayPlayerOptions() {
    System.out.println("""
          ═══════════════ SELECT YOUR PLAYER ═══════════════

            [1] Warrior
                HP: 260 | ATK: 40 | DEF: 20 | SPD: 30
                Special: Shield Bash - Damage + Stun (2 turns)
            [2] Wizard
                HP: 200 | ATK: 50 | DEF: 10 | SPD: 20
                Special: Arcane Blast - AoE damage + ATK buff on kills

        """);
  }

  public int selectPlayer() {
    displayPlayerOptions();
    return getValidInput("Choose your player (1-2): ", 1, 2);
  }

  public void displayItemOptions() {
    System.out.println("""

          ═══════════════ SELECT YOUR ITEMS ═════════════════
            Choose 2 items (duplicates allowed)
            [1] Potion      - Heal 100 HP
            [2] Power Stone - Free special skill use (no cooldown)
            [3] Smoke Bomb  - Enemy attacks deal 0 damage (2 turns)

        """);
  }

  public int selectItem(int itemNumber) {
    return getValidInput("Choose item " + itemNumber + " (1-3): ", 1, 3);
  }

  public void displayDifficultyOptions() {
    System.out.println("""

          ════════════════ SELECT DIFFICULTY ════════════════

            [1] Easy   - 3 Goblins
            [2] Medium - 1 Goblin + 1 Wolf | Backup: 2 Wolves
            [3] Hard   - 2 Goblins | Backup: 1 Goblin + 2 Wolves

            Enemy Stats
              Goblin: HP:55 | ATK:35 | DEF:15 | SPD:25
              Wolf:   HP:40 | ATK:45 | DEF:5  | SPD:35

        """);
  }

  public int selectDifficulty() {
    displayDifficultyOptions();
    return getValidInput("Choose difficulty (1-3): ", 1, 3);
  }

  // =================== TURN ORDER STRATEGY ===================

  public int selectTurnOrderStrategy() {
    System.out.println("""

          ═══════════ SELECT TURN ORDER STRATEGY ════════════

            [1] Speed-Based  - Highest speed goes first
            [2] Player First - Player always acts before enemies

        """);
    return getValidInput("Choose turn order strategy (1-2): ", 1, 2);
  }

  // =================== BATTLE DISPLAY ===================

  public void displayRoundHeader(int roundNumber) {
    System.out.println("""

          ══════════════════   ROUND %-4d════════════════════
        """.formatted(roundNumber));
  }

  public void displayTurnOrder(List<Combatant> turnOrder) {
    System.out.print("  Turn Order: ");
    for (int i = 0; i < turnOrder.size(); i++) {
      Combatant c = turnOrder.get(i);
      System.out.print(c.getName() + " (SPD:" + c.getSpeed() + ")");
      if (i < turnOrder.size() - 1)
        System.out.print(" → ");
    }
    System.out.println();
    System.out.println();
  }

  public void displayCombatantStatus(Player player, List<Enemy> enemies) {
    System.out.println("── Status ──────────────────────────────────────────");

    System.out.println(formatCombatantStats(player));

    // Items
    System.out.println("    Items: " + formatPlayerItems(player));

    // Cooldown
    if (player.getSpecialSkillCooldown() > 0) {
      System.out.println("    Special Skill Cooldown: " + player.getSpecialSkillCooldown() + " rounds");
    } else {
      System.out.println("    Special Skill: READY");
    }

    System.out.println();

    for (Enemy enemy : enemies) {
      if (enemy.isAlive()) {
        System.out.println(formatCombatantStats(enemy));
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
      System.out.printf("  [%d] %s (HP: %d/%d)\n", i + 1, e.getName(), e.getHp(), e.getMaxHp());
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
    System.out.println("""

          ⚠ BACKUP SPAWN TRIGGERED! ⚠
          New enemies enter the battlefield:
        """);
    for (Enemy e : backupEnemies) {
      System.out.println(formatCombatantStats(e));
    }
    System.out.println();
  }

  // =================== GAME END ===================

  public void displayVictory(int remainingHp, int maxHp, int totalRounds, Player player) {
    System.out.println("""
          ╔══════════════════════════════════════════════════╗
          ║              🏆 VICTORY! 🏆                      ║
          ╠══════════════════════════════════════════════════╣
          ║  Congratulations, you have defeated all your     ║
          ║  enemies.                                        ║
          ╠══════════════════════════════════════════════════╣
          ║  Remaining HP:    %-5d/%-25d║
          ║  Total Rounds:    %-31d║
          ║  Remaining Items: %-31s║
          ╚══════════════════════════════════════════════════╝
        """.formatted(remainingHp, maxHp, totalRounds, formatPlayerItems(player)));
  }

  public void displayDefeat(int enemiesRemaining, int totalRounds) {
    System.out.println("""
          ╔══════════════════════════════════════════════════╗
          ║                💀 DEFEAT 💀                      ║
          ╠══════════════════════════════════════════════════╣
          ║  Defeated. Don't give up, try again!             ║
          ╠══════════════════════════════════════════════════╣
          ║  Enemies Remaining:     %-25d║
          ║  Total Rounds Survived: %-25d║
          ╚══════════════════════════════════════════════════╝
        """.formatted(enemiesRemaining, totalRounds));
  }

  public void displayBattleStart(Player player, Difficulty difficulty) {
    System.out.println("""
          ╔═══════════════ BATTLE START ═════════════════════╗
          ║   Player:     %-35s║
          ║   Items:      %-35s║
          ║   Difficulty: %-35s║
          ╚══════════════════════════════════════════════════╝
        """.formatted(player.getName(), formatPlayerItems(player), difficulty.getDisplayName()));
  }

  public int getReplayChoice() {
    System.out.println("""
        What would you like to do?
          [1] Replay with same settings
          [2] Start a new game
          [3] Exit
        """);
    return getValidInput("  > ", 1, 3);
  }

  // =================== ROUND END ===================

  public void displayRoundEnd(int roundNumber, Player player, List<Enemy> enemies) {
    System.out.println();
    System.out.println("── End of Round " + roundNumber + " ──");

    System.out.println(formatCombatantStats(player));

    if (player.getSpecialSkillCooldown() > 0) {
      System.out.println(" | Cooldown: " + player.getSpecialSkillCooldown());
    }

    for (Enemy e : enemies) {
      if (e.isAlive()) {
        System.out.println(formatCombatantStats(e));
      } else {
        System.out.println("  " + e.getName() + " ✗ ELIMINATED");
      }
    }

    System.out.printf("  Items: %s\n", formatPlayerItems(player));
  }

  public void displayGameEnd() {
    System.out.println();
    System.out.println("Thanks for playing! Goodbye.");
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

  private String formatPlayerItems(Player player) {
    if (!player.hasItems())
      return "None";

    StringBuilder sb = new StringBuilder();
    for (Item item : player.getInventory()) {
      sb.append("[" + item.getName() + "] ");
    }
    return sb.toString().trim();
  }

  private String formatCombatantStats(Combatant c) {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format(
        "  %-10s HP:%-3d/%-3d | ATK:%-3d | DEF:%-3d | SPD:%-3d",
        c.getName(),
        c.getHp(),
        c.getMaxHp(),
        c.getAttack(),
        c.getDefense(),
        c.getSpeed()));

    // Add in active effects
    if (!c.getActiveEffects().isEmpty()) {
      sb.append(" | EFF: ");
      for (StatusEffect e : c.getActiveEffects()) {
        sb.append("[")
            .append(e.getName())
            .append(" ")
            .append(e.getDuration())
            .append("t] ");
      }
    }

    return sb.toString().trim();
  }
}
