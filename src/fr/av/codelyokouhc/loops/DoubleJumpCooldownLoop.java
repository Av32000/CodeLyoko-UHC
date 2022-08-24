package fr.av.codelyokouhc.loops;

import fr.av.codelyokouhc.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class DoubleJumpCooldownLoop extends BukkitRunnable {
    Main main;
    int timer = 30;
    public DoubleJumpCooldownLoop(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        if(timer <= 0){
            main.aelitaCanJump = true;
            cancel();
            return;
        }

        timer --;
    }
}
