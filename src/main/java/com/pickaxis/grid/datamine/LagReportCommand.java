package com.pickaxis.grid.datamine;

import com.pickaxis.grid.core.util.LangUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LagReportCommand implements CommandExecutor
{
    public static final String MESSAGE_PREFIX = ChatColor.DARK_AQUA + "[DataMine] " + ChatColor.AQUA;
    
    public void message( CommandSender sender, String message )
    {
        sender.sendMessage( LagReportCommand.MESSAGE_PREFIX + message );
    }
    
    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args )
    {
        DataMinePlugin dm = DataMinePlugin.getInstance();
        dm.getStatsd().increment( "players.lag_reports", "player:" + ( (Player) sender ).getUniqueId().toString() + "/" + sender.getName() );
        this.message( sender, LangUtil.getString( "lagreport", "&eYour lag report has been successfully submitted to our server engineers.", dm) );
        return true;
    }
}
