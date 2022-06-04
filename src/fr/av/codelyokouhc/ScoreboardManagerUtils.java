package fr.av.codelyokouhc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Map;

public class ScoreboardManagerUtils {
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    public void AddScoreBordToPlayer(Player player, Map<String, Integer> values) {
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("Code Lyoko UHC");
        if(objective == null){
            objective = board.registerNewObjective("Code Lyoko UHC", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName("§bCode Lyoko UHC");

            for (String key:values.keySet()) {
                objective.getScore(key).setScore(values.get(key));
            }
        }else{
            objective.unregister();
            objective = board.registerNewObjective("Code Lyoko UHC", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName("§bCode Lyoko UHC");

            for (String key:values.keySet()) {
                objective.getScore(key).setScore(values.get(key));
            }
        }
    }
}
