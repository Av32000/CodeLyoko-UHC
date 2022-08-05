package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GRoles;
import fr.av.codelyokouhc.loops.AnswerLoop;
import fr.av.codelyokouhc.loops.AskCooldownLoop;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class AskCommand implements CommandExecutor {
    Main main;
    public AskCommand(Main main){
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if(main.getRoles().get(player) == GRoles.TamiyaDiop){
            if(main.tamiyaAsk){
                if(strings.length < 3 || main.getServer().getPlayer(strings[1]) == null){
                    player.sendMessage("§cUtilisation : /cl ask <Player> <Question>");
                }else{
                    Player target = main.getServer().getPlayer(strings[1]);
                    String q = "";
                    List<String> args = Arrays.asList(strings);
                    for (String a :args) {
                        if(args.indexOf(a) >= 2){
                            q += a + " ";
                        }
                    }
                    target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.WITHER_SPAWN, 1.0f,1.0f);
                    target.sendMessage("§bTamiya Diop vous pose une question : " + q + "\nVous avez 1 minute pour répondre par oui ou non avant d'être §ckick §b de la partie ! Vous n'avez pas le droit de mentir ! Merci d'être fairplay");
                    player.sendMessage("§a" + target.getDisplayName() + " a 1 minute pour répondre à votre question : " + q + "\nSinon il sera §ckick §ade la partie !");
                    main.tamiyaTarget = target;
                    AnswerLoop answerLoop = new AnswerLoop(main);
                    answerLoop.runTaskTimer(main, 1, 20);
                    main.tamiyaAnswerLoop = answerLoop;
                    AskCooldownLoop loop = new AskCooldownLoop(main);
                    loop.runTaskTimer(main, 1, 20);
                    main.tamiyaAsk = false;
                }
            }else {
                player.sendMessage("§cVous devez attendre 15 minutes entre chaques questions !");
            }
        }else{
            player.sendMessage("§cVous n'avez pas ce pouvoir !");
        }
        return false;
    }
}
