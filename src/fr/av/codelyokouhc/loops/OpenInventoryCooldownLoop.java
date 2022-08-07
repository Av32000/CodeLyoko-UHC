package fr.av.codelyokouhc.loops;

import fr.av.codelyokouhc.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class OpenInventoryCooldownLoop extends BukkitRunnable {
    Main main;
    int timer = 780;
    public OpenInventoryCooldownLoop(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        if(timer <= 0){
            main.millyCanShow = true;
            cancel();
            return;
        }

        timer --;
    }
}
