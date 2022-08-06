package fr.av.codelyokouhc.loops;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GRoles;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RolesLoop extends BukkitRunnable {
    Main main;
    ItemStack enchantedWillBoots = null;
    InfectLoop il = null;
    public RolesLoop(Main main){
        this.main = main;
    }
    @Override
    public void run() {
        //Odd & Kiwi
        if(main.getPlayerByRole(GRoles.Odd) != null && main.getPlayerByRole(GRoles.Kiwi) != null){
            Player odd = main.getPlayerByRole(GRoles.Odd);
            Player kiwi = main.getPlayerByRole(GRoles.Kiwi);

            if(odd.getGameMode() == GameMode.SURVIVAL) kiwi.setCompassTarget(odd.getLocation());
            else kiwi.setCompassTarget(new Location(kiwi.getWorld(),0,0,0));

            if(kiwi.getGameMode() != GameMode.SURVIVAL) return;
            if(main.playerIsAt(odd, kiwi, 10)){
                if(main.isDay()) {
                    odd.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999999,0, false, false));
                    odd.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                }
                else {
                    odd.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999999,0, false, false));
                    odd.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                }
            }else {
                odd.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                odd.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            }
        }

        //Mère de Yumi
        if(main.getPlayerByRole(GRoles.MèreDeYumi) != null && main.getPlayerByRole(GRoles.YumiIshiyama) != null){
            Player mère = main.getPlayerByRole(GRoles.MèreDeYumi);
            Player yumi = main.getPlayerByRole(GRoles.YumiIshiyama);

            if(yumi.getGameMode() == GameMode.SURVIVAL) mère.setCompassTarget(yumi.getLocation());
            else mère.setCompassTarget(new Location(mère.getWorld(),0,0,0));
        }

        //Père de Yumi
        if(main.getPlayerByRole(GRoles.PèreDeYumi) != null && main.getPlayerByRole(GRoles.YumiIshiyama) != null){
            Player père = main.getPlayerByRole(GRoles.PèreDeYumi);
            Player yumi = main.getPlayerByRole(GRoles.YumiIshiyama);

            if(yumi.getGameMode() == GameMode.SURVIVAL) père.setCompassTarget(yumi.getLocation());
            else père.setCompassTarget(new Location(père.getWorld(),0,0,0));
        }

        //Jean Pierre Delmas
        if(main.getPlayerByRole(GRoles.JeanPierreDelmas) != null) {
            Player jpd = main.getPlayerByRole(GRoles.JeanPierreDelmas);
            if(GetJPDAliesAround(jpd) > 3){
                jpd.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 0, false,false));
            }else{
                jpd.removePotionEffect(PotionEffectType.SPEED);
            }
        }

        //Jim Moralès
        if(main.getPlayerByRole(GRoles.JimMoralés) != null){
            Player jim = main.getPlayerByRole(GRoles.JimMoralés);
            if(jim.getStatistic(Statistic.WALK_ONE_CM) / 100 + jim.getStatistic(Statistic.SPRINT_ONE_CM) / 100 > 3000){
                jim.setStatistic(Statistic.WALK_ONE_CM, 0);
                jim.setStatistic(Statistic.SPRINT_ONE_CM, 0);
                boolean isFind = false;
                for (PotionEffect pe:jim.getActivePotionEffects()) {
                        if(pe.getType().getName().equalsIgnoreCase(PotionEffectType.SLOW.getName())){
                            if(pe.getAmplifier() > 0){
                                jim.removePotionEffect(PotionEffectType.SLOW);
                                jim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999999, 0,false,false));
                            }else{
                                jim.removePotionEffect(PotionEffectType.SLOW);
                            }
                            isFind = true;
                        }else if(pe.getType().getName().equalsIgnoreCase(PotionEffectType.SPEED.getName())){
                            if (pe.getAmplifier() < 2){
                                PotionEffect npe = new PotionEffect(PotionEffectType.SPEED, 999999999, pe.getAmplifier() + 1, false,false);
                                jim.removePotionEffect(PotionEffectType.SPEED);
                                jim.addPotionEffect(npe);
                            }
                            isFind = true;
                        }
                }
                if(!isFind){
                    jim.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 0, false,false));
                }
            }
        }

        //Ulrich Stern
        if(main.getPlayerByRole(GRoles.UlrichStern) != null && main.ulrichGirl == null){
            List<Player> girls = new ArrayList<>();
            if(main.getPlayerByRole(GRoles.AelitaSchaeffer) != null && main.getPlayerByRole(GRoles.AelitaSchaeffer).getGameMode() == GameMode.SURVIVAL) girls.add(main.getPlayerByRole(GRoles.AelitaSchaeffer));
            if(main.getPlayerByRole(GRoles.MillySolovieff) != null && main.getPlayerByRole(GRoles.MillySolovieff).getGameMode() == GameMode.SURVIVAL) girls.add(main.getPlayerByRole(GRoles.MillySolovieff));
            if(main.getPlayerByRole(GRoles.TamiyaDiop) != null && main.getPlayerByRole(GRoles.TamiyaDiop).getGameMode() == GameMode.SURVIVAL) girls.add(main.getPlayerByRole(GRoles.TamiyaDiop));
            if(main.getPlayerByRole(GRoles.Sisi) != null && main.getPlayerByRole(GRoles.Sisi).getGameMode() == GameMode.SURVIVAL) girls.add(main.getPlayerByRole(GRoles.Sisi));
            if(main.getPlayerByRole(GRoles.SuzanneHertz) != null && main.getPlayerByRole(GRoles.SuzanneHertz).getGameMode() == GameMode.SURVIVAL) girls.add(main.getPlayerByRole(GRoles.SuzanneHertz));

            if(girls.size() == 0) return;

            Player us = (main.getPlayerByRole(GRoles.UlrichStern));
            main.ulrichGirl = girls.get(new Random().nextInt(girls.size()));
            us.sendMessage("§d💖Vous êtes en couple avec" + main.ulrichGirl.getDisplayName() +"💖");
            main.ulrichGirl.sendMessage("§d💖Vous êtes en couple avec" + us.getDisplayName() +"💖");
            us.playSound(us.getLocation(), Sound.VILLAGER_YES, 1.0f, 1.0f);
            main.ulrichGirl.playSound(main.ulrichGirl.getLocation(), Sound.VILLAGER_YES, 1.0f, 1.0f);
        }

        //William Dunbar
        if(main.getPlayerByRole(GRoles.WilliamDunba) != null && main.getPlayerByRole(GRoles.FranzHopper) != null){
            Player will = main.getPlayerByRole(GRoles.WilliamDunba);
            Player franz = main.getPlayerByRole(GRoles.FranzHopper);
            if(main.playerIsAt(franz,will,5)){
                if(il == null && main.franzInfected == null){
                    InfectLoop infectLoop = new InfectLoop(main, franz);
                    infectLoop.runTaskTimer(main, 1,20);
                    il = infectLoop;
                }
            }else{
                if(il != null){
                    il.cancel();
                    il = null;
                }
                main.franzCanInfect = false;
            }
        }
    }

    public int GetJPDAliesAround(Player player) {
        int count = 0;
        if(main.getPlayerByRole(GRoles.AelitaSchaeffer) != null && main.getPlayerByRole(GRoles.AelitaSchaeffer).getGameMode() == GameMode.SURVIVAL && main.playerIsAt(player, main.getPlayerByRole(GRoles.AelitaSchaeffer), 20)) count ++;
        if(main.getPlayerByRole(GRoles.JeremyBelpois) != null && main.getPlayerByRole(GRoles.JeremyBelpois).getGameMode() == GameMode.SURVIVAL && main.playerIsAt(player, main.getPlayerByRole(GRoles.JeremyBelpois), 20)) count ++;
        if(main.getPlayerByRole(GRoles.JimMoralés) != null && main.getPlayerByRole(GRoles.JimMoralés).getGameMode() == GameMode.SURVIVAL && main.playerIsAt(player, main.getPlayerByRole(GRoles.JimMoralés), 20)) count ++;
        if(main.getPlayerByRole(GRoles.MillySolovieff) != null && main.getPlayerByRole(GRoles.MillySolovieff).getGameMode() == GameMode.SURVIVAL && main.playerIsAt(player, main.getPlayerByRole(GRoles.MillySolovieff), 20)) count ++;
        if(main.getPlayerByRole(GRoles.TamiyaDiop) != null && main.getPlayerByRole(GRoles.TamiyaDiop).getGameMode() == GameMode.SURVIVAL && main.playerIsAt(player, main.getPlayerByRole(GRoles.TamiyaDiop), 20)) count ++;
        if(main.getPlayerByRole(GRoles.Hervé) != null && main.getPlayerByRole(GRoles.Hervé).getGameMode() == GameMode.SURVIVAL && main.playerIsAt(player, main.getPlayerByRole(GRoles.Hervé), 20)) count ++;
        if(main.getPlayerByRole(GRoles.Nicolas) != null && main.getPlayerByRole(GRoles.Nicolas).getGameMode() == GameMode.SURVIVAL && main.playerIsAt(player, main.getPlayerByRole(GRoles.Nicolas), 20)) count ++;
        if(main.getPlayerByRole(GRoles.Sisi) != null && main.getPlayerByRole(GRoles.Sisi).getGameMode() == GameMode.SURVIVAL && main.playerIsAt(player, main.getPlayerByRole(GRoles.Sisi), 20)) count ++;
        if(main.getPlayerByRole(GRoles.SuzanneHertz) != null && main.getPlayerByRole(GRoles.SuzanneHertz).getGameMode() == GameMode.SURVIVAL && main.playerIsAt(player, main.getPlayerByRole(GRoles.SuzanneHertz), 20)) count ++;
        if(main.getPlayerByRole(GRoles.WilliamDunba) != null && main.getPlayerByRole(GRoles.WilliamDunba).getGameMode() == GameMode.SURVIVAL && main.playerIsAt(player, main.getPlayerByRole(GRoles.WilliamDunba), 20)) count ++;

        return count;
    }
}
