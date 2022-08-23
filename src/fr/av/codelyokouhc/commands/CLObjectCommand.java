package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GRoles;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CLObjectCommand implements CommandExecutor {
    Main main;

    public CLObjectCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if(main.getRoles().size() > 0 && main.getRoles().get(player) == GRoles.AelitaSchaeffer){
            ItemStack feather = new ItemStack(Material.FEATHER);
            ItemMeta meta = feather.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add("§bCooldown => 30s");
            meta.setLore(lore);
            meta.setDisplayName("§aDoubleJump");
            feather.setItemMeta(meta);

            player.getInventory().addItem(feather);

            player.sendMessage("§aVous venez de récupérer votre DoubleJump !");
        } else if (main.getRoles().size() > 0 && main.getRoles().get(player) == GRoles.JeanPierreDelmas) {
            ItemStack computer = main.getSkullWithUrl("http://textures.minecraft.net/texture/8d19c68461666aacd7628e34a1e2ad39fe4f2bde32e231963ef3b35533");
            ItemMeta computerMeta = computer.getItemMeta();
            computerMeta.setDisplayName("§dS§au§bp§fe§cr§ac§4a§fl§bc§du§al§ca§bt§de§fu§ar");
            List<String> computerLore = new ArrayList<>();
            computerLore.add("§bAccès au lyoko après 60min de jeu !");
            computerLore.add("§cNE PAS POSER !");
            computerMeta.setLore(computerLore);
            computer.setItemMeta(computerMeta);
            player.getInventory().addItem(computer);

            player.sendMessage("§aVous venez de récupérer votre §dS§au§bp§fe§cr§ac§4a§fl§bc§du§al§ca§bt§de§fu§ar !");
        } else if (main.getRoles().size() > 0 && main.getRoles().get(player) == GRoles.MillySolovieff) {
            ItemStack echo = main.getSkullWithUrl("http://textures.minecraft.net/texture/b462ddfa553ce78683be477b8d8654f3dfc3aa2969808478c987ab88c376a0");
            ItemMeta echoMeta = echo.getItemMeta();
            echoMeta.setDisplayName("§dEchos de Kadic");
            List<String> echoLore = new ArrayList<>();
            echoLore.add("§bVoir la moitié de l'inventaire d'un joueur.");
            echoLore.add("§cNE PAS POSER !");
            echoMeta.setLore(echoLore);
            echo.setItemMeta(echoMeta);
            player.getInventory().addItem(echo);

            player.sendMessage("§aVous venez de récupérer votre §dEchos de Kadic §a!");
        }else {
            player.sendMessage("§cVotre rôle ne vous accorde aucun objet spécial pouvant être récupéré par cette commande !");
        }
        return true;
    }
}
