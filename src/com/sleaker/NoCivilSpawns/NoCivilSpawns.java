package com.sleaker.NoCivilSpawns;

/**
 * A plugin for detecting spawns near pre-defined blockIDs
 *
 * @author Sleaker
 *
 */
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public static Map<String, WorldSpawnConfiguration> worldConfig = Collections.synchronizedMap(new HashMap<String, WorldSpawnConfiguration>());
    static List<String> whiteList;
    static List<String> blackList;
    static List<String> creatures = new ArrayList<String>(Arrays.asList("Wolf", "Chicken", "Cow", "Pig", "Sheep"));
    static List<String> monsters = new ArrayList<String>(Arrays.asList("Spider", "PigZombie", "Zombie", "Skeleton", "Giant", "Ghast", "Squid", "Slime", "Creeper"));
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

        //Create the pluginmanager pm and instantiate our listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.CREATURE_SPAWN, spawnListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.WORLD_LOAD, worldLoadListener, Priority.Monitor, this);


        //Print that the plugin was successfully enabled!
        log.info(plugName + " - " + pdfFile.getVersion() + " by Sleaker is enabled!");


    }

    public static void setupWorld (String worldName) {
        final Boolean temp = false;
        int tempInt = 0;

        worldConfig.put(worldName, new WorldSpawnConfiguration());
        if ( !config.getKeys(null).contains(worldName) ) {	
            setConfigDefaults(worldName);
            log.info(plugName + " " + worldName + " - Generating defaults.");	
        } else if (!config.getKeys(worldName).contains("enabled") ) {
            config.setProperty(worldName+".enabled", true);
            config.save();
        }

        String enabledString = " " + worldName + " - Enabled options: ";
        WorldSpawnConfiguration conf = worldConfig.get(worldName);



        if ( !config.getBoolean(worldName+".enabled", temp)) {
            conf.getWhitelistMobs().addAll(monsters);
            conf.getWhitelistMobs().addAll(creatures);
            conf.setQuickTest(false);
            conf.setHeight(7);
            conf.setGoldBlocker(false);
            conf.setIronBlocker(false);
            conf.setMonstersOnly(false);
            conf.setRadius(10);
            conf.setDiamondEnabler(false);
            log.info(plugName + " Mob blocking will be disabled for: " + worldName);
            return;
        }

        if ( config.getBoolean(worldName+".quickTest", temp) ) {
            enabledString += " QuickTest";	
            conf.setQuickTest(true);
        }

        if ( config.getBoolean(worldName+".goldBlocker", temp) )	{
            enabledString += " GoldBlocker";
            conf.setGoldBlocker(true);
        }

        if (config.getBoolean(worldName+".ironBlocker", temp) ) {
            enabledString += " IronBlocker";
            conf.setIronBlocker(true);
        }

        if ( config.getBoolean(worldName+".diamondEnabler", temp) ) {
            enabledString += " DiamondEnabler";
            conf.setDiamondEnabler(true);
        }

        if ( config.getBoolean(worldName+".monstersOnly", temp) ) {
            conf.getWhitelistMobs().addAll(creatures);
            conf.setMonstersOnly(true);
        }

        if (config.getInt(worldName+".cubeRadius", tempInt) == 0) {
            config.setProperty(worldName+".cubeRadius", 10);
            config.save();
            conf.setRadius(10);
        }
        else
            conf.setRadius(config.getInt(worldName+".cubeRadius", 10));

        if (config.getInt(worldName+".cubeHeight", tempInt) == 0) {
            config.setProperty(worldName+".cubeHeight", 7);
            config.save();
            conf.setHeight(7);
        }
        else
            conf.setHeight(config.getInt(worldName+".cubeHeight", 7));

        tempInt = 0;

        conf.getWhitelistMobs().addAll(config.getStringList(worldName+".whitelistMobs", whiteList));
        conf.getBlacklistMobs().addAll(config.getStringList(worldName+".blacklistMobs", blackList));

        if ( conf.getWhitelistMobs().size() > 0 ) 
            log.info(plugName + " - Whitelisted mobs on " + worldName + ": " + conf.getWhitelistMobs().toString()); 

        if ( conf.getBlacklistMobs().size() > 0 )
            log.info(plugName + " - Blacklisted mobs on " + worldName + ": " + conf.getBlacklistMobs().toString() );

        log.info(plugName + enabledString);
    }

    public static void setConfigDefaults (String worldName) {

        config.setProperty(worldName+".enabled", true);
        config.setProperty(worldName+".quickTest", true);
        config.setProperty(worldName+".goldBlocker", false);
        config.setProperty(worldName+".ironBlocker", false);
        config.setProperty(worldName+".diamondEnabler", false);
        config.setProperty(worldName+".monstersOnly", false);
        config.setProperty(worldName+".whitelistMobs", null);
        config.setProperty(worldName+".blacklistMobs", null);
        config.setProperty(worldName+".cubeRadius", 10);
        config.setProperty(worldName+".cubeHeight", 7);
        config.save();

        return;
    }


}
