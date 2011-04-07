package com.sleaker.NoCivilSpawns;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;

public class NoSpawnCreatureSpawnEvent extends EntityListener{
	public static NoCivilSpawns plugin;
	
	public NoSpawnCreatureSpawnEvent(NoCivilSpawns instance) {
        plugin = instance;
    }
	
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		
		int[] treeids = {17, 18};
		int[] blacklistids = {4, 5, 20, 35, 44, 45, 54, 62, 64, 65, 67, 85};
		int[] spawnerid = {52};
		boolean spawner = false;
		boolean allowSpawn = true;
		//gets the block at the location of spawn
		Block spawnblock = event.getEntity().getWorld().getBlockAt(event.getLocation().getBlockX(), event.getLocation().getBlockY()-1, event.getLocation().getBlockZ());
		
		
		if (allowSpawn) {
			spawner = !testCuboid(1, 5, 5, spawnerid, spawnblock.getLocation());
			//if (spawner)
				//LimitSpawns.log.info("[NoCivilSpawns] - Spawner Detected - Allowing Spawn");
		}
		
		//Test to make sure we aren't spawning on a tree or too close to one. for sure (wood blocks.)
		if (allowSpawn && !spawner) {
			allowSpawn = testCuboid(4, 5, -2, treeids, spawnblock.getLocation());
			//if (!allowSpawn)
				//LimitSpawns.log.info("[NoCivilSpawns] - Canceled Spawn - Attempted Tree Spawn");
		}
		if (allowSpawn && !spawner) {
			allowSpawn = testCuboid(15, 20, 5, blacklistids, spawnblock.getLocation());
			//if (!allowSpawn)
				//LimitSpawns.log.info("[NoCivilSpawns] - Canceled Spawn - Too close to civilization");
		}
			
		if (!allowSpawn && !spawner) {
			event.setCancelled(true);
		}
	}

	
	public boolean testCuboid(int max, int sizeX, int sizeY, int[] blockids, Location blockloc) {
		int count = 0;
		boolean countUp = true;
		if (sizeY < 0) {
			countUp = false;
			sizeY = -sizeY;
		}
		for (int i = 0; i < sizeY; i++)
			for (int j = 0; j < sizeX; j++)
				for (int k = 0; k < sizeX; k++) {
					for (int l = 0; l < blockids.length; l++) {
						
						if (countUp) 
							if ( blockids[l] == blockloc.getWorld().getBlockAt(blockloc.getBlockX() + j, blockloc.getBlockY() + i, blockloc.getBlockZ() + k).getTypeId() )
							count++;
						
						if (!countUp)
							if ( blockids[l] == blockloc.getWorld().getBlockAt(blockloc.getBlockX() + j, blockloc.getBlockY() - i, blockloc.getBlockZ() + k).getTypeId() )
								count++;
						
					}
					if (count >= max)
						return false;
				}
		
		return true;
	}
}