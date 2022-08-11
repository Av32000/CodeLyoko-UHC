package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CheckWinCommand implements CommandExecutor {
    Main main;
    public CheckWinCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("Fait !");
        main.CheckWin();
        return true;
    }
}
