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
import org.bukkit.event.entity.EntityListener;



public class NoSpawnCreatureSpawnEvent extends EntityListener {
	public final NoCivilSpawns plugin;
	private static final int goldId = 41;
	private static final int ironId = 42;
	private static final Set<Integer> treeIds = new HashSet<Integer>(Arrays.asList(new Integer[] {17, 18}) );


	
	public NoSpawnCreatureSpawnEvent(NoCivilSpawns instance) {
		plugin = instance;
	}
	

	public void onCreatureSpawn(CreatureSpawnEvent event) {

		//gets the block at the location of spawn
		Location spawnLocation = event.getLocation();
		
		WorldSpawnConfiguration conf = NoCivilSpawns.worldConfig.get(event.getLocation().getWorld().getName());
			
		if (conf.getWhitelistMobs().contains(event.getCreatureType().getName()) )
			return;
		
		if (conf.getBlacklistMobs().contains(event.getCreatureType().getName()) ) {
			event.setCancelled(true);
			return;
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

		
		// Check to see if we are spawning directly on one of these blocks, if we are, then abort.
		if ( conf.getQuick())
			if (testCuboid(1, 1, -1, -1, conf.getBlockedIds(), spawnLocation, conf)) {
				event.setCancelled(true);
				return;
			}
		
		//Test to make sure we aren't spawning on a tree or too close to one. for sure (wood blocks.)
		if (testCuboid(4, 2, -2, 0, treeIds, spawnLocation, conf)) {
			//NoCivilSpawns.log.info(NoCivilSpawns.plugName + " - Canceled Spawn - Attempted Tree Spawn");
			event.setCancelled(true);
			return;
		}

		if (testCuboid(15, 10, -2, 3, WorldSpawnConfiguration.getBlacklistids(), spawnLocation, conf)) {
			//NoCivilSpawns.log.info(NoCivilSpawns.plugName + " - Canceled Spawn - Too close to civilization");
			event.setCancelled(true);
			return;
		}
	}


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
					if ( conf.getGoldBlocker() && blockId == goldId)
						return true;
					if ( conf.getIronBlocker() && blockId == ironId )
						return true;
				}
			}
		}

		return false;
	}
}