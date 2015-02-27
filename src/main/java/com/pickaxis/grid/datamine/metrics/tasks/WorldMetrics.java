package com.pickaxis.grid.datamine.metrics.tasks;

import com.pickaxis.grid.datamine.DataMinePlugin;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
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
        
        this.getClient().gauge( "worlds", Bukkit.getServer().getWorlds().size() );
        
        for( World world : Bukkit.getServer().getWorlds() )
        {
            this.getClient().gauge( "players.online", world.getPlayers().size(), "world:" + world.getName() );
            
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
            this.getClient().gauge( "world.tiles", tiles, "world:" + world.getName() );
            this.getClient().gauge( "world.chunks", world.getLoadedChunks().length, "world:" + world.getName() );
            
            for( EntityType type : EntityType.values() )
            {
                if( type.equals( EntityType.UNKNOWN ) )
                {
                    continue;
                }
                
                this.getClient().gauge( "world.entities", world.getEntitiesByClass( type.getEntityClass() ).size(), "world:" + world.getName(), "type:" + type.name() );
            }
        }
        
        if( DataMinePlugin.getInstance().isDebug() )
        {
            DataMinePlugin.getInstance().getLogger().log( Level.INFO, "WorldMetrics collected in {0}ns.", System.nanoTime() - startTime );
        }
    }
}
