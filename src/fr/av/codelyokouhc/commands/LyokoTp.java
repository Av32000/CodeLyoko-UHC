package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LyokoTp implements CommandExecutor {
    Main main;
    public LyokoTp(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            ((Player) commandSender).teleport(new Location(main.getServer().getWorld("lyoko"),0,main.getServer().getWorld("lyoko").getHighestBlockYAt(0,0),0 ));
        }
        return true;
    }
}
