package com.pickaxis.grid.datamine.metrics.tasks;

import lombok.Getter;

/**
 * List of metric collectors.
 */
@Getter
public enum MetricCollectors
{
    SERVER( ServerMetrics.class ),
    WORLD( WorldMetrics.class ),
    WORLD_SYNC( WorldSyncMetrics.class, true );
    
    private final Class<? extends MetricCollector> cls;
    
    private final boolean sync;
    
    /**
     * A metric collector to be registered in SendMetricsTask.
     * 
     * @param cls The MetricCollector implementation.
     * @param sync Whether the collector <b>must</b> be run synchronously.
     */
    MetricCollectors( Class<? extends MetricCollector> cls, boolean sync )
    {
        this.cls = cls;
        this.sync = sync;
    }
    
    MetricCollectors( Class<? extends MetricCollector> cls )
    {
        this( cls, false );
    }
}
