package fr.av.codelyokouhc.listeners;

import fr.av.codelyokouhc.GState;
import fr.av.codelyokouhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {
    private Main main;
    public PlayerListeners(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        Location spawn = Bukkit.getWorld("world").getSpawnLocation();
        player.teleport(spawn);

        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setHealth(20);
        player.setGameMode(GameMode.ADVENTURE);

        if(!main.isState(GState.WAITINGPLAYERS)){
            player.kickPlayer("Partie en cours !\nMerci de patienter en attendant la prochaine partie !");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){

    }
}
