package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.GState;
import fr.av.codelyokouhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartGameCommand implements CommandExecutor {
    Main main;
    public StartGameCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(main.isState(GState.WAITINGPLAYERS)){
            main.setState(GState.STARTING);
            Bukkit.broadcastMessage("§aLancement du jeu...");
        }else{
            commandSender.sendMessage("Le est deja en cours !");
        }
        return true;
    }
}
