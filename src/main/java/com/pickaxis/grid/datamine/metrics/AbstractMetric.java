package com.pickaxis.grid.datamine.metrics;

import com.pickaxis.grid.datamine.DataMinePlugin;
import com.timgroup.statsd.StatsDClient;

/**
 * Base metric class.
 */
public abstract class AbstractMetric implements MetricCollector
{
    protected StatsDClient getClient()
    {
        return DataMinePlugin.getInstance().getStatsd();
    }
}
