package nl.thedutchmc.harotorch.commands.torchSubCmds;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import nl.thedutchmc.harotorch.HaroTorch;

public class HelpExecutor {

	public static boolean help(CommandSender sender) {
		
		sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + "HaroTorch help menu");
		sender.sendMessage("-  " + ChatColor.GOLD + "/torch help " + ChatColor.WHITE + "Shows you this page.");
		sender.sendMessage("-  " + ChatColor.GOLD + "/torch highlight " + ChatColor.WHITE + "Highlight all nearby torches.");
		sender.sendMessage("-  " + ChatColor.GOLD + "/torch give " + ChatColor.WHITE + "Give yourself a HaroTorch.");
		sender.sendMessage("-  " + ChatColor.GOLD + "/torch convert " + ChatColor.WHITE + "Convert v1 torches to v2 torches.");
		sender.sendMessage("-  " + ChatColor.GOLD + "/torch version " + ChatColor.WHITE + "Get the HaroTorch and NMS version number.");

		return true;
	}
	
}
