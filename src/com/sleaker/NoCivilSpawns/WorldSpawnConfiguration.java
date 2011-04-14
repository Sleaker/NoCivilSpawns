package com.sleaker.NoCivilSpawns;

import java.util.HashSet;
import java.util.Set;

public class WorldSpawnConfiguration {
	private Boolean quick = true;
	private Boolean goldBlocker = false;
	private Boolean ironBlocker = false;
	private Boolean diamondEnabler = false;
	private Boolean monstersOnly = false;
	private Set<String> whitelistMobs = new HashSet<String>(); 
	private Set<String> blacklistMobs = new HashSet<String>();
	private Set<Integer> blockedIds = makeSet ( new int[] {4, 5, 20, 45} );
	private Set<Integer> spawnOkIds = makeSet( new int[] {52} );
	private static final Set<Integer> blacklistIds = makeSet( new int[] {4, 5, 20, 35, 43, 44, 45, 54, 62, 64, 65, 67, 85} );



	private static final Set<Integer> makeSet(final int[] array) {
		Set<Integer> set = new HashSet<Integer>();
		for (int i : array) {
			set.add(i);
		}
		return set;
	}

	public WorldSpawnConfiguration() {

	}


	public Boolean getQuick() {
		return quick;
	}

	public void setGoldBlocker(Boolean goldBlocker) {
		this.goldBlocker = goldBlocker;
	}

	public Boolean getGoldBlocker() {
		return goldBlocker;
	}

	public Boolean getIronBlocker() {
		return ironBlocker;
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

}
