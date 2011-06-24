package com.sleaker.NoCivilSpawns;

/**
 * A plugin for detecting spawns near pre-defined blockIDs
 *
 * @author Sleaker
 *
 */

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityListener;



public class NoSpawnCreatureSpawnEvent extends EntityListener {
    public final NoCivilSpawns plugin;
    private static final Set<Integer> blockerIds = new HashSet<Integer>(Arrays.asList(new Integer[] {41, 42}));
    private static final Set<Integer> treeIds = new HashSet<Integer>(Arrays.asList(new Integer[] {17, 18}) );
    private static final int goldId = 41;
    private static final int ironId = 42;


    public NoSpawnCreatureSpawnEvent(NoCivilSpawns instance) {
        plugin = instance;
    }

    @Override
    public void onCreatureSpawn(CreatureSpawnEvent event) {

        //Ignore the event if it's null - possible fix for null NPE when getting event location?
        if (event.equals(null) || event.getLocation().getWorld().equals(null))
            return;
        
        if (!event.getSpawnReason().equals(SpawnReason.NATURAL))
        	return;
        
        //gets the block at the location of spawn
        Location spawnLocation = event.getLocation();
        
        WorldSpawnConfiguration conf = NoCivilSpawns.worldConfig.get(spawnLocation.getWorld().getName());
        
        //Make sure we have a configuration stored in the map for the world that has loaded, why didn't we load a config yet?
        if (conf.equals(null)) {
            return;
        }
        
        if (conf.getWhitelistMobs().contains(event.getCreatureType().getName()) ) {
                return;
        } 


        if (!conf.getBlacklistMobs().isEmpty()) {
            if (conf.getBlacklistMobs().contains(event.getCreatureType().getName()) ) {
                event.setCancelled(true);
                return;
            }
        }

        if ( conf.getDiamondEnabler() )
            conf.getSpawnOkIds().add(57);

        // Checks for a spawner in a 9x9x3 cuboid
        if (testCuboid(1, 4, 0, 3, conf.getSpawnOkIds(), spawnLocation, conf)) {
            //NoCivilSpawns.log.info(NoCivilSpawns.plugName + " - Spawner Detected - Allowing Spawn");
            return;
        }

        if ( conf.getGoldBlocker() )
            conf.getBlockedIds().add(goldId);

        if (conf.getIronBlocker() )
            conf.getBlockedIds().add(ironId);


        // Check to see if we are spawning directly on one of these blocks, if we are, then abort.
        if ( conf.getQuickTest())
            if (testCuboid(1, 1, -1, -1, conf.getBlockedIds(), spawnLocation, conf)) {
                event.setCancelled(true);
                return;
            }

        //Test to make sure we aren't spawning on a tree or too close to one. for sure (wood blocks.)
        if (testCuboid(4, 2, -2, 0, treeIds, spawnLocation, conf)) {
            //Debug Code - NoCivilSpawns.log.info(NoCivilSpawns.plugName + " - Canceled Spawn - Attempted Tree Spawn");
            event.setCancelled(true);
            return;
        }
        // figure out how low we need to check the spawn.
        int minY = (int) Math.floor(conf.getHeight() / 2);
        int maxY = conf.getHeight() - minY;
        //The main cube tester.
        if (testCuboid(15, conf.getRadius(), -minY, maxY, WorldSpawnConfiguration.getBlacklistids(), spawnLocation, conf)) {
            //Debug code - NoCivilSpawns.log.info(NoCivilSpawns.plugName + " - Canceled Spawn - Too close to civilization");
            event.setCancelled(true);
            return;
        }
    }

    // Cube Testing member - checks all blocks of a set BLOCKIDS until MAX number is found within a RADIUS, from MINY to MAXY, around the location of a BLOCKLOC.
    public static final boolean testCuboid(int max, int radius, int minY, int maxY, Set<Integer> blockIds, Location blockloc, WorldSpawnConfiguration conf) {
        final World world = blockloc.getWorld();
        final int blockX = blockloc.getBlockX();
        final int blockY = blockloc.getBlockY();
        final int blockZ = blockloc.getBlockZ();

        int count = 0;
        for (int y = blockY+minY; y <= blockY+maxY; y++) {
            for (int x = blockX-radius; x <= blockX+radius; x++) {
                for (int z = blockZ-radius; z <= blockZ+radius; z++) {
                    final int blockId = world.getBlockTypeIdAt(x, y, z);
                    if (blockIds.contains(blockId)) {
                        count++;
                        if (count >= max)
                            return true;
                    }					
                    if ( (conf.getGoldBlocker() || conf.getIronBlocker() ) && blockerIds.contains(blockId) )  {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}