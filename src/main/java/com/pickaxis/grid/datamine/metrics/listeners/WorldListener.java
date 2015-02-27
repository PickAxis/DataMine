package com.pickaxis.grid.datamine.metrics.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

/**
 * Collects metrics for chat messages and commands.
 */
public class WorldListener extends AbstractMetricListener
{
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onWorldLoad( WorldLoadEvent event )
    {
        String message = "[Loaded] World " + event.getWorld().getName() + " has been loaded on " + this.getInstanceName();
        this.getEventClient().event( message, message );
    }
    
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onWorldLoad( WorldUnloadEvent event )
    {
        String message = "[Unloaded] World " + event.getWorld().getName() + " has been unloaded on " + this.getInstanceName();
        this.getEventClient().event( message, message );
    }
}
