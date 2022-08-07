package fr.av.codelyokouhc.listeners;

import com.avaje.ebeaninternal.server.type.RsetDataReader;
import fr.av.codelyokouhc.ScoreboardManagerUtils;
import fr.av.codelyokouhc.enums.GRoles;
import fr.av.codelyokouhc.enums.GState;
import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.loops.DoubleJumpCooldownLoop;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.Vector;

import java.util.*;

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
        player.setLevel(0);
        player.setMaxHealth(20);
        player.setHealth(20);
        RemovePotionEffect(player);
        player.setGameMode(GameMode.ADVENTURE);

        UpdatePLayersScoreBoard();
    }

    private void RemovePotionEffect(Player player){
        for (PotionEffect effect:player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        UpdatePLayersScoreBoard();
        e.setQuitMessage(null);
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
    public void onGenerateBiome(ChunkPopulateEvent e){
        if(e.getChunk().getX() > 125 || e.getChunk().getX() < -125){
            return;
        }
        if(e.getChunk().getZ() > 125 || e.getChunk().getZ() < -125){
            return;
        }
        Chunk chunk = e.getChunk();
        populate(chunk.getWorld(), new Random(), chunk);
    }

    @EventHandler
    public void onTpDim(PlayerPortalEvent e){
        if(e.getTo().getWorld() == main.getServer().getWorld("world_the_end")){
            main.addPlayerLyoko(e.getPlayer());
            e.getPlayer().teleport(main.getLyokoSpawn());
            e.getPlayer().sendMessage("§eBienvenue dans le Lyoko !");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if(e.getPlayer() == main.tamiyaTarget){
            if(e.getMessage().equalsIgnoreCase("oui") || e.getMessage().equalsIgnoreCase("non")){
                String name = main.tamiyaTarget.getDisplayName();
                main.tamiyaTarget = null;
                main.tamiyaAnswerLoop.cancel();
                Player tamiya = main.getPlayerByRole(GRoles.TamiyaDiop);
                tamiya.playSound(tamiya.getLocation(), Sound.LEVEL_UP, 1.0f,1.0f);
                tamiya.sendMessage("§a" + name + "> " + e.getMessage());
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0f,1.0f);
                e.getPlayer().sendMessage("§a" + name + "> " + e.getMessage());
            }
            else{
                e.getPlayer().sendMessage("§cVous devez répondre par oui ou  par non");
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onUseItem(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_AIR){
            return;
        }
        Player p = e.getPlayer();
        if(p.getHealth() < 10 && p.getItemInHand() != null && p.getItemInHand().getType() == Material.COOKED_BEEF && main.getRoles().size() > 0 && main.getRoles().get(p) == GRoles.Odd){
            p.setHealth(p.getHealth() + 1);
            ItemStack newStack = new ItemStack(p.getItemInHand().getType(), p.getItemInHand().getAmount() -1);
            p.getInventory().remove(p.getItemInHand());
            p.getInventory().setItemInHand(newStack);
        }
        if(main.getRoles().get(p) == GRoles.AelitaSchaeffer && p.getItemInHand().getType() == Material.FEATHER && p.getItemInHand().getItemMeta().getLore().size() >= 1 && p.getItemInHand().getItemMeta().getLore().get(0).equalsIgnoreCase("§bCooldown => 30s")){
            if(main.aelitaCanJump){
                main.aelitaCanJump = false;
                Vector v = p.getLocation().getDirection().multiply(1).setY(1);
                p.setVelocity(v);
                DoubleJumpCooldownLoop djcl = new DoubleJumpCooldownLoop(main);
                djcl.runTaskTimer(main,1,20);
            }
        }
        if(main.getRoles().get(p) == GRoles.JeanPierreDelmas && p.getItemInHand().getType() == Material.SKULL_ITEM && p.getItemInHand().getItemMeta().getLore().size() >= 1 && p.getItemInHand().getItemMeta().getLore().get(0).equalsIgnoreCase("§bAccès au lyoko après 60min de jeu !")){
            if(p.getWorld() == main.getServer().getWorld("world")){
                if(main.computerWork){
                    main.addPlayerLyoko(e.getPlayer());
                    e.getPlayer().teleport(main.getLyokoSpawn());
                    e.getPlayer().sendMessage("§eBienvenue dans le Lyoko !");
                }else{
                    p.sendMessage("§cVous devez attendre 1h de jeu pour pouvoir utiliser cette item !");
                }
            }
        }
    }

    public void populate(World world, Random random, Chunk source) {
        int numTrees = 50;

        for (int i = 0; i < numTrees; ++i) {
            int x = random.nextInt(16) + source.getX() * 16;
            int z = random.nextInt(16) + source.getZ() * 16;
            int y = world.getHighestBlockYAt(x, z);

            if (y != 0) {
                world.generateTree(new Location(world, x, y, z), TreeType.TREE);
            }
        }
    }
}
