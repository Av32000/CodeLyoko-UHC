package fr.av.codelyokouhc.listeners;

import fr.av.codelyokouhc.GState;
import fr.av.codelyokouhc.Main;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

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
        Entity killer = (Entity) e.getDamager();

        if(killer instanceof Player){
            if(!main.isState(GState.PVP)){
                e.setCancelled(true);
                return;
            }
        }

        if(killer instanceof Arrow){
            Arrow arrow = (Arrow) killer;
            if(arrow.getShooter() instanceof Player){
                if(!main.isState(GState.PVP)){
                    e.setCancelled(true);
                    return;
                }
            }
        }

        if(killer instanceof Fireball){
            Fireball fireball = (Fireball) killer;
            if(fireball.getShooter() instanceof Player){
                if(!main.isState(GState.PVP)){
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
}
