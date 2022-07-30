package fr.av.codelyokouhc.loops;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GRoles;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class RolesLoop extends BukkitRunnable {
    Main main;
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
    }
}
