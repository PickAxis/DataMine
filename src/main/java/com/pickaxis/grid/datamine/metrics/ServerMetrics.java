package com.pickaxis.grid.datamine.metrics;

import com.pickaxis.grid.datamine.DataMinePlugin;
import java.util.logging.Level;
import net.minecraft.server.v1_8_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Sends server performance metrics.
 */
public class ServerMetrics extends AbstractMetric
{
    @Override
    public void collect()
    {
        long startTime = System.nanoTime();
        
        try
        {
            Class.forName( "net.minecraft.server.v1_8_R1.MinecraftServer" );
            this.getClient().gauge( "tps", MinecraftServer.getServer().recentTps[ 0 ] );
        }
        catch( ClassNotFoundException ex )
        {
            // Plugin needs updated.
        }
        
        this.getClient().gauge( "tasks.running", Bukkit.getServer().getScheduler().getActiveWorkers().size() );
        this.getClient().gauge( "tasks.pending", Bukkit.getServer().getScheduler().getPendingTasks().size() );
        
        int pluginsEnabled = 0;
        for( Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins() )
        {
            if( plugin.isEnabled() )
            {
                pluginsEnabled++;
            }
        }
        this.getClient().gauge( "plugins.loaded", Bukkit.getServer().getPluginManager().getPlugins().length );
        this.getClient().gauge( "plugins.enabled", pluginsEnabled );
        
        this.getClient().gauge( "memory.maximum", Runtime.getRuntime().maxMemory() );
        this.getClient().gauge( "memory.allocated", Runtime.getRuntime().totalMemory() );
        this.getClient().gauge( "memory.used", ( Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() ) );
        
        this.getClient().gauge( "players.maximum", Bukkit.getServer().getMaxPlayers() );
        
        if( DataMinePlugin.getInstance().isDebug() )
        {
            DataMinePlugin.getInstance().getLogger().log( Level.INFO, "ServerMetrics collected in {0}ns.", System.nanoTime() - startTime );
        }
    }
}
