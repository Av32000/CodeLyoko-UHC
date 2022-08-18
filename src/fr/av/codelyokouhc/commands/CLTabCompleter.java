package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.enums.GRoles;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CLTabCompleter implements TabCompleter {
    Main main;

    public CLTabCompleter(Main main) {
        this.main = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> args = new ArrayList<>();
        args.add("help");
        args.add("overworld");
        args.add("spectator");

        if(main.getRoles().size() != 0){
            Player p = (Player) commandSender;
            GRoles role = main.getRoles().get(p);
            if(role == GRoles.ChefDuXana){
                args.add("blocks");
                args.add("MegaTank");
                args.add("kankrelats");
                args.add("spectre");
                args.add("scan");
            }else if(role == GRoles.TamiyaDiop){
                args.add("ask");
            }else if (role == GRoles.UlrichStern){
                args.add("hide");
            }else if (role == GRoles.JeremyBelpois){
                args.add("hide");
                args.add("revive");
            }
        }
        return args;
    }
}
