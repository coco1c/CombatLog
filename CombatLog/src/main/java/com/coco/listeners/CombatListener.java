package com.coco.listeners;

import com.coco.CombatLog;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;
import java.util.UUID;

public class CombatListener implements Listener {

	private final List<UUID> disabledWorldUuids;
	private final CombatLog plugin;
	private final int tagTime;

	public CombatListener(List<UUID> disabledWorldUuids, CombatLog plugin, int tagTime) {
		this.disabledWorldUuids = disabledWorldUuids;
		this.plugin = plugin;
		this.tagTime = tagTime;
	}

	@EventHandler(ignoreCancelled = true)
	public void onDamage(EntityDamageByEntityEvent event) {
		if (isWorldDisabled((Player) event.getDamager())) return;

		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			if (event.getEntity().hasMetadata("NPC")) return;

			plugin.getCombatTagManager().tag((Player) event.getDamager(), (Player) event.getEntity(), tagTime);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (plugin.getConfiguration().getStringList("executable-commands-during-combat").contains(event.getMessage())) return;
		Player player = event.getPlayer();

		if (plugin.getCombatTagManager().isTagged(player)) {
			event.setCancelled(true);
			player.sendMessage(plugin.getConfigMessage("cannot-execute-during-combat"));
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		if (plugin.getCombatTagManager().isTagged(player)) {
			event.setCancelled(true);
			player.sendMessage(plugin.getConfigMessage("cannot-open-inventory"));
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (plugin.getCombatTagManager().isTagged(event.getPlayer())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(plugin.getConfigMessage("cannot-teleport"));
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (plugin.getCombatTagManager().isTagged(player)) {
			plugin.getCombatTagManager().untag(player);
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (plugin.getCombatTagManager().isTagged(player)) {
			player.setHealth(0d);
		}
	}

	private boolean isWorldDisabled(Player player) {
		return disabledWorldUuids.contains(player.getWorld().getUID());
	}
}
