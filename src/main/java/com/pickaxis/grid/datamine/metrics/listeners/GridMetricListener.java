package com.pickaxis.grid.datamine.metrics.listeners;

import com.pickaxis.grid.core.GridPlugin;
import com.pickaxis.grid.core.events.GridMetricEvent;
import java.util.Scanner;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

/**
 * Handles metric events from Grid.
 */
public class GridMetricListener extends AbstractMetricListener
{
    public static final String REQUIRED_GRID_VERSION = "1.0.1";
    
    @Override
    public boolean shouldRegister()
    {
        if( !Bukkit.getPluginManager().isPluginEnabled( "Grid" ) )
        {
            return false;
        }
        
        Scanner gridVer = new Scanner( GridPlugin.getInstance().getDescription().getVersion() ).useDelimiter( "\\." );
        Scanner gridReq = new Scanner( GridMetricListener.REQUIRED_GRID_VERSION ).useDelimiter( "\\." );
        
        while( gridVer.hasNextInt() && gridReq.hasNextInt() )
        {
            int gridVerComp = gridVer.nextInt();
            int gridReqComp = gridReq.nextInt();
            
            if( gridVerComp > gridReqComp )
            {
                return true;
            }
            else if( gridVerComp < gridReqComp )
            {
                return false;
            }
        }
        
        return false;
    }
    
    @EventHandler
    public void onGridMetric( GridMetricEvent event )
    {
        switch( event.getType() )
        {
            case COUNT_DELTA:
                this.getClient().count( event.getKey(), event.getValue().longValue(), event.getTags() );
                break;
            case COUNT_INCREMENT:
                this.getClient().increment( event.getKey(), event.getTags() );
                break;
            case COUNT_DECREMENT:
                this.getClient().decrement( event.getKey(), event.getTags() );
                break;
            case GAUGE_DOUBLE:
                this.getClient().gauge( event.getKey(), event.getValue().doubleValue(), event.getTags() );
                break;
            case GAUGE_LONG:
                this.getClient().gauge( event.getKey(), event.getValue().longValue(), event.getTags() );
                break;
            case HISTOGRAM_DOUBLE:
                this.getClient().histogram( event.getKey(), event.getValue().doubleValue(), event.getTags() );
                break;
            case HISTOGRAM_LONG:
                this.getClient().histogram( event.getKey(), event.getValue().longValue(), event.getTags() );
                break;
            case EXECUTION_TIME:
                this.getClient().recordExecutionTime( event.getKey(), event.getValue().longValue(), event.getTags() );
                break;
        }
    }
}
