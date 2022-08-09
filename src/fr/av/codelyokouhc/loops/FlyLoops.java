package fr.av.codelyokouhc.loops;

import fr.av.codelyokouhc.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FlyLoops extends BukkitRunnable {
    Main main;
    int timer = 5;
    Player player;

    public FlyLoops(Main main, Player player) {
        this.main = main;
        this.player = player;
    }

    @Override
    public void run() {
        if(timer <= 0){
            player.setAllowFlight(false);
            cancel();
            return;
        }

        timer--;
    }
}
