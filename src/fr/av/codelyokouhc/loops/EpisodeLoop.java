package fr.av.codelyokouhc.loops;

import fr.av.codelyokouhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EpisodeLoop extends BukkitRunnable {
    Main main;
    public int timer = 0;

    public EpisodeLoop(Main main){
        this.main = main;
    }

    @Override
    public void run() {
        if(timer == 0){
            NewEpisode();
            timer = 24000;
        }
        timer --;
    }

    public void NewEpisode(){
        main.setEpisode(main.getEpisode() + 1);
        main.setJeremyCanVanish(true);
        Bukkit.broadcastMessage("§b==========[Episode " + main.getEpisode() + "]==========");
        for (Player player : main.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        }
        if(main.getEpisode() == 7){
            ReduceWorldBorderLoop reduceWorldBorderLoop = new ReduceWorldBorderLoop(main);
            reduceWorldBorderLoop.runTaskTimer(main,1,60);
        }
    }
}
