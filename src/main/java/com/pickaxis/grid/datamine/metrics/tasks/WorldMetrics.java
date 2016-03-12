package com.pickaxis.grid.datamine.metrics.tasks;

import com.pickaxis.grid.datamine.DataMinePlugin;
import com.pickaxis.grid.datamine.SendMetricsTask;
import java.util.ConcurrentModificationException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

/**
 * Sends world metrics.
 */
public class WorldMetrics extends AbstractMetricTask
{
    @Override
    public void collect()
    {
        long startTime = System.nanoTime();
        
        try
        {
            this.getClient().gauge( "worlds", Bukkit.getServer().getWorlds().size() );
            
            for( World world : Bukkit.getServer().getWorlds() )
            {
                this.getClient().gauge( "players.online", world.getPlayers().size(), "world:" + world.getName() );
                
                if( DataMinePlugin.getInstance().getSyncTask() instanceof SendMetricsTask )
                {
                    this.getClient().gauge( "world.tiles", ( (WorldSyncMetrics) DataMinePlugin.getInstance().getSyncTask().getCollectors().get( WorldSyncMetrics.class ) ).getLastTileCounts().get( world ), "world:" + world.getName() );
                }
                
                this.getClient().gauge( "world.chunks", world.getLoadedChunks().length, "world:" + world.getName() );
                
                for( EntityType type : EntityType.values() )
                {
                    if( type.equals( EntityType.UNKNOWN ) )
                    {
                        continue;
                    }
                    
                    this.getClient().gauge( "world.entities", world.getEntitiesByClass( type.getEntityClass() ).size(), "world:" + world.getName(), "type:" + type.name() );
                }
                
                this.getClient().gauge( "world.entities.living", world.getLivingEntities().size() );
            }
        }
        catch( ConcurrentModificationException ex )
        {
            DataMinePlugin.getInstance().getLogger().log( Level.INFO, "Encountered a CME while collecting world metrics.  Will continue next run." );
        }
        
        if( DataMinePlugin.getInstance().isDebug() )
        {
            DataMinePlugin.getInstance().getLogger().log( Level.INFO, "WorldMetrics collected in {0}ns.", System.nanoTime() - startTime );
        }
    }
}
