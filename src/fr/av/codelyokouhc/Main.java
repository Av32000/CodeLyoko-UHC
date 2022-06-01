package fr.av.codelyokouhc;

import fr.av.codelyokouhc.commands.StartGameCommand;
import fr.av.codelyokouhc.commands.TpSpawnCommand;
import fr.av.codelyokouhc.listeners.PlayerListeners;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private GState state;
    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Code Lyoko est pret !");

        getCommand("tpspawn").setExecutor(new TpSpawnCommand());
        getCommand("startGame").setExecutor(new StartGameCommand(this));

        setState(GState.WAITINGPLAYERS);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListeners(this), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        System.out.println("Code Lyoko est éteint !");
    }

    public void setState(GState state) {
        this.state = state;
    }

    public boolean isState(GState state){
        return this.state == state;
    }
}
