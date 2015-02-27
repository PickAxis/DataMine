package com.pickaxis.grid.datamine;

import com.pickaxis.grid.datamine.metrics.tasks.MetricCollector;
import com.pickaxis.grid.datamine.metrics.tasks.ServerMetrics;
import com.pickaxis.grid.datamine.metrics.tasks.WorldMetrics;
import lombok.Getter;

/**
 * 
 */
@Getter
public enum Metrics
{
    TPS( ServerMetrics.class ),
    TASKS_RUNNING( ServerMetrics.class ),
    TASKS_PENDING( ServerMetrics.class ),
    PLUGINS_LOADED( ServerMetrics.class ),
    PLUGINS_ENABLED( ServerMetrics.class ),
    MEMORY_MAXIMUM( ServerMetrics.class ),
    MEMORY_ALLOCATED( ServerMetrics.class ),
    MEMORY_USED( ServerMetrics.class ),
    WORLDS( WorldMetrics.class ),
    PLAYERS_ONLINE( WorldMetrics.class, true ),
    CHUNKS( WorldMetrics.class, true ),
    TILES( WorldMetrics.class, true ),
    ENTITIES( WorldMetrics.class, true );
    
    private final Class<? extends MetricCollector> cls;
    
    private final boolean tagged;
    
    Metrics( Class<? extends MetricCollector> cls, boolean tagged )
    {
        this.cls = cls;
        this.tagged = tagged;
    }
    
    Metrics( Class<? extends MetricCollector> cls )
    {
        this( cls, false );
    }
}
