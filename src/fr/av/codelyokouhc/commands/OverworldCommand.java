package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OverworldCommand implements CommandExecutor {
    Main main;
    public OverworldCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player player = ((Player) commandSender).getPlayer();
            if(main.isInLyoko(player)){
                player.teleport(main.getGameSpawn());
                player.sendMessage("§eBienvenue dans l'overworld !");
                main.removePlayerLyoko(player);
                return true;
            }else{
                player.sendMessage("§4&Vous etes déjà dans l'overworld");
            }
        }
        return false;
    }
}
