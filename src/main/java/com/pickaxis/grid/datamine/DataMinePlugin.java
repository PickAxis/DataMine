package com.pickaxis.grid.datamine;

import com.github.arnabk.statsd.NonBlockingStatsDEventClient;
import com.pickaxis.grid.core.GridPlugin;
import com.pickaxis.grid.core.server.ServerDataManager;
import com.pickaxis.grid.datamine.metrics.listeners.MetricListeners;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main DataMine class.
 */
@Getter
@Setter( AccessLevel.PRIVATE )
public class DataMinePlugin extends JavaPlugin
{
    @Getter
    @Setter( AccessLevel.PRIVATE )
    private static DataMinePlugin instance;
    
    private String instanceName;
    
    private StatsDClient statsd;
    
    private NonBlockingStatsDEventClient eventsd;
    
    private SendMetricsTask task;
    
    private Properties buildInfo;
    
    private boolean debug;
    
    /**
     * Sets singleton instance.
     */
    public DataMinePlugin()
    {
        DataMinePlugin.setInstance( this );
    }
    
    /**
     * Register command and vote listener.
     */
    @Override
    public void onEnable()
    {
        this.setBuildInfo( new Properties() );
        try
        {
            this.getBuildInfo().load( this.getClass().getClassLoader().getResourceAsStream( "git.properties" ) );
        }
        catch( IOException ex )
        {
            this.getLogger().log( Level.WARNING, "Couldn't load build info.", ex );
        }
        
        this.setDebug( this.getConfig().getBoolean( "debug", false ) );
        
        this.getCommand( "datamine" ).setExecutor( new DataMineCommand() );
        
        if( !Bukkit.getPluginManager().isPluginEnabled( "Grid" ) || ( (GridPlugin) Bukkit.getPluginManager().getPlugin( "Grid" ) ).isGridInitialized() )
        {
            this.initialize();
        }
        else
        {
            this.getServer().getPluginManager().registerEvents( new GridListener(), this );
        }
    }
    
    /**
     * Cancels metric collection task, unregisters listeners.
     */
    @Override
    public void onDisable()
    {
        this.getTask().cancel();
        
        HandlerList.unregisterAll( this );
    }
    
    /**
     * Begins metric collection.
     */
    public void initialize()
    {
        this.setInstanceName( this.findInstanceName() );
        
        this.setStatsd( new NonBlockingStatsDClient( this.getConfig().getString( "prefix", "minecraft" ),
                                                     this.getConfig().getString( "host", "localhost" ),
                                                     this.getConfig().getInt( "port", 8125 ),
                                                     new String[] { "instance:" + this.getInstanceName() } ) );
        
        this.setEventsd( new NonBlockingStatsDEventClient( this.getConfig().getString( "host", "localhost" ),
                                                           this.getConfig().getInt( "port", 8125 ),
                                                           new String[] { "isntance:", this.getInstanceName() } ) );
        
        this.setTask( new SendMetricsTask() );
        if( this.getConfig().getBoolean( "async", true ) )
        {
            this.getTask().runTaskTimerAsynchronously( this, this.getConfig().getInt( "delay", 300 ), this.getConfig().getInt( "interval", 100 ) );
        }
        else
        {
            this.getTask().runTaskTimer( this, this.getConfig().getInt( "delay", 300 ), this.getConfig().getInt( "interval", 100 ) );
        }
        
        for( MetricListeners type : MetricListeners.values() )
        {
            try
            {
                this.getServer().getPluginManager().registerEvents( type.getCls().newInstance(), this );
            }
            catch( InstantiationException | IllegalAccessException ex )
            {
                this.getLogger().log( Level.SEVERE, "Couldn't initialize MetricListener:" + type.name(), ex );
            }
        }
        
        this.getEventsd().event( this.getInstanceName() + "'s DataMine plugin has initialized", 
                                 "See " + this.getInstanceName() + "'s <a href='https://app.datadoghq.com/dash/dash/41397?live=true&tile_size=m&tpl_var_scope=instance:" + this.getInstanceName() + "'>dashboard</a>" );
        
        this.getLogger().log( Level.INFO, "DataMine initialization completed." );
    }
    
    /**
     * Gets the name to identify this instance.
     * 
     * @return The name of this instance.
     */
    public String findInstanceName()
    {
        if( !this.getConfig().getString( "instance", "" ).isEmpty() )
        {
            return this.getConfig().getString( "instance" );
        }
        
        if( Bukkit.getPluginManager().isPluginEnabled( "Grid" ) )
        {
            return ( (GridPlugin) Bukkit.getPluginManager().getPlugin( "Grid" ) ).getManager( ServerDataManager.class ).getLocalServer().getDbRow().getSlug();
        }
        
        if( !Bukkit.getServerId().isEmpty() )
        {
            return Bukkit.getServerId();
        }
        
        return Bukkit.getIp() + ":" + Bukkit.getPort();
    }
}
