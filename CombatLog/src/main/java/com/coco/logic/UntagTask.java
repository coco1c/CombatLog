package com.coco.logic;

import com.coco.CombatLog;
import com.hakan.core.HCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class UntagTask implements Runnable {

	private final CombatLog plugin;
	private String timerFormat;

	public UntagTask(final CombatLog plugin) {
		this.plugin = plugin;
	}

	public void register(final String timerFormat, final int checkInterval) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 5L, checkInterval);
		this.timerFormat = timerFormat;
	}

	@Override
	public void run() {
		for (final Player player : new ArrayList<>(plugin.getCombatTagManager().tagMap.keySet())) {
			final long timeLeftMillis = plugin.getCombatTagManager().tagMap.get(player).getExpireTime() - System.currentTimeMillis();
			if (timeLeftMillis > 0) {
				showRemainingTime(player , timeLeftMillis + 1000);
				continue;
			}
			plugin.getCombatTagManager().untag(player );
		}
	}

	private void showRemainingTime(final Player player, final long timeLeftMillis) {
		HCore.sendActionBar(player, timerFormat.replace("{time}",((int) timeLeftMillis / 1000) + ""));
	}
}