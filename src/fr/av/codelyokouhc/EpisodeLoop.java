package fr.av.codelyokouhc;

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
        Bukkit.broadcastMessage("§b==========[Episode " + main.getEpisode() + "]==========");
        for (Player player : main.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        }
    }
}
