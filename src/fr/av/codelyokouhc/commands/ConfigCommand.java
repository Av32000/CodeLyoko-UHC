package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GState;
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
            player.sendMessage("§cLa partie à déjà commencée");
            return false;
        }

        Inventory inv = main.generateConfigInventory("Configuration", 27);

        ItemStack clock = new ItemStack(Material.WATCH);
        ItemMeta clockMeta = clock.getItemMeta();
        clockMeta.setDisplayName("§eGestion des Timers");
        clock.setItemMeta(clockMeta);

        ItemStack book = new ItemStack(Material.BRICK);
        ItemMeta bookMeta = book.getItemMeta();
        bookMeta.setDisplayName("§aGestion de la WorldBorder");
        book.setItemMeta(bookMeta);

        ItemStack bookshelf = new ItemStack(Material.BOOKSHELF);
        ItemMeta bookshelfMeta = bookshelf.getItemMeta();
        bookshelfMeta.setDisplayName("§dGestion des Rôles");
        bookshelf.setItemMeta(bookshelfMeta);

        inv.setItem(11, clock);
        inv.setItem(13, book);
        inv.setItem(15, bookshelf);


        player.openInventory(inv);
        return false;
    }
}
