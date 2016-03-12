package com.pickaxis.grid.datamine.metrics.tasks;

import com.pickaxis.grid.datamine.DataMinePlugin;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

/**
 * Sends (synchronously collected) world metrics.
 */
public class WorldSyncMetrics extends AbstractMetricTask
{
    @Getter
    private final Map<World, Integer> lastTileCounts;
    
    public WorldSyncMetrics()
    {
        this.lastTileCounts = new HashMap<>();
    }
    
    @Override
    public void collect()
    {
        long startTime = System.nanoTime();
        
        this.getLastTileCounts().clear();
        
        for( World world : Bukkit.getServer().getWorlds() )
        {
            int tiles = 0;
            
            try
            {
                for( Chunk chunk : world.getLoadedChunks() )
                {
                    tiles += chunk.getTileEntities().length;
                }
            }
            catch( ClassCastException ex )
            {
                DataMinePlugin.getInstance().getLogger().log( Level.SEVERE, "Corrupted chunk data on world " + world, ex );
            }
            catch( IllegalStateException ex )
            {
                DataMinePlugin.getInstance().getLogger().log( Level.INFO, "Entity added asynchronously while iterating tiles." );
            }
            
            this.getLastTileCounts().put( world, tiles );
        }
        
        if( DataMinePlugin.getInstance().isDebug() )
        {
            DataMinePlugin.getInstance().getLogger().log( Level.INFO, "WorldSyncMetrics collected in {0}ns.", System.nanoTime() - startTime );
        }
    }
}
