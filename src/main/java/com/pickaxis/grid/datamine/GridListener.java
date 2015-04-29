package com.pickaxis.grid.datamine;

import com.pickaxis.grid.core.events.GridInitializedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * Listens for Grid events.
 */
public class GridListener implements Listener
{
    /**
     * Start collecting metrics after Grid has initialized.
     */
    @EventHandler( priority = EventPriority.MONITOR )
    public void onGridInitializedEvent( GridInitializedEvent event )
    {
        DataMinePlugin.getInstance().initialize();
        
        // Don't attempt to initialize again if Grid is reloaded.
        HandlerList.unregisterAll( this );
    }
}
