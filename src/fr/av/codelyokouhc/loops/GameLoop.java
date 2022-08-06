package fr.av.codelyokouhc.loops;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.ScoreboardManagerUtils;
import fr.av.codelyokouhc.enums.GRoles;
import fr.av.codelyokouhc.enums.GState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameLoop extends BukkitRunnable {
    int index = 1;
    Main main;
    private int role = 0;
    private int roleSec = 10;
    private int pvp = 1;
    private int pvpsSec = 59;
    private Boolean endRole = false;
    private Boolean endPvp = false;
    private int timer = 0;
    private int timerSec = 0;
    public GameLoop(Main main){
        this.main = main;
    }
    @Override
    public void run() {
        UpdateScoreBoard();
        if(role == 0 && roleSec == 0 && !endRole){
            main.setState(GState.Role);
            SelectRole();
            endRole = true;
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
    }

    private void SelectRole(){
        EpisodeLoop start = new EpisodeLoop(main);
        start.runTaskTimer(main, 0, 1);
        RolesLoop rolesLoop = new RolesLoop(main);
        rolesLoop.runTaskTimer(main, 0,1);
        Boolean is = true;
        for (Player player : main.getServer().getOnlinePlayers()) {
            if(player.getGameMode() == GameMode.SURVIVAL){
                Random rnd = new Random();
                //int index = rnd.nextInt(main.getNonAttribuateRoles().size() + 4);
                if(index < main.getNonAttribuateRoles().size()){
                    main.getRoles().put(player, main.getNonAttribuateRoles().get(index));
                    main.getNonAttribuateRoles().remove(index);
                }else{
                    main.getRoles().put(player, GRoles.Agent);
                }
                ConfigPlayer(player);
                player.sendMessage("§a====================");
                player.sendMessage("Vous etes : §e" + main.getRoles().get(player).toString());
                player.sendMessage("§a====================");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                index = 13;
            }
        }
    }

    void ConfigPlayer(Player player){
        GRoles role = main.getRoles().get(player);
        switch (role){
            case AelitaSchaeffer:
                ItemStack bow = new ItemStack(Material.BOW, 1);
                bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
                bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
                bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
                player.setMaxHealth(14);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 99999999, 1, false, false));
                player.getInventory().addItem(bow);
                break;
            case FranzHopper:
                break;
            case JeanPierreDelmas:
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
            case Agent:
                player.sendMessage("§cEn tant qu'agent vous devez trouver Franz Hopper pour qu'il vous accorde un pouvoir !");
                break;
        }
    }

    private void UpdateScoreBoard(){
        for (Player player:main.getServer().getOnlinePlayers()) {
            if(player.getGameMode() != GameMode.SPECTATOR){
                ScoreboardManagerUtils smu = new ScoreboardManagerUtils();
                Map<String,Integer> values = new HashMap<>();
                if(main.getEpisode() != 0){
                    values.put("   ", 9);
                    values.put("§aEpisode : " + main.getEpisode(), 8);
                }
                values.put(" ", 7);
                if(endRole){
                    values.put("§6Role : §e" + main.getRoles().get(player).toString(), 6);
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
                    values.put("§6Roles : §e" + text, 6);
                }
                if(endPvp){
                    values.put("§bPvP : §aActivé", 5);
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
                    values.put("§bPvP : §3" + text, 5);
                }
                values.put("", 4);
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
                values.put("§eTimer : " + timerText, 3);
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
                values.put(" ", 7);
                if(endRole){
                    values.put("§eRoles : §aRévélés", 6);
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
                    values.put("§eRoles : §a" + text, 6);
                }
                if(endPvp){
                    values.put("§bPvP : §3Activé", 5);
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
                    values.put("§bPvP : §3" + text, 5);
                }
                values.put("", 4);
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
                values.put("§eTimer : " + timerText, 3);
                values.put("     ",2);
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
