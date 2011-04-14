package com.sleaker.NoCivilSpawns;

/**
 * A plugin for detecting spawns near pre-defined blockIDs
 *
 * @author Sleaker
 *
 */
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;


public class NoCivilSpawns extends JavaPlugin{
	private final NoSpawnCreatureSpawnEvent spawnListener = new NoSpawnCreatureSpawnEvent(this);
	private final NoSpawnWorldLoadEvent worldLoadListener = new NoSpawnWorldLoadEvent(this);
	public static HashMap<String, WorldSpawnConfiguration> worldConfig = new HashMap<String, WorldSpawnConfiguration>();
	static List<String> whitelist;
	static List<String> blacklist;
	static List<String> creatures = new ArrayList<String>(Arrays.asList("Wolf", "Chicken", "Cow", "Pig", "Sheep")) ;
	static final String plugName = "[NoCivilSpawns]";
	static Configuration config;


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

		List<World> worlds = getServer().getWorlds();

		for ( World world : worlds)
			setupWorld(world.getName());

		//Create the pluginmanager pm.
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.CREATURE_SPAWN, spawnListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.WORLD_LOAD, worldLoadListener, Priority.Monitor, this);


		//Print that the plugin was successfully enabled!
		log.info(plugName + " - " + pdfFile.getVersion() + " by Sleaker is enabled!");


	}

	public static void setupWorld (String worldName) {

		worldConfig.put(worldName, new WorldSpawnConfiguration());
		if ( !config.getKeys(null).contains(worldName) ) {	
			setConfigDefaults(worldName);
			log.info(plugName + " " + worldName + " - Generating defaults.");	
		}
		else if ( !config.getKeys(null).contains(worldName) ) {
			setConfigDefaults(worldName);
			log.info(plugName + " " + worldName + " - Generating defaults.");	
		}
		String enabledString = " " + worldName + " - Enabled options: ";

		WorldSpawnConfiguration conf = worldConfig.get(worldName);
		if ( config.getBoolean(worldName+".quicktest", conf.getQuick()) )
			enabledString += " QuickTest";	

		if ( config.getBoolean(worldName+".goldblocker", conf.getGoldBlocker()) )	
			enabledString += " GoldBlocker";

		if (config.getBoolean(worldName+".ironblocker", conf.getIronBlocker()) )
			enabledString += " IronBlocker";

		if ( config.getBoolean(worldName+".diamondenabler", conf.getDiamondEnabler()) )
			enabledString += " DiamondEnabler";

		if ( config.getBoolean(worldName+".monstersonly", conf.getMonstersOnly()) )
			conf.getWhitelistMobs().addAll(creatures);

		conf.getWhitelistMobs().addAll(config.getStringList(worldName+".whitelistmobs", whitelist));
		conf.getBlacklistMobs().addAll(config.getStringList(worldName+".blacklistmobs", blacklist));

		if ( conf.getWhitelistMobs().size() > 0 ) 
			log.info(plugName + " - Whitelisted mobs on " + worldName + ": " + conf.getWhitelistMobs().toString()); 

		if ( conf.getBlacklistMobs().size() > 0 )
			log.info(plugName + " - Blacklisted mobs on " + worldName + ": " + conf.getBlacklistMobs().toString() );

		log.info(plugName + enabledString);
	}

	public static void setConfigDefaults (String worldName) {

		config.setProperty(worldName+".quicktest", true);
		config.setProperty(worldName+".goldblocker", false);
		config.setProperty(worldName+".ironblocker", false);
		config.setProperty(worldName+".diamondenabler", false);
		config.setProperty(worldName+".monstersonly", false);
		config.setProperty(worldName+".whitelistmobs", null);
		config.setProperty(worldName+".blacklistmobs", null);
		config.save();

		return;
	}


}
