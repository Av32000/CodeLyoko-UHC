package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.enums.GState;
import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.loops.GameLoop;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class StartGameCommand implements CommandExecutor {
    Main main;
    public StartGameCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(((Player) commandSender).getWorld() != main.getServer().getWorld("world")){
            commandSender.sendMessage("§cVous devez être dans l'overworld pour lancer le jeu !");
            return false;
        }
        if(main.isState(GState.WAITINGPLAYERS)){
            main.setState(GState.STARTING);
            Bukkit.broadcastMessage("§aLancement du jeu...");
            Start();
        }else{
            commandSender.sendMessage("Le jeu est deja en cours !");
        }
        return true;
    }

    void Start(){
        Bukkit.broadcastMessage("Vérification des variables...");
        if(main.getLyokoSpawn() == null){
            Bukkit.broadcastMessage("§4Impossible de lancer le jeu: Le point de spawn du lyoko n'est pas définis (/setLyokoSpawn)");
            main.setState(GState.WAITINGPLAYERS);
            return;
        }
        if(main.lyokoWorld == null){
            Bukkit.broadcastMessage("§4Impossible de lancer le jeu: Le monde du lyoko n'est pas définis (/setLyokoWorld)");
            main.setState(GState.WAITINGPLAYERS);
            return;
        }
        Bukkit.broadcastMessage("Variables valides !");
        Bukkit.broadcastMessage("Lancement des taches...");
        GameLoop start = new GameLoop(main);
        start.runTaskTimer(main, 0, 20);
        main.gameLoop = start;
        Bukkit.broadcastMessage("Taches lancées !");
        Bukkit.broadcastMessage("Préparation des joueurs...");
        for (Player player:Bukkit.getWorld("world").getPlayers()) {
            if(player.getGameMode() != GameMode.SPECTATOR){
                main.ClearPlayer(player);

                player.setGameMode(GameMode.SURVIVAL);
                player.getWorld().setTime(0);

                Bukkit.broadcastMessage("§eTéléportation de " + player.getDisplayName());
                Location playerSpawn = new Location(main.getServer().getWorld("world"), new Random().nextInt(main.worldBorder - (-main.worldBorder)) + (-main.worldBorder), 0, new Random().nextInt(main.worldBorder - (-main.worldBorder)) + (-main.worldBorder));
                int y = playerSpawn.getWorld().getHighestBlockYAt(playerSpawn);
                playerSpawn = new Location(playerSpawn.getWorld(), playerSpawn.getX(), y, playerSpawn.getZ());

                player.teleport(playerSpawn);
            }
        }
        Bukkit.broadcastMessage("Joueurs prêts !");
        Bukkit.broadcastMessage("§aC'est parti !");
        main.setState(GState.MINING);
    }
}
