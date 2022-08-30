package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLyokoWorldCommand implements CommandExecutor {
    Main main;

    public SetLyokoWorldCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            main.lyokoWorld = ((Player) commandSender).getWorld();
            commandSender.sendMessage("Le monde a bien été choisis !");
        }
        return false;
    }
}
