package dev.array21.harotorch.commands.torchSubCmds;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import dev.array21.harotorch.HaroTorch;
import dev.array21.harotorch.commands.SubCommand;
import dev.array21.harotorch.lang.LangHandler;

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
