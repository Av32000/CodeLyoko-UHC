package fr.av.codelyokouhc.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String msg, String[] args) {
        if(commandSender instanceof Player){
            Player sender = (Player) commandSender;
            Location spawn = sender.getWorld().getSpawnLocation();
            for (Player player:sender.getWorld().getPlayers()) {
                player.teleport(spawn);
            }
            sender.sendMessage("Tous les joueurs sont au spawn");
            return true;
        }
        return false;
    }
}
