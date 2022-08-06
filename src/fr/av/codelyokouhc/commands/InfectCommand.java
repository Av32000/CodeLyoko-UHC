package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GRoles;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InfectCommand implements CommandExecutor {
    Main main;

    public InfectCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if(main.getRoles().get(player) == GRoles.FranzHopper && player.getGameMode() == GameMode.SURVIVAL){
            if(main.franzInfected != null){
                player.sendMessage("§cVous avez déja infecté William Dunbar !");
                return true;
            }
            if(!main.franzCanInfect){
                player.sendMessage("§cVous ne pouvez pas infecter William Dunbar pour le moment. Vous devez être à moins de 5 blocs de lui depuis au moins 4min pour cela !");
            }else{
                Player will = main.getPlayerByRole(GRoles.WilliamDunba);
                will.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999999, 0, false, false));
                will.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999999, 0, false, false));
                will.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 0, false, false));

                ItemStack boots = new ItemStack(Material.IRON_BOOTS);
                boots.addEnchantment(Enchantment.DEPTH_STRIDER,1);
                will.getInventory().addItem(boots);

                main.franzInfected = will;

                will.sendMessage("§aVpus venez d'être infecté par Franz Hopper, vous êtes désormais un membre de son équipe !");
                will.playSound(will.getLocation(), Sound.WITHER_SHOOT,1,1);
                player.sendMessage("§aVpus venez d'infecter William Dunbar, il s'agit désormais d'un membre de votre équipe !");
                player.playSound(player.getLocation(), Sound.WITHER_SHOOT,1,1);
            }
        }else{
            player.sendMessage("§cVous n'avez pas ce pouvoir !");
        }
        return false;
    }
}
