package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GetRoleCommand implements CommandExecutor {
    Main main;
    public GetRoleCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage(strings[0] +  " est " + main.getRoles().get(main.getServer().getPlayer(strings[0])));
        return true;
    }
}
