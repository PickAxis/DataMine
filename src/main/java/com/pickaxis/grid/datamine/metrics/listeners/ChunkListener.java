package com.pickaxis.grid.datamine.metrics.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

/**
 * Collects chunk metrics.
 */
public class ChunkListener extends AbstractMetricListener
{
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onChunkLoad( ChunkLoadEvent event )
    {
        this.getClient().increment( "world.chunk.load", "world:" + event.getChunk().getWorld().getName() );
    }
    
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onChunkoad( ChunkUnloadEvent event )
    {
        this.getClient().increment( "world.chunk.unload", "world:" + event.getChunk().getWorld().getName() );
    }
    
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onChunkPopulate( ChunkPopulateEvent event )
    {
        this.getClient().increment( "world.chunk.populate", "world:" + event.getChunk().getWorld().getName() );
    }
}
