package com.pickaxis.grid.datamine;

import com.pickaxis.grid.datamine.metrics.MetricCollector;
import com.pickaxis.grid.datamine.metrics.*;
import lombok.Getter;

/**
 * List of metric collectors.
 */
public enum MetricCollectors
{
    SERVER( ServerMetrics.class ),
    WORLD( WorldMetrics.class );
    
    @Getter
    private Class<? extends MetricCollector> cls;
    
    MetricCollectors( Class<? extends MetricCollector> cls )
    {
        this.cls = cls;
    }
}
