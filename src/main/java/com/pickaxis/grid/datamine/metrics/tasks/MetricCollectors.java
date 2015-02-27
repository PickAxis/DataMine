package com.pickaxis.grid.datamine.metrics.tasks;

import lombok.Getter;

/**
 * List of metric collectors.
 */
public enum MetricCollectors
{
    SERVER( ServerMetrics.class ),
    WORLD( WorldMetrics.class );
    
    @Getter
    private final Class<? extends MetricCollector> cls;
    
    MetricCollectors( Class<? extends MetricCollector> cls )
    {
        this.cls = cls;
    }
}
