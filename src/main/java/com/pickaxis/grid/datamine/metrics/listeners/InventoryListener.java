package com.pickaxis.grid.datamine.metrics.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

/**
 * Collects inventory metrics.
 */
public class InventoryListener extends AbstractMetricListener
{
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onItemPickup( InventoryPickupItemEvent event )
    {
        this.getClient().increment( "inventory.items.pickup", "inventory-dest:" + event.getInventory().getType().name(),
                                                              "item:" + event.getItem().getItemStack().getType().name(),
                                                              "world:" + event.getItem().getWorld().getName() );
    }
    
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onItemMove( InventoryMoveItemEvent event )
    {
        this.getClient().increment( "inventory.items.move", "inventory-dest:" + event.getDestination().getType().name(),
                                                            "inventory-src:" + event.getSource().getType().name(),
                                                            "inventory-init:" + event.getInitiator().getType().name() );
    }
}
