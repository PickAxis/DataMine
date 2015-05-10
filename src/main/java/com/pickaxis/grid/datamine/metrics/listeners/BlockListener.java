package com.pickaxis.grid.datamine.metrics.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

/**
 * Collects block metrics.
 */
public class BlockListener extends AbstractMetricListener
{
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onBlockBurn( BlockBurnEvent event )
    {
        this.getClient().increment( "block.burn", "world:" + event.getBlock().getWorld().getName() );
    }
    
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onRedstoneChange( BlockRedstoneEvent event )
    {
        this.getClient().increment( "block.redstone", "world:" + event.getBlock().getWorld().getName() );
    }
}
