package com.coco.listeners;

import com.coco.CombatLog;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

public class ReloadCMD implements CommandExecutor {

	private final CombatLog plugin;

	public ReloadCMD(CombatLog plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			if (!commandSender.isOp()) return false;
		}
		commandSender.sendMessage(ChatColor.GREEN + "Reloading Combat Log configuration...");
		plugin.loadConfig();
		commandSender.sendMessage(ChatColor.GREEN + "Reloaded configuration!");
		return true;
	}
}
