package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.GState;
import fr.av.codelyokouhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            Start();
        }else{
            commandSender.sendMessage("Le est deja en cours !");
        }
        return true;
    }

    void Start(){
        if(main.getGameSpawn() == null){
            Bukkit.broadcastMessage("§4Impossible de lancer le jeu: Le point de spawn du jeu n'est pas définis (/setGameSpawn)");
            main.setState(GState.WAITINGPLAYERS);
            return;
        }
        for (Player player:Bukkit.getWorld("world").getPlayers()) {
            player.teleport(main.getGameSpawn());
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}
