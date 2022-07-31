package fr.av.codelyokouhc.loops;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GRoles;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class VanishLoop extends BukkitRunnable {
    Main main;
    Player player;
    int timer;

    public VanishLoop(Main main, int timer, Player player){
        this.main = main;
        this.timer = timer;
        this.player = player;
    }

    @Override
    public void run() {
        if(timer <= 0){
            for (Player pl : main.getServer().getOnlinePlayers()) {
                if(pl.getGameMode() == GameMode.SURVIVAL){
                    pl.showPlayer(player);
                }
            }
            if(main.getRoles().get(player) == GRoles.JeremyBelpois) player.removePotionEffect(PotionEffectType.SLOW);
            player.sendMessage("§c Vous êtes de nouveau visible !");
            this.cancel();
            return;
        }
        timer --;
    }
}
