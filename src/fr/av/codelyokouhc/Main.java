package fr.av.codelyokouhc;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import fr.av.codelyokouhc.commands.*;
import fr.av.codelyokouhc.enums.GRoles;
import fr.av.codelyokouhc.enums.GState;
import fr.av.codelyokouhc.listeners.DammageListeners;
import fr.av.codelyokouhc.listeners.HealthListeners;
import fr.av.codelyokouhc.listeners.PlayerListeners;
import fr.av.codelyokouhc.loops.RemoveKilledPlayerLoop;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Main extends JavaPlugin {
    private GState state;
    private Location gameSpawn;
    private Location lyokoSpawn;
    private Map<Player, GRoles> roles = new HashMap<>();
    private List<GRoles> nonAttribuateRoles = new ArrayList<>();
    private List<Player> inLyokoPlayer = new ArrayList<>();
    private int episode = 0;

    //Reset when episode change
    private boolean jeremyCanVanish = true;

    public int ulrichHide = 10;
    public int jeremyRevive = 2;
    public Map<Player, Location> killedPlayer = new HashMap<>();

    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Code Lyoko est pret !");
        if(Bukkit.getAllowNether()) System.out.println("/!\\WARNING/!\\ Nether is enable !");

        getCommand("tpspawn").setExecutor(new TpSpawnCommand());
        getCommand("startGame").setExecutor(new StartGameCommand(this));
        getCommand("setGameSpawn").setExecutor(new SetGameSpawnCommand(this));
        getCommand("setLyokoSpawn").setExecutor(new SetLyokoSpawnCommand(this));
        getCommand("overworld").setExecutor(new OverworldCommand(this));
        getCommand("getRole").setExecutor(new GetRoleCommand(this));
        getCommand("hide").setExecutor(new HideCommand(this));
        getCommand("revive").setExecutor(new ReviveCommand(this));

        Location factorySpawn = new Location(getServer().getWorld("world"), new Random().nextInt(500 - (-500)) + (-500), 0, new Random().nextInt(500 - (-500)) + (-500));
        int y = factorySpawn.getWorld().getHighestBlockYAt(factorySpawn);
        factorySpawn = new Location(factorySpawn.getWorld(), factorySpawn.getX(), y, factorySpawn.getZ());
        System.out.println(factorySpawn);
        generateFactory(factorySpawn);

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
    public int getEpisode() {
        return episode;
    }
    public void setEpisode(int episode) {
        this.episode = episode;
    }
    public boolean jeremyCanVanish() {
        return jeremyCanVanish;
    }
    public void setJeremyCanVanish(boolean jeremyCanVanish) {
        this.jeremyCanVanish = jeremyCanVanish;
    }

    public void eliminate(Player player){
        killedPlayer.put(player, player.getLocation());
        RemoveKilledPlayerLoop rkpl = new RemoveKilledPlayerLoop(this, player);
        rkpl.runTaskTimer(this, 1,20);
        player.setGameMode(GameMode.SPECTATOR);
        player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.0f);
        Bukkit.broadcastMessage("§c====================");
        if(roles.containsKey(player)){
            Bukkit.broadcastMessage("§e" + player.getDisplayName() + " a été tué ! Il était " + roles.get(player).toString());
        }else{
            Bukkit.broadcastMessage("§e" + player.getDisplayName() + " est mort");
        }
        Bukkit.broadcastMessage("§c====================");

        //Special DeathEvents
        if(roles.get(player) == GRoles.Odd && getPlayerByRole(GRoles.Kiwi) != null && getPlayerByRole(GRoles.Kiwi).getGameMode() == GameMode.SURVIVAL){
            Player kiwi = getPlayerByRole(GRoles.Kiwi);
            kiwi.setMaxHealth(16);
            kiwi.setHealth(16);
            kiwi.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 999999999,0, false, false));
            getPlayerByRole(GRoles.Kiwi).sendMessage("§eOdd est mort, votre boussole pointe désormais le 0 0");
        }
        if(roles.get(player) == GRoles.Kiwi && getPlayerByRole(GRoles.Odd) != null && getPlayerByRole(GRoles.Odd).getGameMode() == GameMode.SURVIVAL){
            if(playerIsAt(player, getPlayerByRole(GRoles.Odd), 30)) getPlayerByRole(GRoles.Odd).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999999,1, false, false));
        }
        if(roles.get(player) == GRoles.YumiIshiyama){
            if(getPlayerByRole(GRoles.PèreDeYumi) == null && getPlayerByRole(GRoles.PèreDeYumi).getGameMode() == GameMode.SURVIVAL) getPlayerByRole(GRoles.PèreDeYumi).sendMessage("§eYumi est morte, votre boussole pointe désormais le 0 0");
            if(getPlayerByRole(GRoles.MèreDeYumi) == null && getPlayerByRole(GRoles.MèreDeYumi).getGameMode() == GameMode.SURVIVAL) getPlayerByRole(GRoles.MèreDeYumi).sendMessage("§eYumi est morte, votre boussole pointe désormais le 0 0");
        }

        if(roles.get(player) == GRoles.MèreDeYumi || roles.get(player) == GRoles.PèreDeYumi){
            if(getPlayerByRole(GRoles.YumiIshiyama) != null && getPlayerByRole(GRoles.YumiIshiyama).getGameMode() == GameMode.SURVIVAL) {
                Player yumi = getPlayerByRole(GRoles.YumiIshiyama);
                yumi.setMaxHealth(yumi.getMaxHealth() - 6);
                yumi.setHealth(yumi.getMaxHealth());
            }
        }
    }

    public Boolean playerIsAt(Player player1, Player player2, float dist){
        return player2.getLocation().distanceSquared(player1.getLocation()) <= (dist * dist);
    }

    public <V> Player getPlayerByRole(V value) {
        Supplier<Stream<Object>> result = () -> getRoles()
                .entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey);

        if(result.get().count() > 0) return (Player) result.get().findFirst().get();
        else return null;
    }

    public boolean isDay(){
        return getServer().getWorld("world").getTime() < 12300 || getServer().getWorld("world").getTime() > 23850;
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

    private void generateFactory(Location location){
        WorldEditPlugin worldEdit = getWorldEdit();
        File shematic = new File(getDataFolder(), "factory.schematic");
        EditSession session = worldEdit.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 999999);
        try{
            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(shematic).load(shematic);
            clipboard.rotate2D(90);
            clipboard.paste(session, new Vector(location.getX(),location.getY(), location.getZ()), false);
        }catch (MaxChangedBlocksException | DataException | IOException e){
            e.printStackTrace();
        }
    }
}
