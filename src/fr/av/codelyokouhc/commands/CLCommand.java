package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class CLCommand implements CommandExecutor {
    Main main;
    public CLCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 0 || strings[0].equalsIgnoreCase("help")){
            sendHelpMsg(commandSender);
        }
        else if(strings[0].equalsIgnoreCase("hide")){
            HideCommand hideCommand = new HideCommand(main);
            hideCommand.onCommand(commandSender, command, s,strings);
        }
        else if(strings[0].equalsIgnoreCase("overworld")){
            OverworldCommand overworldCommand = new OverworldCommand(main);
            overworldCommand.onCommand(commandSender, command, s,strings);
        } else if (strings[0].equalsIgnoreCase("revive")) {
            ReviveCommand reviveCommand = new ReviveCommand(main);
            reviveCommand.onCommand(commandSender, command, s,strings);
        } else if (strings[0].equalsIgnoreCase("ask")) {
            AskCommand askCommand = new AskCommand(main);
            askCommand.onCommand(commandSender, command, s,strings);
        } else{
            commandSender.sendMessage("§cCet argument n'existe pas (Voir /cl help)");
        }
        return true;
    }

    void sendHelpMsg(CommandSender commandSender){
        commandSender.sendMessage("§aCodeLyoko-UHC");
        if(main.getRoles().size() != 0) commandSender.sendMessage("§6Vous êtes : " + main.getRoles().get(commandSender).toString());
        commandSender.sendMessage("");
        commandSender.sendMessage("§d/cl help §f=> §bAffiche ce message");
        commandSender.sendMessage("§d/cl hide §f=> §bRends le joueur invisible pendant un certain temps");
        commandSender.sendMessage("§d/cl overworld §f=> §bTéléporte le joueur du lyoko vers l'overworld");
        commandSender.sendMessage("§d/cl revive §f=> §bPermet à Jeremy Belpois de réanimer les joueurs morts dans la dèrnière minute (2 fois)");
        commandSender.sendMessage("§d/cl ask §f=> §bPermet à Tamiya Diop de poser une question à un joueur (15min de cooldown)");
    }
}