package com.pickaxis.grid.datamine;

import com.github.arnabk.statsd.NonBlockingStatsDEventClient;
import com.pickaxis.grid.core.GridPlugin;
import com.pickaxis.grid.core.server.ServerDataManager;
import com.pickaxis.grid.datamine.metrics.listeners.MetricListeners;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    
    private SendMetricsTask syncTask;
    
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
        
        if( this.getSyncTask() instanceof SendMetricsTask )
        {
            this.getSyncTask().cancel();
        }
        
        HandlerList.unregisterAll( this );
    }
    
    /**
     * Begins metric collection.
     */
    void initialize()
    {
        this.setInstanceName( this.findInstanceName() );
        
        List<String> configTags = new ArrayList<>();
        if( this.getConfig().isList( "tags" ) )
        {
            configTags = this.getConfig().getStringList( "tags" );
        }
        configTags.add( "instance:" + this.getInstanceName() );
        configTags.add( "environment:" + this.getConfig().getString( "environment", "production" ) );
        String[] tags = configTags.toArray( new String[ 0 ] );
        
        this.setStatsd( new NonBlockingStatsDClient( this.getConfig().getString( "prefix", "minecraft" ),
                                                     this.getConfig().getString( "host", "localhost" ),
                                                     this.getConfig().getInt( "port", 8125 ),
                                                     tags ) );
        
        this.setEventsd( new NonBlockingStatsDEventClient( this.getConfig().getString( "host", "localhost" ),
                                                           this.getConfig().getInt( "port", 8125 ),
                                                           tags ) );
        
        this.setTask( new SendMetricsTask( false ) );
        if( this.getConfig().getBoolean( "async", true ) )
        {
            this.getTask().runTaskTimerAsynchronously( this, this.getConfig().getInt( "delay", 300 ), this.getConfig().getInt( "interval", 100 ) );
        }
        else
        {
            this.getTask().runTaskTimer( this, this.getConfig().getInt( "delay", 300 ), this.getConfig().getInt( "interval", 100 ) );
        }
        
        this.setSyncTask( new SendMetricsTask( true ) );
        this.getSyncTask().runTaskTimer( this, this.getConfig().getInt( "sync.delay", 350 ), this.getConfig().getInt( "sync.interval", 600 ) );
        
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
        
        if( this.getConfig().getBoolean( "commands.lag", true ) )
        {
            this.getCommand( "lag" ).setExecutor( new LagReportCommand() );
        }
        
        if( this.getConfig().getBoolean( "events.startup", true ) )
        {
            this.getEventsd().event( this.getInstanceName() + "'s DataMine plugin has initialized", 
                                     "See " + this.getInstanceName() + "'s [dashboard](https://app.datadoghq.com/dash/dash/" + this.getConfig().getInt( "dashboard", 0 ) + "?live=true&tile_size=m&tpl_var_scope=instance:" + this.getInstanceName() + ")" );
        }
        
        this.getLogger().log( Level.INFO, "DataMine initialization completed." );
    }
    
    /**
     * Gets the name to identify this instance.
     * 
     * @return The name of this instance.
     */
    private String findInstanceName()
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
