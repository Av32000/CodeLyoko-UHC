package fr.av.codelyokouhc;

import fr.av.codelyokouhc.commands.SetGameSpawnCommand;
import fr.av.codelyokouhc.commands.StartGameCommand;
import fr.av.codelyokouhc.commands.TpSpawnCommand;
import fr.av.codelyokouhc.listeners.DammageListeners;
import fr.av.codelyokouhc.listeners.PlayerListeners;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private GState state;
    private Location gameSpawn;
    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Code Lyoko est pret !");

        getCommand("tpspawn").setExecutor(new TpSpawnCommand());
        getCommand("startGame").setExecutor(new StartGameCommand(this));
        getCommand("setGameSpawn").setExecutor(new SetGameSpawnCommand(this));

        setState(GState.WAITINGPLAYERS);

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
}
