package fr.av.codelyokouhc.loops;

import fr.av.codelyokouhc.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class AskCooldownLoop extends BukkitRunnable {
    Main main;
    int timer = 900;
    public AskCooldownLoop(Main main){
        this.main = main;
    }

    @Override
    public void run() {
        if(timer <= 0){
            main.tamiyaAsk = true;
            cancel();
            return;
        }

        timer --;
    }
}
