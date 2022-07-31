package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GRoles;
import fr.av.codelyokouhc.loops.VanishLoop;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HideCommand implements CommandExecutor {
    Main main;
    public HideCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if(main.getRoles().get(player) == GRoles.JeremyBelpois){
            if(main.jeremyCanVanish()){
                for (Player pl : main.getServer().getOnlinePlayers()) {
                    if(pl.getGameMode() == GameMode.SURVIVAL){
                        pl.hidePlayer(player);
                    }
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999999, 0, false, false));
                VanishLoop v = new VanishLoop(main, 180, player);
                v.runTaskTimer(main, 1,20);
                main.setJeremyCanVanish(false);
                player.sendMessage("§aVous êtes désormais invisible pendant 3 minutes !");
            }else{
                player.sendMessage("§cVous avez déjà utilisé votre pouvoir durant cet épisode !");
            }
        }else if(main.getRoles().get(player) == GRoles.UlrichStern){
            if(main.ulrichHide > 0){
                for (Player pl : main.getServer().getOnlinePlayers()) {
                    if(pl.getGameMode() == GameMode.SURVIVAL){
                        pl.hidePlayer(player);
                    }
                }
                VanishLoop v = new VanishLoop(main, 5, player);
                v.runTaskTimer(main, 1,20);
                main.ulrichHide --;
                player.sendMessage("§aVous êtes désormais invisible pendant 5 secondes !");
            }else{
                player.sendMessage("§cVous avez déjà utilisé votre pouvoir 10 fois !");
            }
        }
        else{
            player.sendMessage("§cVous n'avez pas ce pouvoir !");
        }
        return false;
    }
}
