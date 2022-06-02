package fr.av.codelyokouhc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Map;

public class ScoreboardManagerUtils {
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    public void AddScoreBordToPlayer(Player player, Map<String, Integer> values) {
        Scoreboard board = manager.getNewScoreboard();
        Team team = board.registerNewTeam(player.getDisplayName());
        team.addPlayer(player);
        Objective objective = board.registerNewObjective("Code Lyoko UHC", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§bCode Lyoko UHC");

        for (String key:values.keySet()) {
            objective.getScore(key).setScore(values.get(key));
        }

        player.setScoreboard(board);
    }
}
