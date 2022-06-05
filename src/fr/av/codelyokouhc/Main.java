package fr.av.codelyokouhc;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import fr.av.codelyokouhc.commands.*;
import fr.av.codelyokouhc.enums.GRoles;
import fr.av.codelyokouhc.enums.GState;
import fr.av.codelyokouhc.listeners.DammageListeners;
import fr.av.codelyokouhc.listeners.HealthListeners;
import fr.av.codelyokouhc.listeners.PlayerListeners;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin {
    private GState state;
    private Location gameSpawn;
    private Location lyokoSpawn;
    private Map<Player, GRoles> roles = new HashMap<>();
    private List<GRoles> nonAttribuateRoles = new ArrayList<>();
    private List<Player> inLyokoPlayer = new ArrayList<>();

    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Code Lyoko est pret !");

        getCommand("tpspawn").setExecutor(new TpSpawnCommand());
        getCommand("startGame").setExecutor(new StartGameCommand(this));
        getCommand("setGameSpawn").setExecutor(new SetGameSpawnCommand(this));
        getCommand("setLyokoSpawn").setExecutor(new SetLyokoSpawnCommand(this));
        getCommand("overworld").setExecutor(new OverworldCommand(this));
        getCommand("generateUsine").setExecutor(new GenerateUsineCommand(this));


        setState(GState.WAITINGPLAYERS);
        nonAttribuateRoles.add(GRoles.AelitaSchaeffer);
        nonAttribuateRoles.add(GRoles.FranzHoppe);
        nonAttribuateRoles.add(GRoles.JeanPierreDelmas);
        nonAttribuateRoles.add(GRoles.JeremyBelpois);
        nonAttribuateRoles.add(GRoles.JimMoralés);
        nonAttribuateRoles.add(GRoles.MillySolovieff);
        nonAttribuateRoles.add(GRoles.TamiyaDiop);
        nonAttribuateRoles.add(GRoles.Odd);
        nonAttribuateRoles.add(GRoles.Kiwi);
        nonAttribuateRoles.add(GRoles.Hervé);
        nonAttribuateRoles.add(GRoles.Nicolas);
        nonAttribuateRoles.add(GRoles.Sisi);
        nonAttribuateRoles.add(GRoles.SuzanneHertz);
        nonAttribuateRoles.add(GRoles.UlrichStern);
        nonAttribuateRoles.add(GRoles.WilliamDunba);
        nonAttribuateRoles.add(GRoles.YumiIshiyama);
        nonAttribuateRoles.add(GRoles.MèreDeYumi);
        nonAttribuateRoles.add(GRoles.PèreDeYumi);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListeners(this), this);
        pm.registerEvents(new DammageListeners(this), this);
        pm.registerEvents(new HealthListeners(this), this);
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
    public Location getLyokoSpawn() {
        return lyokoSpawn;
    }
    public void setLyokoSpawn(Location lyokoSpawn) {
        this.lyokoSpawn = lyokoSpawn;
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

    public void addPlayerLyoko(Player player){
        if(!inLyokoPlayer.contains(player)) inLyokoPlayer.add(player);
    }

    public void removePlayerLyoko(Player player){
        if(inLyokoPlayer.contains(player)) inLyokoPlayer.remove(player);
    }

    public boolean isInLyoko(Player player){
        if(inLyokoPlayer.contains(player)) return true;
        return false;
    }

    public WorldEditPlugin getWorldEdit(){
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if(p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
        else return null;
    }
}
