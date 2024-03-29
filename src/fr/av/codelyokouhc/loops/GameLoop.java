package fr.av.codelyokouhc.loops;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.ScoreboardManagerUtils;
import fr.av.codelyokouhc.enums.GRoles;
import fr.av.codelyokouhc.enums.GState;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameLoop extends BukkitRunnable {
    Main main;
    private int role = 0;
    private int roleSec = 0;
    private int pvp = 1;
    private int pvpsSec = 0;
    private Boolean endRole = false;
    private Boolean endPvp = false;
    private int timer = 0;
    private int timerSec = 0;
    int lyokoTimer = 60;
    public GameLoop(Main main){
        this.main = main;
        this.role = main.timeBeforeRoles;
        this.pvp = main.timeBeforePVP;
    }
    @Override
    public void run() {
        UpdateScoreBoard();
        if(role == 0 && roleSec == 0 && !endRole){
            main.setState(GState.Role);
            endRole = true;
            SelectRole();
        }
        if(pvp == 0 && pvpsSec == 0 && !endPvp){
            main.setState(GState.PVP);
            endPvp = true;
            for (Player player : main.getServer().getOnlinePlayers()) {
                if(player.getGameMode() == GameMode.SURVIVAL){
                    player.sendMessage("§4====================");
                    player.sendMessage("/!\\ Le PVP est actif ! /!\\");
                    player.sendMessage("§4====================");
                    player.playSound(player.getLocation(), Sound.BLAZE_BREATH, 1.0f, 1.0f);
                }
            }
        }
        if(timerSec == 59){
            timer ++;
            timerSec = 0;
        }else{
            timerSec++;
        }

        if(roleSec == 0 && !endRole){
            role--;
            roleSec = 59;
        }
        if(pvpsSec == 0 && !endPvp){
            pvp--;
            pvpsSec = 59;
        }
        if(!endPvp){
            pvpsSec--;
        }
        if(!endRole){
            roleSec--;
        }

        if(timer == 60){
            main.computerWork = true;
        }

        //Lyoko Damage
        if(lyokoTimer <= 0){
            lyokoTimer = 60;
            for (Player p :
                    main.getServer().getOnlinePlayers()) {
                if(main.isInLyoko(p)){
                    if (main.getRoles().get(p) != GRoles.YumiIshiyama && main.getRoles().get(p) != GRoles.MèreDeYumi && main.getRoles().get(p) != GRoles.PèreDeYumi && main.getRoles().get(p) != GRoles.Odd && main.getRoles().get(p) != GRoles.Kiwi) {
                        p.damage(2);
                    }else{
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,999999999,0,false,false));
                    }
                }
            }
        }
        lyokoTimer --;
    }

    private void SelectRole(){
        EpisodeLoop start = new EpisodeLoop(main);
        start.runTaskTimer(main, 0, 1);
        RolesLoop rolesLoop = new RolesLoop(main);
        rolesLoop.runTaskTimer(main, 0,1);
        main.rolesLoop = rolesLoop;

        for (GRoles role : main.disabledRoles) {
            main.getNonAttribuateRoles().remove(role);
        }

        int nbAgents = main.getServer().getOnlinePlayers().size() - main.getNonAttribuateRoles().size();
        List<Player> agents = new ArrayList<>();
        if(nbAgents > 0){
            for (int i = nbAgents; i > 0; i--){
                Random rnd = new Random();
                int index = rnd.nextInt(main.getServer().getOnlinePlayers().size());
                agents.add((Player) main.getServer().getOnlinePlayers().toArray()[index]);
            }
        }

        for (Player player : main.getServer().getOnlinePlayers()) {
            if(player.getGameMode() == GameMode.SURVIVAL){
                if(main.getNonAttribuateRoles().size() == 0){
                    main.getRoles().put(player, GRoles.Agent);
                }else{
                    Random rnd = new Random();
                    int index = rnd.nextInt(main.getNonAttribuateRoles().size());
                    if(!agents.contains(player)){
                        main.getRoles().put(player, main.getNonAttribuateRoles().get(index));
                        main.getNonAttribuateRoles().remove(index);
                    }else{
                        main.getRoles().put(player, GRoles.Agent);
                    }
                }
                player.sendMessage("§a====================");
                player.sendMessage("Vous etes : §e" + main.getRoles().get(player).toString());
                player.sendMessage("§a====================");
                ConfigPlayer(player);
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
        }
    }

    void ConfigPlayer(Player player){
        GRoles role = main.getRoles().get(player);
        main.sendMessageInfo(player, role);
        switch (role){
            case AelitaSchaeffer:
                ItemStack bow = new ItemStack(Material.BOW, 1);
                bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
                bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
                bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                bow.addEnchantment(Enchantment.ARROW_FIRE, 1);

                ItemStack feather = new ItemStack(Material.FEATHER);
                ItemMeta meta = feather.getItemMeta();
                List<String> lore = new ArrayList<>();
                lore.add("§bCooldown => 30s");
                meta.setLore(lore);
                meta.setDisplayName("§aDoubleJump");
                feather.setItemMeta(meta);

                ItemStack arrow = new ItemStack(Material.ARROW);

                player.setMaxHealth(14);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 99999999, 1, false, false));
                player.getInventory().addItem(bow);
                player.getInventory().addItem(feather);
                player.getInventory().addItem(arrow);
                break;
            case ChefDuXana:
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999999, 0, false, false));
                break;
            case JeanPierreDelmas:
                ItemStack computer = main.getSkullWithUrl("http://textures.minecraft.net/texture/8d19c68461666aacd7628e34a1e2ad39fe4f2bde32e231963ef3b35533");
                ItemMeta computerMeta = computer.getItemMeta();
                computerMeta.setDisplayName("§dS§au§bp§fe§cr§ac§4a§fl§bc§du§al§ca§bt§de§fu§ar");
                List<String> computerLore = new ArrayList<>();
                computerLore.add("§bAccès au lyoko après 60min de jeu !");
                computerLore.add("§cNE PAS POSER !");
                computerMeta.setLore(computerLore);
                computer.setItemMeta(computerMeta);
                player.getInventory().addItem(computer);
                break;
            case JeremyBelpois:
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 99999999, 0, false, false));
                break;
            case JimMoralés:
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999999, 0, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999999, 1,false, false));
                player.setStatistic(Statistic.WALK_ONE_CM, 0);
                player.setStatistic(Statistic.SPRINT_ONE_CM, 0);
                break;
            case MillySolovieff:
                ItemStack echo = main.getSkullWithUrl("http://textures.minecraft.net/texture/b462ddfa553ce78683be477b8d8654f3dfc3aa2969808478c987ab88c376a0");
                ItemMeta echoMeta = echo.getItemMeta();
                echoMeta.setDisplayName("§dEchos de Kadic");
                List<String> echoLore = new ArrayList<>();
                echoLore.add("§bVoir la moitié de l'inventaire d'un joueur.");
                echoLore.add("§cNE PAS POSER !");
                echoMeta.setLore(echoLore);
                echo.setItemMeta(echoMeta);
                player.getInventory().addItem(echo);
                break;
            case TamiyaDiop:
                break;
            case Odd:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 1, false, false));
                break;
            case Kiwi:
                ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
                pickaxe.addEnchantment(Enchantment.DIG_SPEED, 3);
                player.getInventory().addItem(new ItemStack(Material.COMPASS));
                player.getInventory().addItem(pickaxe);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999999, 1, false, false));
                break;
            case Nicolas:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999999, 0,false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999999, 0,false, false));
                break;
            case UlrichStern:
                break;
            case WilliamDunba:
                break;
            case YumiIshiyama:
                player.setMaxHealth(28);
                player.setHealth(28);
                break;
            case MèreDeYumi:
            case PèreDeYumi:
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999999, 0,false, false));
                ItemStack gaps = new ItemStack(Material.GOLDEN_APPLE, 5);
                player.getInventory().addItem(gaps);
                player.getInventory().addItem(new ItemStack(Material.COMPASS));
                break;
            case ChefMegaTank:
                if(main.getPlayerByRole(GRoles.ChefDuXana) != null){
                    main.getPlayerByRole(GRoles.ChefDuXana).sendMessage("§a" + player.getDisplayName() + " est le Chef MegaTank !");
                }
                break;
            case Kalamar:
                if(main.getPlayerByRole(GRoles.ChefDuXana) != null){
                    main.getPlayerByRole(GRoles.ChefDuXana).sendMessage("§a" + player.getDisplayName() + " est Kalamar !");
                }
                break;
        }
    }

    public void UpdateScoreBoard(){
        for (Player player:main.getServer().getOnlinePlayers()) {
            if(player.getGameMode() != GameMode.SPECTATOR){
                ScoreboardManagerUtils smu = new ScoreboardManagerUtils();
                Map<String,Integer> values = new HashMap<>();
                if(main.getEpisode() != 0){
                    values.put("   ", 11);
                    values.put("§aEpisode : " + main.getEpisode(), 10);
                }
                values.put(" ", 9);
                if(endRole){
                    values.put("§6Role : §e" + main.getRoles().get(player).toString(), 8);
                }else{
                    String text = "";
                    if(role < 10){
                        text = text + "0" + role;
                    }else{
                        text = text + role;
                    }
                    text = text + ":";
                    if(roleSec < 10){
                        text = text + "0" + roleSec;
                    }else{
                        text = text + roleSec;
                    }
                    values.put("§6Roles : §e" + text, 8);
                }
                if(endPvp){
                    values.put("§bPvP : §aActivé", 7);
                }else{
                    String text = "";
                    if(pvp < 10){
                        text = text + "0" + pvp;
                    }else{
                        text = text + pvp;
                    }
                    text = text + ":";
                    if(pvpsSec < 10){
                        text = text + "0" + pvpsSec;
                    }else{
                        text = text + pvpsSec;
                    }
                    values.put("§bPvP : §3" + text, 7);
                }
                values.put("", 6);
                String timerText = "";
                if(timer < 10){
                    timerText = timerText + "0" + timer;
                }else{
                    timerText = timerText + timer;
                }
                timerText = timerText + ":";
                if(timerSec < 10){
                    timerText = timerText + "0" + timerSec;
                }else{
                    timerText = timerText + timerSec;
                }
                values.put("§eTimer : " + timerText, 5);
                values.put("       ",  4);
                values.put("§dBorder : " + main.worldBorder, 3);
                values.put("     ",  2);
                if(Integer.parseInt(main.getPlayerCount()) > 1){
                    values.put("§c" + main.getPlayerCount() + " joueurs", 1);
                }else{
                    values.put("§c" + main.getPlayerCount() + " joueur", 1);
                }
                smu.AddScoreBordToPlayer(player, values);
            }
            else{
                ScoreboardManagerUtils smu = new ScoreboardManagerUtils();
                Map<String,Integer> values = new HashMap<>();
                if(main.getEpisode() != 0){
                    values.put("   ", 11);
                    values.put("§aEpisode : " + main.getEpisode(), 10);
                }
                values.put(" ", 9);
                if(endRole){
                    values.put("§eRoles : §aRévélés", 8);
                }else{
                    String text = "";
                    if(role < 10){
                        text = text + "0" + role;
                    }else{
                        text = text + role;
                    }
                    text = text + ":";
                    if(roleSec < 10){
                        text = text + "0" + roleSec;
                    }else{
                        text = text + roleSec;
                    }
                    values.put("§eRoles : §a" + text, 8);
                }
                if(endPvp){
                    values.put("§bPvP : §3Activé", 7);
                }else{
                    String text = "";
                    if(pvp < 10){
                        text = text + "0" + pvp;
                    }else{
                        text = text + pvp;
                    }
                    text = text + ":";
                    if(pvpsSec < 10){
                        text = text + "0" + pvpsSec;
                    }else{
                        text = text + pvpsSec;
                    }
                    values.put("§bPvP : §3" + text, 7);
                }
                values.put("", 6);
                String timerText = "";
                if(timer < 10){
                    timerText = timerText + "0" + timer;
                }else{
                    timerText = timerText + timer;
                }
                timerText = timerText + ":";
                if(timerSec < 10){
                    timerText = timerText + "0" + timerSec;
                }else{
                    timerText = timerText + timerSec;
                }
                values.put("§eTimer : " + timerText, 5);
                values.put("          ", 4);
                values.put("§dBorder : " + main.worldBorder, 3);
                values.put("     ",  2);
                if(Integer.parseInt(main.getPlayerCount()) > 1){
                    values.put("§c" + main.getPlayerCount() + " joueurs", 1);
                }else{
                    values.put("§c" + main.getPlayerCount() + " joueur", 1);
                }
                smu.AddScoreBordToPlayer(player, values);
            }
        }
    }
}
