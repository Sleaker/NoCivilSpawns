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
	private Configuration config;
	//static Map<String, List<Boolean>> options = new HashMap<String, List<Boolean>>();
	//private List<String> options;
	public static Logger log = Logger.getLogger("Minecraft");

	public void onDisable() {
		log.info("[NoCivilSpawns] Disabled");
	}

	public void onEnable() {
		
		File yml = new File(getDataFolder()+"/config.yml");
		
        if (!yml.exists()) {
        	new File(getDataFolder().toString()).mkdir();
    	    try {
    	    	yml.createNewFile();
    	    }
    	    catch (IOException ex) {
    	    	System.out.println("[NoCivilSpawns] - Cannot create configuration file. And none to load, using defaults.");
    	    }
        }	

        config = getConfiguration();
        
        if ( config.getKeys(null).isEmpty() ) {
        	config.setProperty("quicktest", true);
        	config.setProperty("goldblocker", false);
        	config.setProperty("daimondenabler", false);
        	config.save();
        }
        config.getBoolean("quicktest", quick);
        config.getBoolean("goldblocker", goldBlocker);
        config.getBoolean("daimondenabler", daimondEnabler);
        
        
		//Create the pluginmanager pm.
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.CREATURE_SPAWN, spawnListener, Priority.Normal, this);

		//Get the infomation from the plugin.yml file.
		PluginDescriptionFile pdfFile = this.getDescription();
		//Print that the plugin has been enabled!
		log.info("[NoCivilSpawns] version " + pdfFile.getVersion() + " by Sleaker is enabled!");


	}
}
