package fr.av.codelyokouhc.loops;

import fr.av.codelyokouhc.Main;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class InfectLoop extends BukkitRunnable {
    Main main;
    Player franz;

    int timer = 240;
    public InfectLoop(Main main, Player franz) {
        this.main = main;
        this.franz = franz;
    }

    @Override
    public void run() {
        if(timer <= 0 && !main.franzCanInfect){
            main.franzCanInfect = true;
            franz.sendMessage("§aCela fait plus de 4 minutes que vous êtes à coté de William Dunbar, vous pouvez l'infecter avec la commande §d/cl infect §a !");
            franz.playSound(franz.getLocation(), Sound.LEVEL_UP,1,1);
            return;
        }
        timer --;
    }
}
