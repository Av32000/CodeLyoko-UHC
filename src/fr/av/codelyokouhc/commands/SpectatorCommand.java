package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class SpectatorCommand implements CommandExecutor {
    Main main;

    public SpectatorCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p = (Player) commandSender;
        if(p.getGameMode() != GameMode.SPECTATOR){
            p.sendMessage("§cVous n'avez pas le droit de faire ca !");
            return false;
        }

        List<Player> players = new ArrayList<>();
        int invSize = 0;
        for (Player ps: main.getServer().getOnlinePlayers()) {
            if (ps.getGameMode() == GameMode.SURVIVAL) players.add(ps);
        }

        if(players.size() <= 9){
            invSize = 9;
        }else if(players.size() <= 18){
            invSize = 18;
        }else if(players.size() <= 27){
            invSize = 27;
        }else if(players.size() <= 36){
            invSize = 36;
        }else if(players.size() <= 45){
            invSize = 45;
        }else if(players.size() <= 54){
            invSize = 54;
        }else{
            invSize = 90;
        }

        //Create Inventory
        Inventory playerListInventory = Bukkit.createInventory(null, invSize, "Menu Spectateur");
        for (Player ps : players) {
            playerListInventory.addItem(getPlayerHead(ps));
        }

        p.openInventory(playerListInventory);
        return false;
    }

    ItemStack getPlayerHead(Player player){
        ItemStack skull = new ItemStack(397, 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(player.getName());
        meta.setDisplayName("§a" + player.getDisplayName());
        List<String> lore = new ArrayList<>();
        if(main.getRoles().size() == 0) lore.add("§bAttente du role...");
        else lore.add("§b" + main.getRoles().get(player));
        lore.add("§dCliquez pour vous téléporter");
        meta.setLore(lore);
        skull.setItemMeta(meta);
        return skull;
    }
}
