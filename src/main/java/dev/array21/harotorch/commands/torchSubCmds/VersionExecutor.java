
package dev.array21.harotorch.commands.torchSubCmds;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import dev.array21.harotorch.HaroTorch;
import dev.array21.harotorch.commands.SubCommand;

public class VersionExecutor implements SubCommand {

	public boolean run(HaroTorch plugin, CommandSender sender, String[] args) {
		sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + "You are running HaroTorch version " + ChatColor.RED + plugin.getDescription().getVersion() + ChatColor.GOLD + " and NMS version " + ChatColor.RED + HaroTorch.NMS_VERSION);
		return true;
	}
	
}
