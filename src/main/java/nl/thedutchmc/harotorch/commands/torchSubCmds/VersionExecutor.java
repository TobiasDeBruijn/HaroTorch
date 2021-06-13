
package nl.thedutchmc.harotorch.commands.torchSubCmds;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.commands.SubCommand;

public class VersionExecutor implements SubCommand {

	public boolean run(HaroTorch plugin, CommandSender sender, String[] args) {
		sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + "You are running HaroTorch version " + ChatColor.RED + plugin.getDescription().getVersion() + ChatColor.GOLD + " and NMS version " + ChatColor.RED + HaroTorch.NMS_VERSION);
		return true;
	}
	
}
