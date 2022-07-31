package fr.av.codelyokouhc.loops;

import fr.av.codelyokouhc.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RemoveKilledPlayerLoop extends BukkitRunnable {
    Main main;
    Player player;

    int timer = 60;
    public RemoveKilledPlayerLoop(Main main, Player player){
        this.main = main;
        this.player = player;
    }
    @Override
    public void run() {
        if(timer <= 0){
            main.killedPlayer.remove(player);
            cancel();
            return;
        }

        timer --;
    }
}
