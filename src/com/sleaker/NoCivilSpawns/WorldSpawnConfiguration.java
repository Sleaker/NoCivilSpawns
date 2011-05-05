package com.sleaker.NoCivilSpawns;

import java.util.HashSet;
import java.util.Set;
/*
 * Class to hold per-world configuration.  This class is instantiated when a new node is detected at the root of the config.yml (Denoting a configuration for a world)
 */
public class WorldSpawnConfiguration {
	private Boolean enabled = true;
	private Boolean quickTest = true;
	private Boolean goldBlocker = false;
	private Boolean ironBlocker = false;
	private Boolean diamondEnabler = false;
	private Boolean monstersOnly = false;
	private int cubeRadius = 0;
	private int cubeHeight = 0;
	private Set<String> whitelistMobs = new HashSet<String>(); 
	private Set<String> blacklistMobs = new HashSet<String>();
	private Set<Integer> blockedIds = makeSet ( new int[] {4, 5, 20, 45} );
	private Set<Integer> spawnOkIds = makeSet( new int[] {52} );
	private static final Set<Integer> blacklistIds = makeSet( new int[] {4, 5, 20, 22, 35, 43, 44, 45, 53, 54, 62, 64, 65, 67, 85} );


	//Simple set creation from an integer array. 
	private static final Set<Integer> makeSet(final int[] array) {
		Set<Integer> set = new HashSet<Integer>();
		for (int i : array) {
			set.add(i);
		}
		return set;
	}

	//Empty Constructor - no need to run anything here.
	public WorldSpawnConfiguration() {

	}

	public Boolean getQuickTest() {
		return quickTest;
	}

	public void setQuickTest(Boolean quickTest) {
		this.quickTest = quickTest;
	}
	
	public void setGoldBlocker(Boolean goldBlocker) {
		this.goldBlocker = goldBlocker;
	}

	public Boolean getGoldBlocker() {
		return goldBlocker;
	}
	
	public void setIronBlocker(Boolean ironBlocker) {
		this.ironBlocker = ironBlocker;
	}

	public Boolean getIronBlocker() {
		return ironBlocker;
	}
	
	public int getRadius() {
		return cubeRadius;
	}
	
	public void setRadius(int radius) {
		this.cubeRadius = radius;
	}
	
	public int getHeight() {
		return cubeHeight;
	}
	
	public void setHeight(int height) {
		this.cubeHeight = height;
	}
	
	public void setDiamondEnabler(Boolean diamondEnabler) {
		this.diamondEnabler = diamondEnabler;
	}

	public Boolean getDiamondEnabler() {
		return diamondEnabler;
	}

	public Set<String> getWhitelistMobs() {
		return whitelistMobs;
	}

	public Boolean getMonstersOnly() {
		return monstersOnly;
	}

	public void setMonstersOnly(Boolean monstersOnly) {
		this.monstersOnly = monstersOnly;
	}

	public Set<String> getBlacklistMobs() {
		return blacklistMobs;
	}

	public Set<Integer> getBlockedIds() {
		return blockedIds;
	}

	public Set<Integer> getSpawnOkIds() {
		return spawnOkIds;
	}

	public static Set<Integer> getBlacklistids() {
		return blacklistIds;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}


}
