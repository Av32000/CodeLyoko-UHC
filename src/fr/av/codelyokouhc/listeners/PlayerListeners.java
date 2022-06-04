package fr.av.codelyokouhc.listeners;

import com.sun.javafx.sg.prism.NodeEffectInput;
import fr.av.codelyokouhc.ScoreboardManagerUtils;
import fr.av.codelyokouhc.enums.GState;
import fr.av.codelyokouhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerListeners implements Listener {
    private Main main;
    public PlayerListeners(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if(!main.isState(GState.WAITINGPLAYERS)){
            e.setJoinMessage(null);
            player.kickPlayer("Partie en cours !\nMerci de patienter en attendant la prochaine partie !");
            return;
        }

        Location spawn = Bukkit.getWorld("world").getSpawnLocation();
        player.teleport(spawn);

        org.bukkit.scoreboard.Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("healthCount");
        if (objective == null) {
            objective = board.registerNewObjective("healthCount", "health");
            player.damage(2);
            objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }else{
            objective.unregister();
            objective = board.registerNewObjective("healthCount", "health");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            player.damage(2);
            objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }

        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setHealth(20);
        player.setGameMode(GameMode.ADVENTURE);

        UpdatePLayersScoreBoard();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        UpdatePLayersScoreBoard();
    }

    public void UpdatePLayersScoreBoard(){
        if(main.isState(GState.WAITINGPLAYERS)){
            for (Player player:main.getServer().getOnlinePlayers()) {
                ScoreboardManagerUtils smu = new ScoreboardManagerUtils();
                Map<String,Integer> values = new HashMap<>();
                values.put("", 4);
                values.put("§dAttente de joueurs...", 3);
                values.put(" ", 2);
                if(Integer.parseInt(main.getPlayerCount()) > 1){
                    values.put("§c" + main.getPlayerCount() + " joueurs", 1);
                }else{
                    values.put("§c" + main.getPlayerCount() + " joueur", 1);

                }
                smu.AddScoreBordToPlayer(player, values);
            }
        }
    }

    @EventHandler
    public void onTpDim(PlayerChangedWorldEvent e){
        if(e.getFrom().toString() == "world"){
            return;
        }
        if(e.getPlayer().getWorld().toString() != "world_nether" && e.getPlayer().getWorld().toString() != "world"){
            e.getPlayer().teleport(main.getLyokoSpawn());
            e.getPlayer().sendMessage("§eBienvenue dans le Lyoko !");
            main.addPlayerLyoko(e.getPlayer());
        }
    }
}
