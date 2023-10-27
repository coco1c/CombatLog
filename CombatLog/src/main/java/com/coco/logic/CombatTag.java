package com.coco.logic;

import org.bukkit.entity.Player;

public class CombatTag {

	private final Player attacker;
	private final long expireTime;

	public CombatTag(Player attacker, long expireTime) {
		this.attacker = attacker;
		this.expireTime = expireTime + System.currentTimeMillis();
	}

	public long getExpireTime() {
		return expireTime;
	}

	public Player getAttacker() {
		return attacker;
	}

}
