package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GRoles;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReviveCommand implements CommandExecutor {
    Main main;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if(main.getRoles().get(player) == GRoles.JeremyBelpois){
            int count = 0;
            if(main.jeremyRevive > 0){
                for (Player pl:main.killedPlayer.keySet()) {
                    if(pl.isOnline()){
                        pl.teleport(main.killedPlayer.get(pl));
                        pl.setGameMode(GameMode.SURVIVAL);
                        main.killedPlayer.remove(pl);
                        pl.sendMessage("§aVous avez été réanimé par Jeremy Belpois !");
                        count ++;
                    }
                }
                player.sendMessage("§aVous avez réanimé " + count + " joueurs !");
                main.jeremyRevive --;
            }else{
                player.sendMessage("§cVous avez déjà utilisé votre pouvoir 2 fois !");
            }
        }else{
            player.sendMessage("$cVous n'avez pas ce pouvoir !");
        }
        return true;
    }

    public ReviveCommand(Main main) {
        this.main = main;
    }
}
