package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfigCommand implements CommandExecutor {
    Main main;
    public ConfigCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if(!main.isState(GState.WAITINGPLAYERS)){
            player.sendMessage("§cLa partie à déjà commencé");
            return false;
        }
        Inventory inv = Bukkit.createInventory(null, 27, "Configuration");

        //Add Orange Glass Decoration
        ItemStack border = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 10);
        ItemMeta meta = border.getItemMeta();
        meta.setDisplayName("Config");
        border.setItemMeta(meta);

        for (int i = 0; i <9;i++){
            inv.setItem(i, border);
        }
        for (int i = 18; i <27;i++){
            inv.setItem(i, border);
        }
        inv.setItem(9, border);
        inv.setItem(17, border);

        player.openInventory(inv);
        return false;
    }
}
