package com.pickaxis.grid.datamine;

import com.pickaxis.grid.datamine.metrics.MetricCollector;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Sends metrics to DogStatsD.
 */
public class SendMetricsTask extends BukkitRunnable
{
    @Getter
    public final Collection<MetricCollector> collectors;
    
    public SendMetricsTask()
    {
        this.collectors = new HashSet<>();
        for( MetricCollectors collector : MetricCollectors.values() )
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
    
    @Override
    public void run()
    {
        for( MetricCollector collector : this.getCollectors() )
        {
            collector.collect();
        }
    }
}
