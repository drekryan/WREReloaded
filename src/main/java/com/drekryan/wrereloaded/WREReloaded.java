package com.drekryan.wrereloaded;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StringFlag;

public class WREReloaded extends JavaPlugin
{
    private WREReloaded instance;
    private WorldGuardPlugin worldGuard;
    public final StringFlag wreFlag = new StringFlag( "wre-effects", "" );
    private WREListener listener = new WREListener( this );

    @Override
    public void onLoad()
    {
        getWorldGuard().getFlagRegistry().register( wreFlag );
    }

    @Override
    public void onEnable()
    {
        super.onEnable();

        instance = this;
        getLogger().log( Level.INFO, getDescription().getName() + " version " + getDescription().getVersion() + " is enabled!" );

        worldGuard = getWorldGuard();
        if ( worldGuard == null )
        {
            getLogger().log( Level.SEVERE, "Could not find WorldGuard! WorldGuard 6.2 or later is required. Disabling..." );
            this.onDisable();
        }
        else
        {
            getLogger().log( Level.INFO, "Hooked into WorldGuard!" );
        }

        getServer().getPluginManager().registerEvents( listener, this );
    }

    @Override
    public void onDisable()
    {
        super.onDisable();
    }

    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args )
    {
        if ( args.length == 1 && args[0].equalsIgnoreCase( "toggle" ) )
        {
            if ( sender.isOp() )
            {
                listener.toggleEnabled();

                String state = listener.isEnabled() ? "on" : "off";
                ChatColor color = listener.isEnabled() ? ChatColor.GREEN : ChatColor.DARK_RED;
                sender.sendMessage( color + "Region status effects have been toggled " + state + "..." );
            }
            else
            {
                sender.sendMessage( ChatColor.DARK_RED + "Sorry you don't have permission..." );
            }

            return true;
        }

        return false;
    }

    public WREReloaded getPluginInstance()
    {
        return instance;
    }

    public WorldGuardPlugin getWorldGuard()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin( "WorldGuard" );

        // WorldGuard may not be loaded
        if ( plugin == null || !( plugin instanceof WorldGuardPlugin ) )
        {
            return null;
        }

        return ( WorldGuardPlugin ) plugin;
    }
}
