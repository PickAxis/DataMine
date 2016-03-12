package com.pickaxis.grid.datamine.metrics.listeners;

import com.github.arnabk.statsd.Priority;
import com.pickaxis.grid.datamine.DataMinePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

/**
 * Sends events when worlds are loaded/unloaded.
 */
public class WorldListener extends AbstractMetricListener
{
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onWorldLoad( WorldLoadEvent event )
    {
        if( !DataMinePlugin.getInstance().getConfig().getBoolean( "events.worlds", true ) )
        {
            return;
        }
        
        String message = "[Loaded] World " + event.getWorld().getName() + " has been loaded on " + this.getInstanceName();
        this.getEventClient().event( message, message, Priority.low );
    }
    
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onWorldUnload( WorldUnloadEvent event )
    {
        if( !DataMinePlugin.getInstance().getConfig().getBoolean( "events.worlds", true ) )
        {
            return;
        }
        
        String message = "[Unloaded] World " + event.getWorld().getName() + " has been unloaded on " + this.getInstanceName();
        this.getEventClient().event( message, message, Priority.low );
    }
}
