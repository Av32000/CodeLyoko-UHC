package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GRoles;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class SpectreCommand implements CommandExecutor {
    Main main;

    public SpectreCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if(main.getRoles().get(player) == GRoles.ChefDuXana && player.getGameMode() == GameMode.SURVIVAL){
            if(strings.length != 2 || main.getServer().getPlayer(strings[1]) == null){
                player.sendMessage("§cUtilisation : /cl spectre <Player>");
            }else{
                Player target = main.getServer().getPlayer(strings[1]);
                if(main.getRoles().get(target) == GRoles.Agent){
                    for (PotionEffect e : target.getActivePotionEffects()) {
                        target.removePotionEffect(e.getType());
                    }

                    ItemStack feather = new ItemStack(Material.FEATHER);
                    ItemMeta meta = feather.getItemMeta();
                    meta.setDisplayName("Fly");
                    List<String> lore = new ArrayList<>();
                    lore.add("§bVous permet de Fly pendant 5s");
                    meta.setLore(lore);
                    feather.setItemMeta(meta);
                    target.getInventory().addItem(feather);

                    if(main.blocks.containsKey(target)) main.blocks.remove(target,1f);
                    if(!main.spectres.contains(target)) main.spectres.add(target);
                    if(main.kankrelats.contains(target)) main.kankrelats.remove(target);

                    target.sendMessage("§aFranz Hopper vous a transformé en Spectre");
                    target.playSound(target.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                }
                player.sendMessage("§aSi il s'agit d'un agent, il a été transformé en Spectre !");
            }
        }else{
            player.sendMessage("§cVous n'avez pas ce pouvoir !");
        }
        return false;
    }
}
