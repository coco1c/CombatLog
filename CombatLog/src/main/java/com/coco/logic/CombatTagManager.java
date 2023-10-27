package com.coco.logic;

import com.coco.CombatLog;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CombatTagManager {

	private final CombatLog plugin;

	public final Map<Player, CombatTag> tagMap = new HashMap<>();

	public CombatTagManager(CombatLog plugin) {
		this.plugin = plugin;
	}

	public void tag(final Player attacker, final Player victim, final long time) {
		if (!isTagged(attacker)) {
			attacker.sendMessage(plugin.getConfigMessage("in-combat-quit-warning"));
		}
		if (!isTagged(victim)) {
			victim.sendMessage(plugin.getConfigMessage("in-combat-quit-warning"));
		}
		tagMap.put(attacker, new CombatTag(attacker, time));
		tagMap.put(victim, new CombatTag(attacker, time));
	}

	public void untag(final Player player) {
		tagMap.keySet().removeIf(player1 -> player1.equals(player));

		player.sendMessage(plugin.getConfigMessage("combat-ended"));
	}

	public boolean isTagged(final Player player) {
		return tagMap.containsKey(player);
	}

}
