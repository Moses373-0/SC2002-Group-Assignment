package boundary;

import entity.combatant.Combatant;
import entity.combatant.player.Player;
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
