package com.sleaker.NoCivilSpawns;


import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;


public class NoSpawnWorldLoadEvent extends WorldListener{

	public final NoCivilSpawns plugin;


	public NoSpawnWorldLoadEvent(NoCivilSpawns instance) {
		plugin = instance;

	}

	public void onWorldLoad(WorldLoadEvent event) {
		NoCivilSpawns.setupWorld(event.getWorld().getName());
	}


}
