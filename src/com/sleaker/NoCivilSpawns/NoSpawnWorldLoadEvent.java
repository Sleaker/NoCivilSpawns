package com.sleaker.NoCivilSpawns;

import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;


public class NoSpawnWorldLoadEvent extends WorldListener{
	
	public final NoCivilSpawns plugin;
	static final Logger log = NoCivilSpawns.log;
	private static final String plugName = NoCivilSpawns.plugName;

	
	public NoSpawnWorldLoadEvent(NoCivilSpawns instance) {
		plugin = instance;
			
	}
	
    public void onWorldLoad(WorldLoadEvent event){
    	final World world = event.getWorld();
    	final String worldName = world.getName();
    	NoCivilSpawns.worldConfig.put(worldName, new WorldSpawnConfiguration());
    	
    	if ( !NoCivilSpawns.config.getKeys(null).contains(worldName) ) {
    		setConfigDefaults(worldName);
			log.info(NoCivilSpawns.plugName + " - Generating defaults for " + world.getName());	
		}
    	
    	String enabledString = " - Enabled options for " + worldName + ":";
    	
    	
		WorldSpawnConfiguration conf = NoCivilSpawns.worldConfig.get(worldName);
		if ( NoCivilSpawns.config.getBoolean(worldName+".quicktest", conf.getQuick()) )
			enabledString += " QuickTest";	
		
		if ( NoCivilSpawns.config.getBoolean(worldName+".goldblocker", conf.getGoldBlocker()) )	
			enabledString += " GoldBlocker";
		
		if ( NoCivilSpawns.config.getBoolean(worldName+".ironblocker", conf.getIronBlocker()) )
			enabledString += " IronBlocker";
		
		if ( NoCivilSpawns.config.getBoolean(worldName+".diamondenabler", conf.getDiamondEnabler()) )
			enabledString += " DiamondEnabler";
		
		if ( NoCivilSpawns.config.getBoolean(worldName+".monstersonly", conf.getMonstersOnly()) )
			conf.getWhitelistMobs().addAll(NoCivilSpawns.creatures);
		
		conf.getWhitelistMobs().addAll(NoCivilSpawns.config.getStringList(worldName+".whitelistmobs", NoCivilSpawns.whitelist));
		conf.getBlacklistMobs().addAll(NoCivilSpawns.config.getStringList(worldName+".blacklistmobs", NoCivilSpawns.blacklist));
		
		if ( conf.getWhitelistMobs().size() > 0 ) 
			log.info(plugName + " - Whitelisted mobs on " + worldName + ": " + conf.getWhitelistMobs().toString()); 
		
		if ( conf.getBlacklistMobs().size() > 0 )
			log.info(plugName + " - Blacklisted mobs on " + worldName + ": " + conf.getBlacklistMobs().toString() );

		log.info(plugName + enabledString);
    }
    
	public static void setConfigDefaults (String worldName) {
		
		NoCivilSpawns.config.setProperty(worldName+".quicktest", true);
		NoCivilSpawns.config.setProperty(worldName+".goldblocker", false);
		NoCivilSpawns.config.setProperty(worldName+".ironblocker", false);
		NoCivilSpawns.config.setProperty(worldName+".diamondenabler", false);
		NoCivilSpawns.config.setProperty(worldName+".monstersonly", false);
		NoCivilSpawns.config.setProperty(worldName+".whitelistmobs", null);
		NoCivilSpawns.config.setProperty(worldName+".blacklistmobs", null);
		NoCivilSpawns.config.save();
		
		return;
	}
	
}
