package fr.av.codelyokouhc.listeners;

import fr.av.codelyokouhc.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HealthListeners implements Listener {
    Main main;
    public HealthListeners(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent e){
        if(e.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN || e.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }
}
