package com.sleaker.NoCivilSpawns;


import org.bukkit.event.world.WorldListener;

import org.bukkit.event.world.WorldLoadEvent;
/*
 * Detects when a new world is loaded in and runs the configuration loader for the world.
 */

public class NoSpawnWorldLoadEvent extends WorldListener{

	public final NoCivilSpawns plugin;


	public NoSpawnWorldLoadEvent(NoCivilSpawns instance) {
		plugin = instance;
	}

	public void onWorldLoad(WorldLoadEvent event) {
		NoCivilSpawns.setupWorld(event.getWorld().getName());
	}


}
