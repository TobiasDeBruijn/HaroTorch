package nl.thedutchmc.harotorch.commands.torchSubCmds;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import nl.thedutchmc.harotorch.HaroTorch;

public class VersionExecutor {

	public static boolean version(CommandSender sender, HaroTorch plugin) {
		
		sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + "You are running HaroTorch version " + ChatColor.RED + plugin.getDescription().getVersion() + ChatColor.GOLD + " and NMS version " + ChatColor.RED + HaroTorch.NMS_VERSION);
		
		return true;
	}
	
}
