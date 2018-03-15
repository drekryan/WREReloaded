WorldGuard Region Effects Reloaded
======

WRE Reloaded is a remake of the old WorldGuard Region Effects Plugin for Bukkit

This plugin is a Minecraft Spigot Plugin which hooks into WorldGuard regions to add status
effects when you enter a region and remove them when the player leaves.

It adds a custom WorldGuard flag called "wre-effects" which allows you to
set the effects given for the region. The format of this flag should be like so.

regeneration 5, blindness, absorbtion 4

Each effect should be seperated by a comma. Each effect can optionally specify
a effect level by sperating the level and name with a space. Roman numerals up to 5
are supported such as I, II, III, IV, V. You can also use levels outside of normal
survival values. The plugin also supports negative levels to negate the effect. If you dont specify
a effect level it is assumed to be level 1. The plugin will recognize common names for each effect.
for example Regeneration can be said as "regen" or "regeneration" and Haste can be said as "fastdigging",
"fast_digging", "haste", or "efficiency".

Finally the effects for every region can be toggled globally with the '/wrereloaded toggle' command for server admins.