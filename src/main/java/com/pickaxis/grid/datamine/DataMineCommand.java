package com.pickaxis.grid.datamine;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DataMineCommand implements CommandExecutor
{
    public static final String MESSAGE_PREFIX = ChatColor.DARK_AQUA + "[Grid Vote Receiver] " + ChatColor.AQUA;
    
    public void message( CommandSender sender, String message )
    {
        sender.sendMessage( DataMineCommand.MESSAGE_PREFIX + message );
    }
    
    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args )
    {
        DataMinePlugin gridvr = DataMinePlugin.getInstance();
        
        this.message( sender, "DataMine v" + gridvr.getDescription().getVersion() + " (" +
                              gridvr.getBuildInfo().getProperty( "git.branch" ) + "/" +
                              gridvr.getBuildInfo().getProperty( "git.commit.id.describe" ) + ")" );
        
        String builtBy = "";
        if( sender.hasPermission( "grid.cmd.datamine.info.extended" ) )
        {
            builtBy = "by " + gridvr.getBuildInfo().getProperty( "git.build.user.name" );
        }
        this.message( sender, "Built " + builtBy + " on " +
                              gridvr.getBuildInfo().getProperty( "git.build.time" ) );
        
        if( sender.hasPermission( "grid.cmd.datamine.info.extended" ) )
        {
            this.message( sender, "Full Commit Hash: " + gridvr.getBuildInfo().getProperty( "git.commit.id" ) );
            this.message( sender, "Commit Message: " + gridvr.getBuildInfo().getProperty( "git.commit.message.short" ) );
            this.message( sender, "Committed by " + gridvr.getBuildInfo().getProperty( "git.commit.user.name" ) + " <" +
                                  gridvr.getBuildInfo().getProperty( "git.commit.user.email" ) + "> on " +
                                  gridvr.getBuildInfo().getProperty( "git.commit.time" ) );
        }
        
        return true;
    }
}
