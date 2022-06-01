package fr.av.codelyokouhc;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class WaitingPvp extends BukkitRunnable {
    Main main;
    private int timer = 30;
    public WaitingPvp(Main main){
        this.main = main;
    }
    @Override
    public void run() {
        if(timer == 0){
            main.setState(GState.PVP);
            SelectRole();
            cancel();
        }
        for (Player player : main.getServer().getOnlinePlayers()) {
            player.setLevel(timer);
        }
        timer--;
    }

    private void SelectRole(){
        for (Player player : main.getServer().getOnlinePlayers()) {
            if(player.getGameMode() == GameMode.SURVIVAL){
                Random rnd = new Random();
                int index = rnd.nextInt(main.getNonAttribuateRoles().size() - 0 + 1);
                main.getRoles().put(player, main.getNonAttribuateRoles().get(index));
                main.getNonAttribuateRoles().remove(index);
                player.sendMessage("Vous etes : " + main.getRoles().get(player).toString());
            }
        }
    }
}
