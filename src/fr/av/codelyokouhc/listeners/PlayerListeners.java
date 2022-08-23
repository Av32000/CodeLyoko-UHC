package fr.av.codelyokouhc.listeners;

import com.avaje.ebeaninternal.server.type.RsetDataReader;
import fr.av.codelyokouhc.ScoreboardManagerUtils;
import fr.av.codelyokouhc.enums.GRoles;
import fr.av.codelyokouhc.enums.GState;
import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.loops.DoubleJumpCooldownLoop;
import fr.av.codelyokouhc.loops.FlyLoops;
import fr.av.codelyokouhc.loops.OpenInventoryCooldownLoop;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;
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

        main.ClearPlayer(player);
        RemovePotionEffect(player);
        player.setGameMode(GameMode.ADVENTURE);

        if(Bukkit.getAllowNether()) player.sendMessage("§c/!\\WARNING/!\\ Nether is enable !");

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
                    p.sendMessage("§cVous devez attendre 1h de jeu pour pouvoir utiliser cet item !");
                }
            }
        }

        if(main.spectres.contains(p) && p.getItemInHand().getType() == Material.FEATHER && p.getItemInHand().getItemMeta() != null && p.getItemInHand().getItemMeta().getLore() != null && p.getItemInHand().getItemMeta().getLore().size() >= 1 && p.getItemInHand().getItemMeta().getLore().get(0).equalsIgnoreCase("§bVous permet de Fly pendant 5s")){
            p.setAllowFlight(true);
            FlyLoops fl = new FlyLoops(main,p);
            fl.runTaskTimer(main,1,20);
            p.getInventory().remove(p.getItemInHand());
        }

        if(main.getRoles().get(p) == GRoles.MillySolovieff && p.getItemInHand().getType() == Material.SKULL_ITEM && p.getItemInHand().getItemMeta().getLore().size() >= 1 && p.getItemInHand().getItemMeta().getLore().get(0).equalsIgnoreCase("§bVoir la moitié de l'inventaire d'un joueur.")){
            //Find Inventory Size
            List<Player> players = new ArrayList<>();
            int invSize = 0;
            for (Player ps: main.getServer().getOnlinePlayers()) {
                if (ps.getGameMode() == GameMode.SURVIVAL) players.add(ps);
            }

            if(players.size() <= 9){
                invSize = 9;
            }else if(players.size() <= 18){
                invSize = 18;
            }else if(players.size() <= 27){
                invSize = 27;
            }else if(players.size() <= 36){
                invSize = 36;
            }else if(players.size() <= 45){
                invSize = 45;
            }else if(players.size() <= 54){
                invSize = 54;
            }else{
                invSize = 90;
            }

            //Create Inventory
            Inventory playerListInventory = Bukkit.createInventory(null, invSize, "Liste des Joueurs");

            //Add Player Head To Inventory
            for (Player ps : players) {
                playerListInventory.addItem(getPlayerHead(ps));
            }

            p.openInventory(playerListInventory);
        }

        if(e.getMaterial() == Material.NETHER_STAR){
            if(main.lyokoBoostedPlayer.containsKey(p)){
                main.lyokoBoostedPlayer.replace(p, main.lyokoBoostedPlayer.get(p) + 1);
            }else{
                main.lyokoBoostedPlayer.put(p,1);
            }
            p.sendMessage("§cLa puissance du Lyoko entre en vous et boost vos dégats de " + main.lyokoBoostedPlayer.get(p) * 20 + "% !");
            p.getInventory().remove(e.getItem());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        ItemStack current = e.getCurrentItem();

        if(current == null || current.getType() == Material.AIR) return;

        if(inv.getName().equalsIgnoreCase("Liste des Joueurs")){
            e.setCancelled(true);
            if(current.getItemMeta() != null && current.getItemMeta().getDisplayName() != null && current.getItemMeta().getDisplayName().startsWith("§aInventaire de ")){
                p.closeInventory();
                if(main.millyCanShow) {
                    OpenHalfInventory(current.getItemMeta().getDisplayName().split("Inventaire de ")[1], p);
                    main.millyCanShow = false;
                    OpenInventoryCooldownLoop oicl = new OpenInventoryCooldownLoop(main);
                    oicl.runTaskTimer(main,1,20);
                }
                else p.sendMessage("§cVous devez attendre 13 min entre chaque utilisation de cet item !");
            }
        }

        if(inv.getName().equalsIgnoreCase("Menu Spectateur")){
            e.setCancelled(true);
            p.closeInventory();
            p.teleport(main.getServer().getPlayer(current.getItemMeta().getDisplayName().split("§a")[1]).getLocation());
        }

        if(inv.getName().startsWith("Inventaire de")){
            e.setCancelled(true);
        }

        if(inv.getName().equalsIgnoreCase("Configuration")){
            e.setCancelled(true);
            if(nameIs(current,"§cQuitter")) p.closeInventory();
            if(nameIs(current,"§eGestion des Timers")){
                Inventory invc = main.generateConfigInventory("Configuration des Timers", 27);

                ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
                ItemMeta swordItemMeta = sword.getItemMeta();
                swordItemMeta.setDisplayName("§3Temps avant PVP");
                sword.setItemMeta(swordItemMeta);

                ItemStack book = new ItemStack(Material.BOOK);
                ItemMeta bookItemMeta = book.getItemMeta();
                bookItemMeta.setDisplayName("§eTemps avant Rôles");
                book.setItemMeta(bookItemMeta);

                invc.setItem(12, sword);
                invc.setItem(14, book);

                p.closeInventory();
                p.openInventory(invc);
            }
            if(nameIs(current, "§dGestion des Rôles")){
                Inventory rolesInv = main.generateConfigInventory("Configuration des Rôles", 45);

                rolesInv = GenerateRolesConfigInv(rolesInv);

                p.closeInventory();
                p.openInventory(rolesInv);
            }

            if(nameIs(current, "§aGestion de la WorldBorder")){
                Inventory gw = main.generateConfigInventory("Gestion de la WorldBorder", 27);

                ItemStack magentaWool = new ItemStack(Material.WOOL,1, (short) 2);
                ItemMeta magentaItemMeta = magentaWool.getItemMeta();
                magentaItemMeta.setDisplayName("§cRetirer 100 blocs");
                magentaWool.setItemMeta(magentaItemMeta);

                ItemStack purpleWool = new ItemStack(Material.WOOL,1, (short) 10);
                ItemMeta purpleItemMeta = purpleWool.getItemMeta();
                purpleItemMeta.setDisplayName("§cRetirer 10 blocs");
                purpleWool.setItemMeta(purpleItemMeta);

                ItemStack pinkWool = new ItemStack(Material.WOOL,1, (short) 6);
                ItemMeta pinkItemMeta = pinkWool.getItemMeta();
                pinkItemMeta.setDisplayName("§cRetirer 1 bloc");
                pinkWool.setItemMeta(pinkItemMeta);

                ItemStack skyWool = new ItemStack(Material.WOOL,1, (short) 3);
                ItemMeta skyWoolItemMeta = skyWool.getItemMeta();
                skyWoolItemMeta.setDisplayName("§aAjouter 1 bloc");
                skyWool.setItemMeta(skyWoolItemMeta);

                ItemStack cyanWool = new ItemStack(Material.WOOL,1, (short) 9);
                ItemMeta cyanWoolItemMeta = cyanWool.getItemMeta();
                cyanWoolItemMeta.setDisplayName("§aAjouter 10 blocs");
                cyanWool.setItemMeta(cyanWoolItemMeta);

                ItemStack darkBlueWool = new ItemStack(Material.WOOL,1, (short) 11);
                ItemMeta darkBlueWoolItemMeta = darkBlueWool.getItemMeta();
                darkBlueWoolItemMeta.setDisplayName("§aAjouter 100 blocs");
                darkBlueWool.setItemMeta(darkBlueWoolItemMeta);

                ItemStack brick = new ItemStack(Material.LEVER);
                ItemMeta brickItemMeta = brick.getItemMeta();
                brickItemMeta.setDisplayName("§bTaille de la bordure : §a" + main.worldBorder + " blocs");
                brick.setItemMeta(brickItemMeta);

                gw.setItem(10, magentaWool);
                gw.setItem(11, purpleWool);
                gw.setItem(12, pinkWool);
                gw.setItem(13, brick);
                gw.setItem(14, skyWool);
                gw.setItem(15, cyanWool);
                gw.setItem(16, darkBlueWool);

                p.closeInventory();
                p.openInventory(gw);
            }
        }

        if(inv.getName().equalsIgnoreCase("Configuration des Rôles")){
            e.setCancelled(true);
            if(nameIs(current,"§cQuitter")) {
                p.closeInventory();
                p.performCommand("config");
                return;
            }
            if(nameIs(current," ")) return;
            if(current.getItemMeta().getDisplayName().startsWith("§a")){
                main.disabledRoles.add(GRoles.valueOf(current.getItemMeta().getDisplayName().split("§a")[1]));

                ItemStack redWool = new ItemStack(Material.WOOL,1, (short) 14);
                ItemMeta redWoolItemMeta = redWool.getItemMeta();
                redWoolItemMeta.setDisplayName("§c" + current.getItemMeta().getDisplayName().split("§a")[1]);
                redWool.setItemMeta(redWoolItemMeta);

                inv.setItem(Arrays.asList(inv.getContents()).indexOf(current), redWool);
                p.updateInventory();
            } else if (current.getItemMeta().getDisplayName().startsWith("§c")) {
                main.disabledRoles.remove(GRoles.valueOf(current.getItemMeta().getDisplayName().split("§c")[1]));

                ItemStack limeWool = new ItemStack(Material.WOOL,1, (short) 5);
                ItemMeta limeWoolItemMeta = limeWool.getItemMeta();
                limeWoolItemMeta.setDisplayName("§a" + GRoles.valueOf(current.getItemMeta().getDisplayName().split("§c")[1]));
                limeWool.setItemMeta(limeWoolItemMeta);

                inv.setItem(Arrays.asList(inv.getContents()).indexOf(current), limeWool);
                p.updateInventory();
            }

        }


        if(inv.getName().equalsIgnoreCase("Configuration des Timers")){
            e.setCancelled(true);
            if(nameIs(current,"§cQuitter")) {
                p.closeInventory();
                p.performCommand("config");
            }
            if(nameIs(current, "§3Temps avant PVP")){
                Inventory configPVPInv = main.generateConfigInventory("Temps avant PVP", 27);

                ItemStack redWool = new ItemStack(Material.WOOL,1, (short) 14);
                ItemMeta redWoolItemMeta = redWool.getItemMeta();
                redWoolItemMeta.setDisplayName("§cRetirer 1 minute");
                redWool.setItemMeta(redWoolItemMeta);

                ItemStack limeWool = new ItemStack(Material.WOOL,1, (short) 5);
                ItemMeta limeWoolItemMeta = limeWool.getItemMeta();
                limeWoolItemMeta.setDisplayName("§aAjouter 1 minute");
                limeWool.setItemMeta(limeWoolItemMeta);

                ItemStack clock = new ItemStack(Material.WATCH);
                ItemMeta clockMeta = clock.getItemMeta();;
                clockMeta.setDisplayName("§bTemps avant PVP : §a" + main.timeBeforePVP + " minutes");
                clock.setItemMeta(clockMeta);

                configPVPInv.setItem(11, redWool);
                configPVPInv.setItem(13, clock);
                configPVPInv.setItem(15, limeWool);

                p.closeInventory();
                p.openInventory(configPVPInv);
            }
            if(nameIs(current, "§eTemps avant Rôles")){
                Inventory configRolesInv = main.generateConfigInventory("Temps avant Rôles", 27);

                ItemStack redWool = new ItemStack(Material.WOOL,1, (short) 14);
                ItemMeta redWoolItemMeta = redWool.getItemMeta();
                redWoolItemMeta.setDisplayName("§cRetirer 1 minute");
                redWool.setItemMeta(redWoolItemMeta);

                ItemStack limeWool = new ItemStack(Material.WOOL,1, (short) 5);
                ItemMeta limeWoolItemMeta = limeWool.getItemMeta();
                limeWoolItemMeta.setDisplayName("§aAjouter 1 minute");
                limeWool.setItemMeta(limeWoolItemMeta);

                ItemStack clock = new ItemStack(Material.WATCH);
                ItemMeta clockMeta = clock.getItemMeta();;
                clockMeta.setDisplayName("§bTemps avant Rôles : §a" + main.timeBeforeRoles + " minutes");
                clock.setItemMeta(clockMeta);

                configRolesInv.setItem(11, redWool);
                configRolesInv.setItem(13, clock);
                configRolesInv.setItem(15, limeWool);

                p.closeInventory();
                p.openInventory(configRolesInv);
            }
        }

        if(inv.getName().equalsIgnoreCase("Temps avant PVP")){
            e.setCancelled(true);
            if(nameIs(current,"§cQuitter")) {
                p.closeInventory();
                p.performCommand("config");
            }
            if(nameIs(current, "§aAjouter 1 minute")){
                main.timeBeforePVP ++;

                ItemStack clock = new ItemStack(Material.WATCH);
                ItemMeta clockMeta = clock.getItemMeta();;
                clockMeta.setDisplayName("§bTemps avant PVP : §a" + main.timeBeforePVP + " minutes");
                clock.setItemMeta(clockMeta);

                inv.setItem(13,clock);
                p.updateInventory();
            }
            if(nameIs(current, "§cRetirer 1 minute")){
                if(main.timeBeforePVP == 1){
                    p.sendMessage("§cVous en pouvez pas descendre en dessous de 1 minute !");
                    return;
                }
                main.timeBeforePVP --;

                ItemStack clock = new ItemStack(Material.WATCH);
                ItemMeta clockMeta = clock.getItemMeta();;
                clockMeta.setDisplayName("§bTemps avant PVP : §a" + main.timeBeforePVP + " minutes");
                clock.setItemMeta(clockMeta);

                inv.setItem(13,clock);
                p.updateInventory();
            }
        }

        if(inv.getName().equalsIgnoreCase("Gestion de la WorldBorder")){
            e.setCancelled(true);
            if(nameIs(current,"§cQuitter")) {
                p.closeInventory();
                p.performCommand("config");
            }
            if(nameIs(current, "§aAjouter 1 bloc")){
                main.worldBorder ++;

                ItemStack brick = new ItemStack(Material.LEVER);
                ItemMeta brickItemMeta = brick.getItemMeta();;
                brickItemMeta.setDisplayName("§bTaille de la bordure : §a" + main.worldBorder + " blocs");
                brick.setItemMeta(brickItemMeta);

                inv.setItem(13,brick);
                p.updateInventory();
                main.setWorldBorder(main.worldBorder);
            }
            if(nameIs(current, "§aAjouter 10 blocs")){
                main.worldBorder = main.worldBorder + 10;

                ItemStack brick = new ItemStack(Material.LEVER);
                ItemMeta brickItemMeta = brick.getItemMeta();;
                brickItemMeta.setDisplayName("§bTaille de la bordure : §a" + main.worldBorder + " blocs");
                brick.setItemMeta(brickItemMeta);

                inv.setItem(13,brick);
                p.updateInventory();
                main.setWorldBorder(main.worldBorder);
            }
            if(nameIs(current, "§aAjouter 100 blocs")){
                main.worldBorder = main.worldBorder + 100;

                ItemStack brick = new ItemStack(Material.LEVER);
                ItemMeta brickItemMeta = brick.getItemMeta();;
                brickItemMeta.setDisplayName("§bTaille de la bordure : §a" + main.worldBorder + " blocs");
                brick.setItemMeta(brickItemMeta);

                inv.setItem(13,brick);
                p.updateInventory();
                main.setWorldBorder(main.worldBorder);
            }
            if(nameIs(current, "§cRetirer 1 bloc")){
                if(main.worldBorder == 1){
                    p.sendMessage("§cVous ne pouvez pas descendre en dessous de 1 bloc !");
                    return;
                }
                main.worldBorder --;

                ItemStack brick = new ItemStack(Material.LEVER);
                ItemMeta brickItemMeta = brick.getItemMeta();;
                brickItemMeta.setDisplayName("§bTaille de la bordure : §a" + main.worldBorder + " blocs");
                brick.setItemMeta(brickItemMeta);

                inv.setItem(13,brick);
                p.updateInventory();
                main.setWorldBorder(main.worldBorder);
            }
            if(nameIs(current, "§cRetirer 10 blocs")){
                if(main.worldBorder < 11){
                    p.sendMessage("§cVous ne pouvez pas descendre en dessous de 1 bloc !");
                    return;
                }
                main.worldBorder = main.worldBorder - 10;

                ItemStack brick = new ItemStack(Material.LEVER);
                ItemMeta brickItemMeta = brick.getItemMeta();;
                brickItemMeta.setDisplayName("§bTaille de la bordure : §a" + main.worldBorder + " blocs");
                brick.setItemMeta(brickItemMeta);

                inv.setItem(13,brick);
                p.updateInventory();
                main.setWorldBorder(main.worldBorder);
            }
            if(nameIs(current, "§cRetirer 100 blocs")){
                if(main.worldBorder < 101){
                    p.sendMessage("§cVous ne pouvez pas descendre en dessous de 1 bloc !");
                    return;
                }
                main.worldBorder = main.worldBorder - 100;

                ItemStack brick = new ItemStack(Material.LEVER);
                ItemMeta brickItemMeta = brick.getItemMeta();;
                brickItemMeta.setDisplayName("§bTaille de la bordure : §a" + main.worldBorder + " blocs");
                brick.setItemMeta(brickItemMeta);

                inv.setItem(13,brick);
                p.updateInventory();
                main.setWorldBorder(main.worldBorder);
            }
        }

        if(inv.getName().equalsIgnoreCase("Temps avant Rôles")){
            e.setCancelled(true);
            if(nameIs(current,"§cQuitter")) {
                p.closeInventory();
                p.performCommand("config");
            }
            if(nameIs(current, "§aAjouter 1 minute")){
                main.timeBeforeRoles ++;

                ItemStack clock = new ItemStack(Material.WATCH);
                ItemMeta clockMeta = clock.getItemMeta();;
                clockMeta.setDisplayName("§bTemps avant Rôles : §a" + main.timeBeforeRoles + " minutes");
                clock.setItemMeta(clockMeta);

                inv.setItem(13,clock);
                p.updateInventory();
            }
            if(nameIs(current, "§cRetirer 1 minute")){
                if(main.timeBeforeRoles == 1){
                    p.sendMessage("§cVous en pouvez pas descendre en dessous de 1 minute !");
                    return;
                }
                main.timeBeforeRoles --;

                ItemStack clock = new ItemStack(Material.WATCH);
                ItemMeta clockMeta = clock.getItemMeta();;
                clockMeta.setDisplayName("§bTemps avant Rôles : §a" + main.timeBeforeRoles + " minutes");
                clock.setItemMeta(clockMeta);

                inv.setItem(13,clock);
                p.updateInventory();
            }
        }
    }

    public Inventory GenerateRolesConfigInv(Inventory inv){
        for (GRoles role : main.getNonAttribuateRoles()) {
            if (role != GRoles.Agent) {
                if(main.disabledRoles.contains(role)){
                    ItemStack redWool = new ItemStack(Material.WOOL,1, (short) 14);
                    ItemMeta redWoolItemMeta = redWool.getItemMeta();
                    redWoolItemMeta.setDisplayName("§c" + role);
                    redWool.setItemMeta(redWoolItemMeta);

                    inv.addItem(redWool);
                }else{
                    ItemStack limeWool = new ItemStack(Material.WOOL,1, (short) 5);
                    ItemMeta limeWoolItemMeta = limeWool.getItemMeta();
                    limeWoolItemMeta.setDisplayName("§a" + role);
                    limeWool.setItemMeta(limeWoolItemMeta);

                    inv.addItem(limeWool);
                }
            }
        }

        return inv;
    }

    public boolean nameIs(ItemStack item, String name){
        if(item != null && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().equalsIgnoreCase(name)){
            return true;
        }
        return false;
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e){
        if(e.getInventory().getRecipe().getResult().getType() == Material.GOLD_SWORD && e.getInventory().getRecipe().getResult().getEnchantments().size() == 0){
            e.getInventory().setResult(new ItemStack(Material.AIR));
            for(HumanEntity he:e.getViewers()) {
                if(he instanceof Player) {
                    ((Player)he).sendMessage("§cDésolé mais ce craft est déactivé :(");
                }
            }
        }
        if(e.getInventory().getRecipe().getResult().getType() == Material.GOLD_SWORD && e.getInventory().getRecipe().getResult().getEnchantments().size() > 0){
            if(main.getRoles().get(e.getViewers().get(0)) != GRoles.Sisi){
                e.getInventory().setResult(new ItemStack(Material.AIR));
                for(HumanEntity he:e.getViewers()) {
                    if(he instanceof Player) {
                        ((Player)he).sendMessage("§cDésolé mais vous ne pouvez pas crafter cet item :(");
                    }
                }
            }
        }
        if(e.getInventory().getRecipe().getResult().getType() == Material.GOLD_HELMET && e.getInventory().getRecipe().getResult().getEnchantments().size() > 0){
            if(main.getRoles().get(e.getViewers().get(0)) != GRoles.YumiIshiyama){
                e.getInventory().setResult(new ItemStack(Material.AIR));
                for(HumanEntity he:e.getViewers()) {
                    if(he instanceof Player) {
                        ((Player)he).sendMessage("§cDésolé mais vous ne pouvez pas crafter cet item :(");
                    }
                }
            }
        }
        if(e.getInventory().getRecipe().getResult().getType() == Material.GOLD_CHESTPLATE && e.getInventory().getRecipe().getResult().getEnchantments().size() > 0){
            if(main.getRoles().get(e.getViewers().get(0)) != GRoles.YumiIshiyama){
                e.getInventory().setResult(new ItemStack(Material.AIR));
                for(HumanEntity he:e.getViewers()) {
                    if(he instanceof Player) {
                        ((Player)he).sendMessage("§cDésolé mais vous ne pouvez pas crafter cet item :(");
                    }
                }
            }
        }
        if(e.getInventory().getRecipe().getResult().getType() == Material.GOLD_LEGGINGS && e.getInventory().getRecipe().getResult().getEnchantments().size() > 0){
            if(main.getRoles().get(e.getViewers().get(0)) != GRoles.YumiIshiyama){
                e.getInventory().setResult(new ItemStack(Material.AIR));
                for(HumanEntity he:e.getViewers()) {
                    if(he instanceof Player) {
                        ((Player)he).sendMessage("§cDésolé mais vous ne pouvez pas crafter cet item :(");
                    }
                }
            }
        }
        if(e.getInventory().getRecipe().getResult().getType() == Material.GOLD_BOOTS && e.getInventory().getRecipe().getResult().getEnchantments().size() > 0){
            if(main.getRoles().get(e.getViewers().get(0)) != GRoles.YumiIshiyama){
                e.getInventory().setResult(new ItemStack(Material.AIR));
                for(HumanEntity he:e.getViewers()) {
                    if(he instanceof Player) {
                        ((Player)he).sendMessage("§cDésolé mais vous ne pouvez pas crafter cet item :(");
                    }
                }
            }
        }
        if(e.getInventory().getRecipe().getResult().getType() == Material.GOLD_CHESTPLATE && e.getInventory().getRecipe().getResult().getEnchantments().size() == 0){
            e.getInventory().setResult(new ItemStack(Material.AIR));
            for(HumanEntity he:e.getViewers()) {
                if(he instanceof Player) {
                    ((Player)he).sendMessage("§cDésolé mais ce craft est déactivé :(");
                }
            }
        }
        if(e.getInventory().getRecipe().getResult().getType() == Material.GOLD_HELMET && e.getInventory().getRecipe().getResult().getEnchantments().size() == 0){
            e.getInventory().setResult(new ItemStack(Material.AIR));
            for(HumanEntity he:e.getViewers()) {
                if(he instanceof Player) {
                    ((Player)he).sendMessage("§cDésolé mais ce craft est déactivé :(");
                }
            }
        }
        if(e.getInventory().getRecipe().getResult().getType() == Material.GOLD_BOOTS && e.getInventory().getRecipe().getResult().getEnchantments().size() == 0){
            e.getInventory().setResult(new ItemStack(Material.AIR));
            for(HumanEntity he:e.getViewers()) {
                if(he instanceof Player) {
                    ((Player)he).sendMessage("§cDésolé mais ce craft est déactivé :(");
                }
            }
        }
        if(e.getInventory().getRecipe().getResult().getType() == Material.GOLD_LEGGINGS && e.getInventory().getRecipe().getResult().getEnchantments().size() == 0){
            e.getInventory().setResult(new ItemStack(Material.AIR));
            for(HumanEntity he:e.getViewers()) {
                if(he instanceof Player) {
                    ((Player)he).sendMessage("§cDésolé mais ce craft est déactivé :(");
                }
            }
        }
    }

    public void OpenHalfInventory(String playerName, Player p){
        Player player = main.getServer().getPlayer(playerName);

        Inventory inv = Bukkit.createInventory(null, 18, "Inventaire de " + playerName);

        for (int i = 0; i < 18; i++){
            if(player.getInventory().getItem(i) != null){
                inv.setItem(i, player.getInventory().getItem(i));
            }else{
                inv.setItem(i, new ItemStack(Material.AIR));
            }
        }

        p.openInventory(inv);
    }

    ItemStack getPlayerHead(Player player){
        ItemStack skull = new ItemStack(397, 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(player.getName());
        meta.setDisplayName("§aInventaire de " + player.getDisplayName());
        skull.setItemMeta(meta);
        return skull;
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
