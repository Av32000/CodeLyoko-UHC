package fr.av.codelyokouhc;

import fr.av.codelyokouhc.commands.TpSpawnCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Code Lyoko est pret !");
        getCommand("tpspawn").setExecutor(new TpSpawnCommand());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        System.out.println("Code Lyoko est éteint !");
    }
}
