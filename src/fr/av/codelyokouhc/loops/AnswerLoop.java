package fr.av.codelyokouhc.loops;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GRoles;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AnswerLoop extends BukkitRunnable {
    Main main;

    int timer = 60;
    public AnswerLoop(Main main){
        this.main = main;
    }
    @Override
    public void run() {
        if(timer == 30) {
            main.tamiyaTarget.getPlayer().playSound(main.tamiyaTarget.getPlayer().getLocation(), Sound.WITHER_SPAWN, 1.0f,1.0f);
            main.tamiyaTarget.sendMessage("§cIl vous reste 30 secondes pour répondre à la question de Tamiya Diop");
        }
        if(timer <= 10 && timer > 0){
            main.tamiyaTarget.getPlayer().playSound(main.tamiyaTarget.getPlayer().getLocation(), Sound.WITHER_SPAWN, 1.0f,1.0f);
            main.tamiyaTarget.sendMessage("§cIl vous reste " + timer + " secondes §cpour répondre à la question de Tamiya Diop");
        }
        if(timer <= 0){
            main.tamiyaTarget.kickPlayer("Triche => N'a pas répondu à la question de Tamiya Diop");
            for (Player p : main.getServer().getOnlinePlayers()) {
                p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.0f);
            }
            Bukkit.broadcastMessage("§c====================");
            Bukkit.broadcastMessage("§e" + main.tamiyaTarget.getDisplayName() + " est un tricheur : Il n'a pas répondu à la question de Tamiya Diop et est donc éliminé ! Il était " + main.getRoles().get(main.tamiyaTarget));
            Bukkit.broadcastMessage("§c====================");
            main.getPlayerByRole(GRoles.TamiyaDiop).sendMessage("§a" + main.tamiyaTarget.getDisplayName() + " n'a pas répondu à votre question :( Il est §céliminé §ade la partie !");
            main.tamiyaTarget = null;
            cancel();
            return;
        }

        timer --;
    }
}
