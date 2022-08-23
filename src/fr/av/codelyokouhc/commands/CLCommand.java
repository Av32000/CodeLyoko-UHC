package fr.av.codelyokouhc.commands;

import fr.av.codelyokouhc.Main;
import fr.av.codelyokouhc.loops.InfectLoop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CLCommand implements CommandExecutor {
    Main main;
    public CLCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 0 || strings[0].equalsIgnoreCase("help")){
            sendHelpMsg(commandSender);
        }
        else if(strings[0].equalsIgnoreCase("hide")){
            HideCommand hideCommand = new HideCommand(main);
            hideCommand.onCommand(commandSender, command, s,strings);
        }
        else if(strings[0].equalsIgnoreCase("overworld")){
            OverworldCommand overworldCommand = new OverworldCommand(main);
            overworldCommand.onCommand(commandSender, command, s,strings);
        } else if (strings[0].equalsIgnoreCase("revive")) {
            ReviveCommand reviveCommand = new ReviveCommand(main);
            reviveCommand.onCommand(commandSender, command, s,strings);
        } else if (strings[0].equalsIgnoreCase("ask")) {
            AskCommand askCommand = new AskCommand(main);
            askCommand.onCommand(commandSender, command, s,strings);
        }else if(strings[0].equalsIgnoreCase("MegaTank")){
            MegaTankCommand megaTankCommand = new MegaTankCommand(main);
            megaTankCommand.onCommand(commandSender, command, s,strings);
        }
        else if(strings[0].equalsIgnoreCase("kankrelats")){
            KankrelatsCommand kankrelatsCommand = new KankrelatsCommand(main);
            kankrelatsCommand.onCommand(commandSender, command, s,strings);
        }
        else if(strings[0].equalsIgnoreCase("blocks")){
            BlocksCommand blocksCommand = new BlocksCommand(main);
            blocksCommand.onCommand(commandSender, command, s,strings);
        }
        else if(strings[0].equalsIgnoreCase("infect")){
            InfectCommand infectCommand = new InfectCommand(main);
            infectCommand.onCommand(commandSender, command, s,strings);
        }
        else if(strings[0].equalsIgnoreCase("scan")){
            ScanCommand scanCommand = new ScanCommand(main);
            scanCommand.onCommand(commandSender, command, s,strings);
        }
        else if(strings[0].equalsIgnoreCase("spectre")){
            SpectreCommand spectreCommand = new SpectreCommand(main);
            spectreCommand.onCommand(commandSender, command, s,strings);
        }
        else if(strings[0].equalsIgnoreCase("spectator")){
            SpectatorCommand spectatorCommand = new SpectatorCommand(main);
            spectatorCommand.onCommand(commandSender, command, s,strings);
        }
        else if(strings[0].equalsIgnoreCase("object")){
            CLObjectCommand objectCommand = new CLObjectCommand(main);
            objectCommand.onCommand(commandSender, command, s,strings);
        }
        else{
            commandSender.sendMessage("§cCet argument n'existe pas (Voir /cl help)");
        }
        return true;
    }

    void sendHelpMsg(CommandSender commandSender){
        commandSender.sendMessage("§aCodeLyoko-UHC");
        if(main.getRoles().size() != 0) commandSender.sendMessage("§6Vous êtes : " + main.getRoles().get(commandSender).toString());
        commandSender.sendMessage("");
        commandSender.sendMessage("§d/cl help §f=> §bAffiche ce message");
        commandSender.sendMessage("§d/cl hide §f=> §bRends le joueur invisible pendant un certain temps");
        commandSender.sendMessage("§d/cl overworld §f=> §bTéléporte le joueur du lyoko vers l'overworld");
        commandSender.sendMessage("§d/cl revive §f=> §bPermet à Jeremy Belpois de réanimer les joueurs morts dans la dèrnière minute (2 fois)");
        commandSender.sendMessage("§d/cl ask §f=> §bPermet à Tamiya Diop de poser une question à un joueur (15min de cooldown)");
        commandSender.sendMessage("§d/cl blocks §f=> §bLe Chef du XANA transforme l'agent en bloks, ils possèdent speed 1 et + 5% d'attaque a chaque kill");
        commandSender.sendMessage("§d/cl MegaTank §f=> §bLe Chef du XANA transforme l'agent en MegaTank, il possède force 1 résistance 1 et lenteur 2");
        commandSender.sendMessage("§d/cl kankrelats §f=> §bLe Chef du XANA transforme l'agent en kankrelats, il possède lenteur 1 et Fire Aspect");
        commandSender.sendMessage("§d/cl spectre §f=> §bLe Chef du XANA transforme l'agent en spectre, il possède un item qui leur donne un fly (5sec)");
        commandSender.sendMessage("§d/cl scan §f=> §bPermet au Chef du XANA de connaitre le nombre d'agent se trouvant à moins de 20 blocs de lui (3 fois)");
        commandSender.sendMessage("§d/cl spectator §f=> §bPermet aux joueurs éliminés de voir les roles des joueurs restants et de se tp à eux");
        commandSender.sendMessage("§d/cl object §f=> §bPermet aux joueurs qui possède un objet spécial grace à leur rôle de le récupérer en cas de perte");
    }
}
