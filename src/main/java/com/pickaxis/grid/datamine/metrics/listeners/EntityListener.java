package com.pickaxis.grid.datamine.metrics.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

/**
 * Collects entity metrics.
 */
public class EntityListener extends AbstractMetricListener
{
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onEntitySpawn( EntitySpawnEvent event )
    {
        this.getClient().increment( "world.entities.spawns", "type:" + event.getEntityType().name() );
    }
    
    @EventHandler( priority = EventPriority.MONITOR,
                   ignoreCancelled = true )
    public void onEntityDeath( EntityDeathEvent event )
    {
        this.getClient().increment( "world.entities.despawns", "type:" + event.getEntityType().name() );
    }
    
    @EventHandler( priority = EventPriority.MONITOR,
                   ignoreCancelled = true )
    public void onCreatureSpawn( CreatureSpawnEvent event )
    {
        this.getClient().increment( "world.entities.spawns.creature", "type:" + event.getEntityType().name(), "reason:" + event.getSpawnReason().name() );
    }
}
