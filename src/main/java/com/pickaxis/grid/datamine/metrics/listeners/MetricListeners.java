package com.pickaxis.grid.datamine.metrics.listeners;

import lombok.Getter;

/**
 * List of metric collectors.
 */
public enum MetricListeners
{
    PLAYER( PlayerListener.class ),
    WORLD( WorldListener.class ),
    ENTITY( EntityListener.class );
    
    @Getter
    private final Class<? extends MetricListener> cls;
    
    MetricListeners( Class<? extends MetricListener> cls )
    {
        this.cls = cls;
    }
}
