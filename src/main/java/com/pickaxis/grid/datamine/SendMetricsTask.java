package com.pickaxis.grid.datamine;

import com.pickaxis.grid.datamine.metrics.tasks.MetricCollectors;
import com.pickaxis.grid.datamine.metrics.tasks.MetricCollector;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Sends metrics to DogStatsD.
 */
@Getter
public class SendMetricsTask extends BukkitRunnable
{
    private final Collection<MetricCollector> collectors;
    
    private final boolean sync;
    
    public SendMetricsTask( boolean sync )
    {
        this.collectors = new HashSet<>();
        this.sync = sync;
        for( MetricCollectors collector : MetricCollectors.values() )
        {
            if( collector.isSync() == this.isSync() )
            {
                try
                {
                    this.getCollectors().add( collector.getCls().newInstance() );
                }
                catch( InstantiationException | IllegalAccessException ex )
                {
                    DataMinePlugin.getInstance().getLogger().log( Level.SEVERE, "Could not initialize collector: " + collector.name(), ex );
                }
            }
        }
    }
    
    @Override
    public void run()
    {
        for( MetricCollector collector : this.getCollectors() )
        {
            collector.collect();
        }
    }
}
