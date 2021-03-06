package com.pickaxis.grid.datamine;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DataMineCommand implements CommandExecutor
{
    public static final String MESSAGE_PREFIX = ChatColor.DARK_AQUA + "[DataMine] " + ChatColor.AQUA;
    
    public void message( CommandSender sender, String message )
    {
        sender.sendMessage( DataMineCommand.MESSAGE_PREFIX + message );
    }
    
    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args )
    {
        DataMinePlugin dm = DataMinePlugin.getInstance();
        
        this.message( sender, "DataMine v" + dm.getDescription().getVersion() + " (" +
                              dm.getBuildInfo().getProperty( "git.branch" ) + "/" +
                              dm.getBuildInfo().getProperty( "git.commit.id.describe" ) + ")" );

        String builtBy = "";
        if( sender.hasPermission( "grid.cmd.datamine.info.extended" ) )
        {
            builtBy = "by " + dm.getBuildInfo().getProperty( "git.build.user.name" );
        }
        this.message( sender, "Built " + builtBy + " on " +
                              dm.getBuildInfo().getProperty( "git.build.time" ) );

        if( sender.hasPermission( "grid.cmd.datamine.info.extended" ) )
        {
            this.message( sender, "Full Commit Hash: " + dm.getBuildInfo().getProperty( "git.commit.id" ) );
            this.message( sender, "Commit Message: " + dm.getBuildInfo().getProperty( "git.commit.message.short" ) );
            this.message( sender, "Committed by " + dm.getBuildInfo().getProperty( "git.commit.user.name" ) + " <" +
                                  dm.getBuildInfo().getProperty( "git.commit.user.email" ) + "> on " +
                                  dm.getBuildInfo().getProperty( "git.commit.time" ) );
        }
        
        return true;
    }
}
