package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GRoles;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KankrelatsCommand implements CommandExecutor {
    Main main;
    public KankrelatsCommand(Main main){
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if(main.getRoles().get(player) == GRoles.FranzHopper && player.getGameMode() == GameMode.SURVIVAL){
            if(strings.length != 2 || main.getServer().getPlayer(strings[1]) == null){
                player.sendMessage("§cUtilisation : /cl kankrelats <Player>");
            }else{
                Player target = main.getServer().getPlayer(strings[1]);
                if(main.getRoles().get(target) == GRoles.Agent){
                    for (PotionEffect e : target.getActivePotionEffects()) {
                        target.removePotionEffect(e.getType());
                    }
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999999,0,false,false));

                    if(!main.kankrelats.contains(target)) main.kankrelats.add(target);

                    target.sendMessage("§aFranz Hopper vous a transformé en Kankrelats");
                    target.playSound(target.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                }
                player.sendMessage("§aSi il s'agit d'un agent, il a été transformé en Kankrelats !");
            }
        }else{
            player.sendMessage("§cVous n'avez pas ce pouvoir !");
        }
        return false;
    }
}
