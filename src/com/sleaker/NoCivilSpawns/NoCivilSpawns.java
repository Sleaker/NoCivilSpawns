package com.sleaker.NoCivilSpawns;

/**
 * A plugin for detecting spawns near pre-defined blockIDs
 *
 * @author Sleaker
 *
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	static final Boolean ironBlocker = false;
	static final Boolean diamondEnabler = false;
	static final Boolean monstersonly = false;
	static final String plugName = "[NoCivilSpawns]";
	private static List<String> whitelist;
	private static List<String> blacklist;
	private static List<String> creatures = new ArrayList<String>(Arrays.asList("Wolf", "Chicken", "Cow", "Pig", "Sheep")) ;
 	private Configuration config;
	static Set<String> whitelistmobs = new HashSet<String>(); 
	static Set<String> blacklistmobs = new HashSet<String>();
	
	public static Logger log = Logger.getLogger("Minecraft");

	public void onDisable() {
		log.info(plugName + " Disabled");
	}

	public void onEnable() {
		//Get the information from the plugin.yml file.
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
       //Attempt to load in the data from the configuration file.
        if ( config.getKeys(null).isEmpty() ) {
        	config.setProperty("quicktest", true);
        	config.setProperty("goldblocker", false);
        	config.setProperty("ironblocker", false);
        	config.setProperty("diamondenabler", false);
        	config.setProperty("monstersonly", false);
        	config.setProperty("whitelistmobs", null);
        	config.setProperty("blacklistmobs", null);
        	log.info(plugName + " - No configuration file found. Generating defaults.");
        	config.save();
        }
        if ( config.getBoolean("quicktest", quick) )
        	log.info(plugName + " - Additional quick-detection method enabled.");
        if ( config.getBoolean("goldblocker", goldBlocker) )
        	log.info(plugName + " - Gold Blocks will prevent mobs from spawning nearby.");
        if (config.getBoolean("ironblocker", ironBlocker) )
        	log.info(plugName + " - Iron Blocks will prevent mobs from spawning nearby.");
        if ( config.getBoolean("diamondenabler", diamondEnabler) )
        	log.info(plugName + " - Diamond blocks will always allow mobs to spawn nearby.");
        if ( config.getBoolean("monstersonly", true))
        	whitelistmobs.addAll(creatures);
        
        
        whitelistmobs.addAll(config.getStringList("whitelistmobs", whitelist));
        if ( whitelistmobs.size() > 0 ) {
        	log.info(plugName + " - Imported mob whitelist: " + whitelistmobs.toString()); 
        } 
        
        blacklistmobs.addAll(config.getStringList("blacklistmobs", blacklist));
        if ( blacklistmobs.size() > 0 ){
        	log.info(plugName + " - Imported mob blacklist: " + blacklistmobs.toString() );
        } 
        
    	
		//Create the pluginmanager pm.
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.CREATURE_SPAWN, spawnListener, Priority.Normal, this);


		//Print that the plugin was successfully enabled!
		log.info(plugName + " - " + pdfFile.getVersion() + " by Sleaker is enabled!");


	}
}
