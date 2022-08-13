package fr.av.codelyokouhc.loops;

import fr.av.codelyokouhc.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class ReduceWorldBorderLoop extends BukkitRunnable {
    Main main;
    public ReduceWorldBorderLoop(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        if(main.worldBorder <= 300){
            cancel();
            return;
        }
        main.setWorldBorder(main.worldBorder - 1);
        main.worldBorder --;
    }
}
