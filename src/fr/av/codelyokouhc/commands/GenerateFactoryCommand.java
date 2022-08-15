package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class GenerateFactoryCommand implements CommandExecutor {
    Main main;
    public GenerateFactoryCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Location factorySpawn = new Location(main.getServer().getWorld("world"), new Random().nextInt(500 - (-500)) + (-500), 0, new Random().nextInt(500 - (-500)) + (-500));
        int y = factorySpawn.getWorld().getHighestBlockYAt(factorySpawn);
        factorySpawn = new Location(factorySpawn.getWorld(), factorySpawn.getX(), y, factorySpawn.getZ());

        main.generateFactory(factorySpawn);

        TextComponent message = new TextComponent("§aClick to teleport");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + factorySpawn.getX() + " " + factorySpawn.getY() + " " + factorySpawn.getZ()));
        Player player = (Player) commandSender;
        player.spigot().sendMessage(message);
        return true;
    }
}
