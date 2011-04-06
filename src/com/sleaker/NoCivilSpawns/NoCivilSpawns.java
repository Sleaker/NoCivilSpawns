package com.sleaker.NoCivilSpawns;

import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NoCivilSpawns extends JavaPlugin{
	private final NoSpawnCreatureSpawnEvent spawnListener = new NoSpawnCreatureSpawnEvent(this);
	
	public static Logger log = Logger.getLogger("Minecraft");

	public void onDisable() {
		log.info("[NoCivilSpawns] Disabled");
	}

	public void onEnable() {
        
        //Create the pluginmanage pm.
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.CREATURE_SPAWN, spawnListener, Priority.Normal, this);
        
        //Get the infomation from the plugin.yml file.
        PluginDescriptionFile pdfFile = this.getDescription();
        //Print that the plugin has been enabled!
        log.info("[NoCivilSpawns] version " + pdfFile.getVersion() + " by Sleaker is enabled!");
	}
}
