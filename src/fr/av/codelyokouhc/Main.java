package fr.av.codelyokouhc;

import fr.av.codelyokouhc.commands.SetGameSpawnCommand;
import fr.av.codelyokouhc.commands.StartGameCommand;
import fr.av.codelyokouhc.commands.TpSpawnCommand;
import fr.av.codelyokouhc.enums.GRoles;
import fr.av.codelyokouhc.enums.GState;
import fr.av.codelyokouhc.listeners.DammageListeners;
import fr.av.codelyokouhc.listeners.PlayerListeners;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin {
    private GState state;
    private Location gameSpawn;
    private Map<Player, GRoles> roles = new HashMap<>();
    private List<GRoles> nonAttribuateRoles = new ArrayList<>();

    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Code Lyoko est pret !");

        getCommand("tpspawn").setExecutor(new TpSpawnCommand());
        getCommand("startGame").setExecutor(new StartGameCommand(this));
        getCommand("setGameSpawn").setExecutor(new SetGameSpawnCommand(this));

        setState(GState.WAITINGPLAYERS);
        nonAttribuateRoles.add(GRoles.Role1);
        nonAttribuateRoles.add(GRoles.Role2);
        nonAttribuateRoles.add(GRoles.Role3);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListeners(this), this);
        pm.registerEvents(new DammageListeners(this), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        System.out.println("Code Lyoko est eteint !");
    }

    public void setState(GState state) {
        this.state = state;
    }
    public boolean isState(GState state){
        return this.state == state;
    }
    public Location getGameSpawn() {
        return gameSpawn;
    }
    public void setGameSpawn(Location gameSpawn) {
        this.gameSpawn = gameSpawn;
    }
    public Map<Player, GRoles> getRoles() {
        return roles;
    }
    public List<GRoles> getNonAttribuateRoles() {
        return nonAttribuateRoles;
    }


    public void eliminate(Player player){
        player.setGameMode(GameMode.SPECTATOR);
        player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.0f);
        if(roles.containsKey(player)){
            Bukkit.broadcastMessage("§e" + player.getDisplayName() + " a été tué ! Il était " + roles.get(player).toString());
        }else{
            Bukkit.broadcastMessage("§e" + player.getDisplayName() + " est mort");
        }
    }

    public String getPlayerCount() {
        int count = 0;
        for (Player player : getServer().getOnlinePlayers()) {
            if(player.getGameMode() != GameMode.SPECTATOR){
                count++;
            }
        }
        return String.valueOf(count);
    }
}
