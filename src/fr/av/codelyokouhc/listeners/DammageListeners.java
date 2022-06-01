package fr.av.codelyokouhc.listeners;

import fr.av.codelyokouhc.Main;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DammageListeners implements Listener {
    Main main;
    public DammageListeners(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent e){
        Entity victim = 
    }
}
