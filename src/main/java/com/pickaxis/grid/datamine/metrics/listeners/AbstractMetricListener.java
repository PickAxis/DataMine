package com.pickaxis.grid.datamine.metrics.listeners;

import com.github.arnabk.statsd.NonBlockingStatsDEventClient;
import com.pickaxis.grid.datamine.DataMinePlugin;
import com.timgroup.statsd.StatsDClient;

/**
 * Base class for metric listeners.
 */
public abstract class AbstractMetricListener implements MetricListener
{
    protected String getInstanceName()
    {
        return DataMinePlugin.getInstance().getInstanceName();
    }
    
    protected StatsDClient getClient()
    {
        return DataMinePlugin.getInstance().getStatsd();
    }
    
    protected NonBlockingStatsDEventClient getEventClient()
    {
        return DataMinePlugin.getInstance().getEventsd();
    }
}
