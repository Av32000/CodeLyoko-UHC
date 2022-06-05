package fr.av.codelyokouhc.commands;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import fr.av.codelyokouhc.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class GenerateUsineCommand implements CommandExecutor {
    Main main;
    public GenerateUsineCommand(Main main) {
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player player = ((Player) commandSender).getPlayer();
            Location location = player.getLocation();
            WorldEditPlugin worldEdit = main.getWorldEdit();
            File shematic = new File(main.getDataFolder(), "factory.schematic");
            System.out.println(main.getDataFolder().toString());
            EditSession session = worldEdit.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 999999);
            try{
                CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(shematic).load(shematic);
                clipboard.rotate2D(90);
                clipboard.paste(session, new Vector(location.getX(),location.getY(), location.getZ()), false);
            }catch (MaxChangedBlocksException | DataException | IOException e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
