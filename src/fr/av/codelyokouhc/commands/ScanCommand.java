package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GRoles;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScanCommand implements CommandExecutor {
    Main main;

    public ScanCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if(main.getRoles().get(player) == GRoles.ChefDuXana && player.getGameMode() == GameMode.SURVIVAL){
            if(main.scanCount >= 3){
                player.sendMessage("§cVous avez déjà utilisé ce pouvoir 3 fois !");
                return false;
            }
            int count = 0;
            for (Player ps : main.getServer().getOnlinePlayers()) {
                if (ps.getGameMode() == GameMode.SURVIVAL && main.playerIsAt(player, ps, 20)) {
                    if(main.getRoles().get(ps) == GRoles.Agent){
                        count ++;
                    }
                }
            }
            main.scanCount ++;
            player.sendMessage("§aIl y a §b" + count + " §aagent(s) autour de vous !");
        }else{
            player.sendMessage("§cVous n'avez pas ce pouvoir !");
        }
        return false;
    }
}
