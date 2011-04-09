package com.sleaker.NoCivilSpawns;

/**
 * A plugin for detecting spawns near pre-defined blockIDs
 *
 * @author Sleaker
 *
 */
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class NoCivilSpawns extends JavaPlugin{
	private final NoSpawnCreatureSpawnEvent spawnListener = new NoSpawnCreatureSpawnEvent(this);
	static final Boolean quick = true;
	static final Boolean goldBlocker = false;
	static final Boolean daimondEnabler = false;
	static final String plugName = "[NoCivilSpawns]";
	private Configuration config;
	
	public static Logger log = Logger.getLogger("Minecraft");

	public void onDisable() {
		log.info(plugName + " Disabled");
	}

	public void onEnable() {
		//Get the infomation from the plugin.yml file.
		PluginDescriptionFile pdfFile = this.getDescription();
		
		//Check to see if there is a configuration file.
		File yml = new File(getDataFolder()+"/config.yml");
		
        if (!yml.exists()) {
        	new File(getDataFolder().toString()).mkdir();
    	    try {
    	    	yml.createNewFile();
    	    }
    	    catch (IOException ex) {
    	    	log.info(plugName + " - Cannot create configuration file. And none to load, using defaults.");
    	    }
        }	
        
        
        config = getConfiguration();
      //Attempt to load in the configuration file.
        if ( config.getKeys(null).isEmpty() ) {
        	config.setProperty("quicktest", true);
        	config.setProperty("goldblocker", false);
        	config.setProperty("daimondenabler", false);
        	log.info(plugName + " - No configuration file found. Generating defaults.");
        	config.save();
        }
        if ( config.getBoolean("quicktest", quick) )
        	log.info(plugName + " - Additional quick-detection method enabled.");
        if ( config.getBoolean("goldblocker", goldBlocker) )
        	log.info(plugName + " - Gold Blocks will prevent mobs from spawning nearby.");
        if ( config.getBoolean("daimondenabler", daimondEnabler) )
        	log.info(plugName + " - Daimond blocks will always allow mobs to spawn.");       
        
		//Create the pluginmanager pm.
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.CREATURE_SPAWN, spawnListener, Priority.Normal, this);


		//Print that the plugin was successfully enabled!
		log.info(plugName + " - " + pdfFile.getVersion() + " by Sleaker is enabled!");


	}
}
