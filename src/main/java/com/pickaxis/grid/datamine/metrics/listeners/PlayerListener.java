package com.pickaxis.grid.datamine.metrics.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Collects metrics for chat messages and commands.
 */
public class PlayerListener extends AbstractMetricListener
{
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onPlayerLogin( PlayerLoginEvent event )
    {
        this.getClient().increment( "players.logins" );
    }
    
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onPlayerLogout( PlayerQuitEvent event )
    {
        this.getClient().increment( "players.logouts" );
    }
    
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onPlayerCommand( PlayerCommandPreprocessEvent event )
    {
        this.getClient().increment( "players.commands" );
    }
    
    @EventHandler( priority = EventPriority.MONITOR, 
                   ignoreCancelled = true )
    public void onPlayerChat( AsyncPlayerChatEvent event )
    {
        this.getClient().increment( "players.chat_messages" );
    }
}
