package com.sleaker.NoCivilSpawns;

/**
 * A plugin for detecting spawns near pre-defined blockIDs
 *
 * @author Sleaker
 *
 */

import java.util.HashSet;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;


public class NoSpawnCreatureSpawnEvent extends EntityListener {
	public final NoCivilSpawns plugin;
	private static final Set<Integer> blockedIds = makeSet ( new int[] {4, 5, 20, 45} );
	private static final Set<Integer> blacklistIds = makeSet( new int[] {4, 5, 20, 35, 44, 45, 54, 62, 64, 65, 67, 85} );
	private static final Set<Integer> treeIds = makeSet( new int[] {17, 18} );
	private static final Set<Integer> spawnOkIds = makeSet( new int[] {52} );
	private static final int goldId = 41;


	private static final Set<Integer> makeSet(final int[] array) {
		Set<Integer> set = new HashSet<Integer>();
		for (int i : array) {
			set.add(i);
		}
		return set;
	}

	public NoSpawnCreatureSpawnEvent(NoCivilSpawns instance) {
		plugin = instance;
	}

	public void onCreatureSpawn(CreatureSpawnEvent event) {
			
		//gets the block at the location of spawn
		Location spawnLocation = event.getLocation();
		
		if (NoCivilSpawns.whitelistmobs.contains(event.getCreatureType()) )
			return;
		
		if (NoCivilSpawns.blacklistmobs.contains(event.getCreatureType()) ) {
			event.setCancelled(true);
			return;
		}
				
		if ( NoCivilSpawns.diamondEnabler )
			spawnOkIds.add(57);
		
		// Checks for a spawner in a 9x9x3 cuboid
		if (testCuboid(1, 4, 0, 3, spawnOkIds, spawnLocation)) {
			//NoCivilSpawns.log.info(NoCivilSpawns.plugName + " - Spawner Detected - Allowing Spawn");
			return;
		}

		if (NoCivilSpawns.goldBlocker)
			blockedIds.add(goldId);

		
		// Check to see if we are spawning directly on one of these blocks, if we are, then abort.
		if (NoCivilSpawns.quick)
			if (testCuboid(1, 1, -1, -1, blockedIds, spawnLocation)) {
				event.setCancelled(true);
				return;
			}
		
		//Test to make sure we aren't spawning on a tree or too close to one. for sure (wood blocks.)
		if (testCuboid(4, 2, -2, 0, treeIds, spawnLocation)) {
			//NoCivilSpawns.log.info(NoCivilSpawns.plugName + " - Canceled Spawn - Attempted Tree Spawn");
			event.setCancelled(true);
			return;
		}

		if (testCuboid(15, 10, -2, 3, blacklistIds, spawnLocation)) {
			//NoCivilSpawns.log.info(NoCivilSpawns.plugName + " - Canceled Spawn - Too close to civilization");
			event.setCancelled(true);
			return;
		}
	}


	public static final boolean testCuboid(int max, int radius, int minY, int maxY, Set<Integer> blockIds, Location blockloc) {
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
					if (NoCivilSpawns.goldBlocker && blockId == goldId)
						return true;
				}
			}
		}

		return false;
	}
}