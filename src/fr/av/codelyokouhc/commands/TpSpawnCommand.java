package fr.av.codelyokouhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String msg, String[] args) {
        Location spawn = Bukkit.getWorld("world").getSpawnLocation();
        for (Player player:Bukkit.getWorld("world").getPlayers()) {
            player.teleport(spawn);
        }
        System.out.println("Tous les joueurs sont au spawn");
        return true;
    }
}
