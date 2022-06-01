package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGameSpawnCommand implements CommandExecutor {
    Main main;
    public SetGameSpawnCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player sender = (Player) commandSender;
            main.setGameSpawn(sender.getLocation());
            sender.sendMessage("§aGameSpawnPoint definis !");
            return true;
        }
        return false;
    }
}
