package fr.av.codelyokouhc.listeners;

import fr.av.codelyokouhc.enums.GState;
import fr.av.codelyokouhc.Main;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DammageListeners implements Listener {
    Main main;
    public DammageListeners(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player player = (Player)e.getEntity();
            if(player.getHealth() <= e.getDamage()){
                e.setDamage(0);
                main.eliminate(player);
            }
        }
    }

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player)){
            return;
        }

        if(e.getDamager() instanceof Player){
            if(!main.isState(GState.PVP)){
                e.setCancelled(true);
                return;
            }
            if(main.kankrelats.contains(e.getDamager())){
                e.getEntity().setFireTicks(1000);
            }
            if(main.blocks.containsKey(e.getDamager())){
                e.setDamage(e.getDamage() * main.blocks.get(e.getDamager()));
                if(((Player) e.getEntity()).getHealth() <= e.getFinalDamage()){
                    float newMultiplicator = main.blocks.get(e.getDamager()) * 1.5f;
                    main.blocks.remove(e.getDamager());
                    main.blocks.put((Player) e.getDamager(), newMultiplicator);
                }
            }
            if(main.lyokoBoostedPlayer.containsKey(e.getDamager())){
                e.setDamage(e.getFinalDamage() * (1 + (main.lyokoBoostedPlayer.get(e.getDamager()) * 0.2)));
                e.getDamager().sendMessage("Vos dégats on été multipliés par " + (1 + (main.lyokoBoostedPlayer.get(e.getDamager()) * 0.2)));
            }
        }

        if(e.getDamager() instanceof Arrow){
            Arrow arrow = (Arrow) e.getDamager();
            if(arrow.getShooter() instanceof Player){
                if(!main.isState(GState.PVP)){
                    e.setCancelled(true);
                    return;
                }
            }
        }

        if(e.getDamager() instanceof Fireball){
            Fireball fireball = (Fireball) e.getDamager();
            if(fireball.getShooter() instanceof Player){
                if(!main.isState(GState.PVP)){
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
}
