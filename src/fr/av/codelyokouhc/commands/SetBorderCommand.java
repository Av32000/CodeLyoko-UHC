package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetBorderCommand implements CommandExecutor {
    Main main;
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length != 1) {
            commandSender.sendMessage("§cUtilisation : /setborder <Dist>");
            return false;
        }
        try {
            int dist = Integer.parseInt(strings[0]);
            main.setWorldBorder(dist);
            commandSender.sendMessage("§cLa bordure de la map est désormais à " + dist + " blocs du centre !");
        }catch (Exception e){
            e.printStackTrace();
            commandSender.sendMessage("§cLa distance doit être un nombre valide !");
        }
        return true;
    }

    public SetBorderCommand(Main main) {
        this.main = main;
    }
}
