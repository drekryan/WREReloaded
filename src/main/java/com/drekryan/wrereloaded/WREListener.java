package com.drekryan.wrereloaded;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class WREListener implements Listener
{
    private final WREReloaded plugin;
    private RegionContainer container;
    private boolean isEnabled = true;

    public WREListener( final WREReloaded plugin )
    {
        this.plugin = plugin;
        this.container = plugin.getWorldGuard().getRegionContainer();
    }

    @EventHandler
    public void onPlayerMove( PlayerMoveEvent event )
    {
        if ( container == null )
            return;

        Player player = event.getPlayer();
        Location newLocation = event.getTo();
        Location oldLocation = event.getFrom();

        RegionQuery query = container.createQuery();
        RegionManager regions = container.get( newLocation.getWorld() );

        if ( regions != null )
        {
            ApplicableRegionSet oldRegions = query.getApplicableRegions( oldLocation );
            ApplicableRegionSet currentRegions = query.getApplicableRegions( newLocation );

            // Clear old region effects
            for ( ProtectedRegion region : oldRegions )
            {
                Map<PotionEffectType, Integer> regionEffects = getEffectTypesForRegion( region );
                if ( regionEffects == null )
                    continue;

                for ( PotionEffectType effectType : regionEffects.keySet() )
                {
                    if ( effectType != null )
                        player.removePotionEffect( effectType );
                }
            }

            if ( isEnabled )
            {
                // Set new region effects
                for ( ProtectedRegion region : currentRegions )
                {
                    Map<PotionEffectType, Integer> regionEffects = getEffectTypesForRegion( region );
                    if ( regionEffects == null )
                    {
                        continue;
                    }

                    for ( Map.Entry<PotionEffectType, Integer> effect : regionEffects.entrySet() )
                    {
                        PotionEffectType type = effect.getKey();
                        int level = effect.getValue();

                        if ( type != null )
                        {
                            PotionEffect potionEffect = new PotionEffect( type, 10000 * 20, level - 1, true, false, null );
                            player.addPotionEffect( potionEffect );
                        }
                    }
                }
            }
        }
    }

    public Map<PotionEffectType, Integer> getEffectTypesForRegion( ProtectedRegion region )
    {
        Map<PotionEffectType, Integer> regionEffects = new HashMap<>();

        String effectsFlag = region.getFlag( plugin.wreFlag );
        if ( effectsFlag == null || effectsFlag.equalsIgnoreCase( "" ) )
            return null;

        String[] effects = effectsFlag.split( "," );
        for ( String effect : effects )
        {
            String[] effectTokens = effect.trim().split( " " );
            PotionEffectType type;
            Integer level;

            if ( effectTokens.length < 2 || effectTokens[1] == null || effectTokens[1].equalsIgnoreCase( "" ) )
            {
                level = 1;
            }
            else
            {
                if ( effectTokens[1].trim().equalsIgnoreCase( "I" ) )
                    level = 1;
                else if ( effectTokens[1].equalsIgnoreCase( "II" ) )
                    level = 2;
                else if ( effectTokens[1].equalsIgnoreCase( "III" ) )
                    level = 3;
                else if ( effectTokens[1].equalsIgnoreCase( "IV" ) )
                    level = 4;
                else if ( effectTokens[1].equalsIgnoreCase( "V" ) )
                    level = 5;
                else
                {
                    try
                    {
                        level = Integer.valueOf( effectTokens[1] );
                    }
                    catch ( NumberFormatException ex )
                    {
                        level = 1;
                    }
                }
            }

            type = getPotionEffectType( effectTokens[0].replaceAll(" ", "").trim() );
            regionEffects.put( type, level );
        }

        return regionEffects;
    }

    public PotionEffectType getPotionEffectType( String effectString )
    {
        PotionEffectType type = null;

        switch ( effectString.toLowerCase() )
        {
            case "absorption":
                type = PotionEffectType.ABSORPTION;
                break;
            case "badluck":
            case "bad_luck":
            case "unluck":
                type = PotionEffectType.UNLUCK;
                break;
            case "blind":
            case "blindness":
                type = PotionEffectType.BLINDNESS;
                break;
            case "fire_resist":
            case "fireresist":
            case "fireresistance":
            case "fire_resistance":
                type = PotionEffectType.FIRE_RESISTANCE;
                break;
            case "glow":
            case "glowing":
                type = PotionEffectType.GLOWING;
                break;
            case "efficiency":
            case "haste":
            case "fastdigging":
            case "fast_digging":
                type = PotionEffectType.FAST_DIGGING;
                break;
            case "healthboost":
            case "health_boost":
                type = PotionEffectType.HEALTH_BOOST;
                break;
            case "hunger":
                type = PotionEffectType.HUNGER;
                break;
            case "instantdamage":
            case "instant_damage":
            case "damage":
                type = PotionEffectType.HARM;
                break;
            case "instanthealth":
            case "instant_health":
            case "health":
                type = PotionEffectType.HEAL;
                break;
            case "invis":
            case "invisibility":
                type = PotionEffectType.INVISIBILITY;
                break;
            case "jumpboost":
            case "jump_boost":
            case "jump":
                type = PotionEffectType.JUMP;
                break;
            case "levitation":
                type = PotionEffectType.LEVITATION;
                break;
            case "luck":
                type = PotionEffectType.LUCK;
                break;
            case "miningfatigue":
            case "slowdigging":
            case "mining_fatigue":
            case "slow_digging":
                type = PotionEffectType.SLOW_DIGGING;
                break;
            case "nausea":
            case "confusion":
                type = PotionEffectType.CONFUSION;
                break;
            case "nightvision":
            case "night_vision":
                type = PotionEffectType.NIGHT_VISION;
                break;
            case "poison":
                type = PotionEffectType.POISON;
                break;
            case "regen":
            case "regeneration":
                type = PotionEffectType.REGENERATION;
                break;
            case "resist":
            case "resistance":
            case "damageresistance":
            case "damage_resistance":
            case "damageresist":
            case "damage_resist":
                type = PotionEffectType.DAMAGE_RESISTANCE;
                break;
            case "saturation":
                type = PotionEffectType.SATURATION;
                break;
            case "slowness":
            case "slow":
            case "reducespeed":
            case "reduce_speed":
                type = PotionEffectType.SLOW;
                break;
            case "speed":
            case "swiftness":
            case "increasespeed":
            case "increase_speed":
                type = PotionEffectType.SPEED;
                break;
            case "stren":
            case "str":
            case "strength":
                type = PotionEffectType.INCREASE_DAMAGE;
                break;
            case "waterbreathing":
            case "breath":
            case "water_breathing":
                type = PotionEffectType.WATER_BREATHING;
                break;
            case "weakness":
            case "weak":
            case "weaken":
                type = PotionEffectType.WEAKNESS;
                break;
            case "wither":
                type = PotionEffectType.WITHER;
                break;
        }

        return type;
    }

    public void setEnabled( boolean isEnabled )
    {
        this.isEnabled = isEnabled;
    }

    public boolean isEnabled()
    {
        return isEnabled;
    }

    public void toggleEnabled()
    {
        this.isEnabled = !this.isEnabled;
    }
}
