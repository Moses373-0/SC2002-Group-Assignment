package entity.combatant.enemy;

import entity.combatant.Combatant;
import entity.combatant.player.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Default enemy action: BasicAttack on a random alive player.
 */
public class BasicAttackStrategy implements EnemyActionStrategy {
    private static final Random random = new Random();

    @Override
    public String execute(Enemy enemy, List<Combatant> targets) {
        // Find alive players
        List<Combatant> alivePlayers = new ArrayList<>();
        for (Combatant c : targets) {
            if (c instanceof Player && c.isAlive()) {
                alivePlayers.add(c);
            }
        }

        if (alivePlayers.isEmpty()) {
            return enemy.getName() + " has no targets!";
        }

        // Pick a random alive player (in this game there is only one player)
        Combatant target = alivePlayers.get(random.nextInt(alivePlayers.size()));

        // Check if target has smoke bomb invulnerability
        if (target.hasSmokeBombInvulnerability()) {
            return enemy.getName() + " attacks " + target.getName() +
                   " but Smoke Bomb negates all damage! " + target.getName() +
                   " HP: " + target.getHp() + "/" + target.getMaxHp();
        }

        int damage = Math.max(0, enemy.getAttack() - target.getDefense());
        target.takeDamage(damage);

        StringBuilder sb = new StringBuilder();
        sb.append(enemy.getName()).append(" attacks ").append(target.getName()).append("!");
        sb.append("\n  Damage dealt: ").append(damage);
        sb.append(" (").append(enemy.getAttack()).append(" - ").append(target.getDefense()).append(")");
        sb.append(" | ").append(target.getName()).append(" HP: ").append(target.getHp()).append("/").append(target.getMaxHp());
        if (!target.isAlive()) {
            sb.append(" | ").append(target.getName()).append(" ELIMINATED!");
        }
        return sb.toString();
    }
}
