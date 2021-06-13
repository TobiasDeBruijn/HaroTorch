package nl.thedutchmc.harotorch.commands.torchSubCmds;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.commands.SubCommand;
import nl.thedutchmc.harotorch.lang.LangHandler;

public class HelpExecutor implements SubCommand {

	public boolean run(HaroTorch plugin, CommandSender sender, String[] args) {		
		sender.sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("helpMenuTitle"));
		sender.sendMessage("- " + ChatColor.GOLD + "/torch help " + ChatColor.WHITE + LangHandler.activeLang.getLangMessages().get("helpHelp"));
		sender.sendMessage("- " + ChatColor.GOLD + "/torch highlight " + ChatColor.WHITE + LangHandler.activeLang.getLangMessages().get("helpHighlight"));
		sender.sendMessage("- " + ChatColor.GOLD + "/torch give " + ChatColor.WHITE + LangHandler.activeLang.getLangMessages().get("helpGive"));
		sender.sendMessage("- " + ChatColor.GOLD + "/torch convert " + ChatColor.WHITE + LangHandler.activeLang.getLangMessages().get("helpConvert"));
		sender.sendMessage("- " + ChatColor.GOLD + "/torch version " + ChatColor.WHITE + LangHandler.activeLang.getLangMessages().get("helpVersion"));
		sender.sendMessage("- " + ChatColor.GOLD + "/torch aoe " + ChatColor.WHITE + LangHandler.activeLang.getLangMessages().get("helpAoe"));
		
		return true;
	}
}
