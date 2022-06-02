package fr.av.codelyokouhc;

import fr.av.codelyokouhc.enums.GState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WaitingPvp extends BukkitRunnable {
    Main main;
    private int role = 0;
    private int roleSec = 59;
    private int pvp = 1;
    private int pvpsSec = 59;
    private Boolean endRole = false;
    private Boolean endPvp = false;
    public WaitingPvp(Main main){
        this.main = main;
    }
    @Override
    public void run() {
        UpdateScoreBoard();
        if(role == 0 && roleSec == 0 && !endRole){
            main.setState(GState.Role);
            SelectRole();
            endRole = true;
        }
        if(pvp == 0 && pvpsSec == 0 && !endPvp){
            main.setState(GState.PVP);
            endPvp = true;
        }
        if(roleSec == 0 && !endRole){
            role--;
            roleSec = 59;
        }
        if(pvpsSec == 0 && !endPvp){
            pvp--;
            pvpsSec = 59;
        }
        if(!endPvp){
            pvpsSec--;
        }
        if(!endRole){
            roleSec--;
        }
    }

    private void SelectRole(){
        for (Player player : main.getServer().getOnlinePlayers()) {
            if(player.getGameMode() == GameMode.SURVIVAL){
                Random rnd = new Random();
                int index = rnd.nextInt(main.getNonAttribuateRoles().size() - 0 + 1);
                main.getRoles().put(player, main.getNonAttribuateRoles().get(index));
                main.getNonAttribuateRoles().remove(index);
                player.sendMessage("Vous etes : " + main.getRoles().get(player).toString());
            }
        }
    }

    private void UpdateScoreBoard(){
        for (Player player:main.getServer().getOnlinePlayers()) {
            if(player.getGameMode() != GameMode.SPECTATOR){
                ScoreboardManagerUtils smu = new ScoreboardManagerUtils();
                Map<String,Integer> values = new HashMap<>();
                values.put(" ", 5);
                if(endRole){
                    values.put("§6Roles : §e" + main.getRoles().get(player).toString(), 4);
                }else{
                    String text = "";
                    if(role < 10){
                        text = text + "0" + role;
                    }else{
                        text = text + role;
                    }
                    text = text + ":";
                    if(roleSec < 10){
                        text = text + "0" + roleSec;
                    }else{
                        text = text + roleSec;
                    }
                    values.put("§6Roles : §e" + text, 4);
                }
                if(endPvp){
                    values.put("§bPvP : §aActivé", 3);
                }else{
                    String text = "";
                    if(pvp < 10){
                        text = text + "0" + pvp;
                    }else{
                        text = text + pvp;
                    }
                    text = text + ":";
                    if(pvpsSec < 10){
                        text = text + "0" + pvpsSec;
                    }else{
                        text = text + pvpsSec;
                    }
                    values.put("§bPvP : §3" + text, 3);
                }
                values.put("", 2);
                if(Integer.parseInt(main.getPlayerCount()) > 1){
                    values.put("§c" + main.getPlayerCount() + " joueurs", 1);
                }else{
                    values.put("§c" + main.getPlayerCount() + " joueur", 1);
                }
                smu.AddScoreBordToPlayer(player, values);
            }
            else{
                ScoreboardManagerUtils smu = new ScoreboardManagerUtils();
                Map<String,Integer> values = new HashMap<>();
                values.put(" ", 5);
                if(endRole){
                    values.put("§eRoles : §aRévélés", 4);
                }else{
                    String text = "";
                    if(role < 10){
                        text = text + "0" + role;
                    }else{
                        text = text + role;
                    }
                    text = text + ":";
                    if(roleSec < 10){
                        text = text + "0" + roleSec;
                    }else{
                        text = text + roleSec;
                    }
                    values.put("§eRoles : §a" + text, 4);
                }
                if(endPvp){
                    values.put("§bPvP : §3Activé", 3);
                }else{
                    String text = "";
                    if(pvp < 10){
                        text = text + "0" + pvp;
                    }else{
                        text = text + pvp;
                    }
                    text = text + ":";
                    if(pvpsSec < 10){
                        text = text + "0" + pvpsSec;
                    }else{
                        text = text + pvpsSec;
                    }
                    values.put("§bPvP : §3" + text, 3);
                }
                values.put("", 2);
                if(Integer.parseInt(main.getPlayerCount()) > 1){
                    values.put("§c" + main.getPlayerCount() + " joueurs", 1);
                }else{
                    values.put("§c" + main.getPlayerCount() + " joueur", 1);
                }
                smu.AddScoreBordToPlayer(player, values);
            }
        }
    }
}
