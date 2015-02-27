package com.pickaxis.grid.datamine;

import com.pickaxis.grid.core.GridPlugin;
import com.pickaxis.grid.core.server.ServerDataManager;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
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
    
    private StatsDClient statsd;
    
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
        
        this.setStatsd( new NonBlockingStatsDClient( this.getConfig().getString( "prefix", "minecraft" ),
                                                     this.getConfig().getString( "host", "localhost" ),
                                                     this.getConfig().getInt( "port", 8125 ),
                                                     new String[] { "instance:" + this.getInstanceName() } ) );
        
        this.setTask( new SendMetricsTask() );
        this.getTask().runTaskTimerAsynchronously( this, 300, 100 );
    }
    
    /**
     * Cancels metric collection task.
     */
    @Override
    public void onDisable()
    {
        this.getTask().cancel();
    }
    
    /**
     * Gets the name to identify this instance.
     * 
     * @return The name of this instance.
     */
    public String getInstanceName()
    {
        if( !this.getConfig().getString( "instance" ).isEmpty() )
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
